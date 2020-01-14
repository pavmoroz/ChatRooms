package model;

import java.util.Objects;

public class User {
    private String name;
    private String chatRoomName;

    public User(String name, String chatRoomName) {
        this.name = name;
        this.chatRoomName = chatRoomName;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equalsIgnoreCase(user.name);

    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}