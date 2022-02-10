package ru.ao.simplemessenger.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public class SocketMessageDTO {
    private MessageType type;
    private HashMap<String, String> body;
    private UserStatus userStatus;

    public SocketMessageDTO(MessageType type, HashMap<String, String> body) {
        this.type = type;
        this.body = body;
    }

    public SocketMessageDTO(MessageType type, HashMap<String, String> body, UserStatus userStatus) {
        this.type = type;
        this.body = body;
        this.userStatus = userStatus;
    }

    public SocketMessageDTO() {
    }

    public MessageType getType() {
        return this.type;
    }

    public HashMap<String, String> getBody() {
        return this.body;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setBody(HashMap<String, String> body) {
        this.body = body;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @JsonIgnore
    public void specifyUsername(String username) {
        this.body.put(MessageStringHolder.USERNAME_KEY, username);
    }

    @JsonIgnore
    public String getSentUsername() {
        return this.body.get(MessageStringHolder.USERNAME_KEY);
    }

    @JsonIgnore
    public void specifySelfStatus(String status) {
        this.body.put(MessageStringHolder.IS_SELF_KEY, status);
    }

    @JsonIgnore
    public void specifyDate(String date) {
        if (this.body == null) {
            this.body = new HashMap<>();
        }
        this.body.put(MessageStringHolder.DATE_KEY, date);
    }

}
