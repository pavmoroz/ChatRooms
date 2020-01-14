package model;

import java.time.LocalDateTime;

public class UnusedName {
    private String name;
    private LocalDateTime timeOfCreation;

    public UnusedName(String name) {
        this.name = name;
        timeOfCreation = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }
}
