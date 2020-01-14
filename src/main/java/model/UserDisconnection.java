package model;

import java.time.LocalDateTime;

public class UserDisconnection {
    private User user;
    private LocalDateTime timeOfDisconnection;

    public UserDisconnection(User user) {
        this.user = user;
        timeOfDisconnection = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getTimeOfDisconnection() {
        return timeOfDisconnection;
    }
}
