package ru.ao.simplemessenger.client.application.window.chat;

import ru.ao.simplemessenger.client.defaults.DefaultModel;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

public interface DefaultChatModel extends DefaultModel {
    void sendGetActiveUsersMessageToServer();

    SocketMessageDTO getMessageFromQueue();

    boolean getIsConnectionFailed();

    void sendMessageToServer(SocketMessageDTO message);

    String getActiveUsername();
}
