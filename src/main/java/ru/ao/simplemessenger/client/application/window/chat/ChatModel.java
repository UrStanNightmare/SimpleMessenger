package ru.ao.simplemessenger.client.application.window.chat;

import ru.ao.simplemessenger.client.socket.DefaultSocketManager;
import ru.ao.simplemessenger.transfer.Response;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

public class ChatModel implements DefaultChatModel {
    private final DefaultSocketManager socketManager;

    public ChatModel(DefaultSocketManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void closeServerConnection() {
        this.socketManager.closeConnection();
    }

    @Override
    public void sendGetActiveUsersMessageToServer() {
        SocketMessageDTO message = new Response.Builder()
                .buildGetActiveUserListMessage();

        this.socketManager.sendMessageToServer(message);
    }

    @Override
    public SocketMessageDTO getMessageFromQueue() {
        return this.socketManager.getMessageFromQueue();
    }

    @Override
    public boolean getIsConnectionFailed() {
        return this.socketManager.getIsConnectionFailed();
    }

    @Override
    public void sendMessageToServer(SocketMessageDTO message) {
        this.socketManager.sendMessageToServer(message);
    }

    @Override
    public String getActiveUsername() {
        return this.socketManager.getBoundUsername();
    }
}
