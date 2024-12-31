package com.botchat.botchat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ChatSession {
    @Id
    private String uuid;
    private String userName;
    private int currentNodeId;
    private String userInput;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(int currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
