package websocket;

import model.*;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import static model.TmpStorage.listForDisconnections;

@ServerEndpoint(value = "/", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class MyServerEndpoint {

    private static final ConcurrentHashMap<String, ChatRoom> allChatRooms = new ConcurrentHashMap<>();
    private Session session;

    static {
        new DisconnectionsHandler().start();
        new UnusedNamesRemover().start();
    }

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        Message response = new Message();
        response.setMessageType(message.getMessageType());

        switch (message.getMessageType()) {
            case GENERAL:
                response.setContent(message.getContent());
                response.setFrom(message.getFrom());

                allChatRooms.get(message.getChatRoomName()).addToChatStory(response.getFrom() + ": " + response.getContent());

                response.setChatRoomName(message.getChatRoomName());
                sendMessageToEveryone(response);
                break;

            case USERNAME_CHECK:
                if (checkIfUsernameIsAvailable(message.getContent())) {
                    response.setContent("YES");
                } else {
                    response.setContent("NO");
                    TmpStorage.userNames.add(new UnusedName(message.getContent()));

                }
                session.getBasicRemote().sendObject(response);
                break;

            case CHAT_ROOM_NAME_CREATE:
                if (checkIfChatRoomNameIsAvailable(message.getContent())) {
                    response.setContent("YES");
                } else {
                    response.setContent("NO");
                    TmpStorage.chatRoomNames.add(new UnusedName(message.getContent()));
                }
                session.getBasicRemote().sendObject(response);
                break;

            case CHAT_ROOM_NAME_JOIN:
                if (allChatRooms.get(message.getContent()) != null) {
                    response.setContent("YES");
                } else {
                    response.setContent("NO");
                }
                session.getBasicRemote().sendObject(response);
                break;

            case JOIN_THE_ROOM:
                String[] info = message.getContent().split("/");

                User tmpUser;
                ChatRoom tmpChatRoom;

                if (allChatRooms.get(info[1]) != null) {
                    tmpChatRoom = allChatRooms.get(info[1]);
                } else {
                    tmpChatRoom = new ChatRoom(info[1]);
                    allChatRooms.put(tmpChatRoom.getName(), tmpChatRoom);
                }

                if (tmpChatRoom.getUser(info[0]).isPresent()) {
                    tmpUser = tmpChatRoom.getUser(info[0]).get();
                } else {
                    tmpUser = new User(info[0], tmpChatRoom.getName());
                    response.setContent(tmpUser.getName() + " joined!");
                }

                tmpChatRoom.getUsersEndpoints().put(tmpUser, this);

                TmpStorage.chatRoomNames.removeIf(e -> e.getName().equalsIgnoreCase(tmpChatRoom.getName()));
                TmpStorage.userNames.removeIf(e-> e.getName().equalsIgnoreCase(tmpUser.getName()));

                response.setActiveUsers(new ArrayList<>(tmpChatRoom.getUsersEndpoints().keySet().stream().
                        map(User::getName).collect(Collectors.toList())));

                response.setFrom(tmpUser.getName());

                Message chatStoryResponse = new Message();
                chatStoryResponse.setChatStory(tmpChatRoom.getChatStory());
                chatStoryResponse.setMessageType(MessageType.JOIN_WITH_STORY);
                session.getBasicRemote().sendObject(chatStoryResponse);

                if (response.getContent() != null)
                    tmpChatRoom.addToChatStory(response.getContent());

                response.setChatRoomName(message.getChatRoomName());
                sendMessageToEveryone(response);
                break;

            case USER_DISCONNECT:
                listForDisconnections.add(
                        new UserDisconnection(
                                allChatRooms.get(message.getChatRoomName())
                                        .getUser(message.getFrom()).get()
                        )
                );
                break;

            case USER_DISCONNECT_CANCELLATION:
                listForDisconnections.removeIf(e -> e.getUser().equals(
                        allChatRooms.get(message.getChatRoomName()).getUser(message.getFrom()).get()
                ));
                break;
        }
    }

    private Session getSession() {
        return session;
    }

    private static void disconnectUser(User u) {

        ChatRoom tmp = allChatRooms.get(u.getChatRoomName());
        Message message = new Message();
        message.setMessageType(MessageType.QUIT);
        message.setContent(u.getName() + " left this Chat Room");
        message.setFrom(u.getName());
        message.setActiveUsers(new ArrayList<>((allChatRooms.get(u.getChatRoomName())
                .getUsersEndpoints()).keySet().stream()
                .map(User::getName).collect(Collectors.toList())));
        message.getActiveUsers().remove(u.getName());
        message.setChatRoomName(tmp.getName());

        tmp.getUsersEndpoints().remove(u);

        sendMessageToEveryone(message);

        tmp.addToChatStory(message.getContent());

        if (tmp.getUsersEndpoints().size() == 0)
            allChatRooms.remove(tmp);
    }

    private static boolean checkIfChatRoomNameIsAvailable(String chatRoomName) {
        boolean res1 = TmpStorage.chatRoomNames.stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(chatRoomName));

        boolean res2 = allChatRooms.values().stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(chatRoomName));

        return res1 || res2;
    }

    private static boolean checkIfUsernameIsAvailable(String username) {
        boolean res1 = TmpStorage.userNames.stream()
                .anyMatch(e -> e.getName().equalsIgnoreCase(username));

        boolean res2 = allChatRooms.values().stream()
                .flatMap(r -> r.getUsersEndpoints().keySet().stream())
                .anyMatch(e -> username.equalsIgnoreCase(e.getName()));

        return res1 || res2;
    }

    private static void sendMessageToEveryone(Message message) {
        allChatRooms.get(message.getChatRoomName()).getUsersEndpoints().values()
                .forEach(endpoint -> {
                    synchronized (endpoint) {
                        try {
                            endpoint.getSession().getBasicRemote()
                                    .sendObject(message);
                        } catch (IOException | EncodeException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    static class DisconnectionsHandler extends Thread {
        @Override
        public void run() {
            while (true) {
                List<UserDisconnection> toBeRemoved = listForDisconnections.stream()
                        .filter(u -> Math.abs(Duration.between(LocalDateTime.now(),
                                u.getTimeOfDisconnection()).getSeconds()) > 5)
                        .collect(Collectors.toList());

                toBeRemoved.forEach(u -> {
                    disconnectUser(u.getUser());
                    listForDisconnections.remove(u);
                });
            }
        }
    }

    static class UnusedNamesRemover extends Thread {
        @Override
        public void run() {
            while (true) {
                TmpStorage.userNames.removeIf(
                        u -> Math.abs(Duration.between(LocalDateTime.now(),
                                u.getTimeOfCreation()).getSeconds()) > 10
                );

                TmpStorage.chatRoomNames.removeIf(
                        u -> Math.abs(Duration.between(LocalDateTime.now(),
                                u.getTimeOfCreation()).getSeconds()) > 10
                );

                try {
                    sleep(20_000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}