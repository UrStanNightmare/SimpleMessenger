package ru.ao.simplemessenger.transfer;

import java.util.HashMap;

public class Response {


    public static class Builder {
        private MessageType messageType = null;
        private HashMap<String, String> body;
        private UserStatus userStatus;

        public Builder() {
            body = new HashMap<>();
        }

        public Builder messageType(MessageType type) {
            messageType = type;
            return this;
        }

        public Builder addBodyPart(String key, String value) {
            body.put(key, value);
            return this;
        }

        public Builder addUserStatus(UserStatus status) {
            userStatus = status;
            return this;
        }

        public SocketMessageDTO build() {
            if (userStatus != null) {
                return new SocketMessageDTO(messageType, body, userStatus);
            } else {
                return new SocketMessageDTO(messageType, body);
            }
        }

        public SocketMessageDTO buildCheckUserMessage(String username, String password) {
            return this.messageType(MessageType.CU)
                    .addBodyPart(MessageStringHolder.USERNAME_KEY, username)
                    .addBodyPart(MessageStringHolder.PASSWORD_KEY, password)
                    .build();
        }

        public SocketMessageDTO buildGetActiveUserListMessage() {
            return new SocketMessageDTO(MessageType.GCU, null);
        }

        public SocketMessageDTO buildDisconnectMessageWithoutMeta() {
            return new SocketMessageDTO(MessageType.D, null);
        }

        public SocketMessageDTO buildChatMessageWithoutMeta(String messageString) {
            return this.messageType(MessageType.MES)
                    .addBodyPart(MessageStringHolder.MESSAGE_KEY, messageString)
                    .build();
        }

        public SocketMessageDTO buildStatusMessage(UserStatus userStatus) {
            return this.messageType(MessageType.US)
                    .addUserStatus(userStatus)
                    .build();
        }

        public SocketMessageDTO buildSendActiveUserListMessage(String users) {
            return this.messageType(MessageType.GCU)
                    .addBodyPart(MessageStringHolder.USERS_ONLINE_KEY, users)
                    .build();
        }

        public SocketMessageDTO buildChatMessageWithMeta(String message, String username, String time) {
            return this.messageType(MessageType.MES)
                    .addBodyPart(MessageStringHolder.MESSAGE_KEY, message)
                    .addBodyPart(MessageStringHolder.USERNAME_KEY, username)
                    .addBodyPart(MessageStringHolder.TIME_KEY, time)
                    .build();
        }

    }
}
