package model;

import websocket.MyServerEndpoint;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {

    private String name;
    private List<String> chatStory;
    private ConcurrentHashMap<User, MyServerEndpoint> usersEndpoints;

    public ChatRoom(String name) {
        this.name = name;
        chatStory = new CopyOnWriteArrayList<>();
        usersEndpoints = new ConcurrentHashMap<>();
    }

    public void addToChatStory(String line) {
        chatStory.add(0, line + "\n");
        if (chatStory.size() == 21)
            chatStory.remove(20);
    }

    public ConcurrentHashMap<User, MyServerEndpoint> getUsersEndpoints() {
        return usersEndpoints;
    }

    public List<String> getChatStory() {
        return chatStory;
    }

    public String getName() {
        return name;
    }

    public Optional<User> getUser(String username) {
        return usersEndpoints.keySet().stream()
                .filter(e -> username.equalsIgnoreCase(e.getName()))
                .findFirst();
    }
}