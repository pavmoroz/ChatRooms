package model;

import java.util.List;

public class Message {
    private String from;
    private String chatRoomName;
    private String content;
    private MessageType messageType;
    private List<String> activeUsers;
    private List<String> chatStory;

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<String> getChatStory() {
        return chatStory;
    }

    public void setChatStory(List<String> chatStory) {
        this.chatStory = chatStory;
    }

}