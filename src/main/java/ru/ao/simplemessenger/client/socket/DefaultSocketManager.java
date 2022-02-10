package ru.ao.simplemessenger.client.socket;

import ru.ao.simplemessenger.client.enums.ConnectionResult;
import ru.ao.simplemessenger.client.handlers.SocketManagerListener;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

public interface DefaultSocketManager {
    void setServerInfo(String ip, int port);

    void tryToConnect();

    void closeConnection();

    void addConnectionStateListener(SocketManagerListener listener);

    void removeConnectionStateListener(String name);

    void sendConnectionResultToListeners(ConnectionResult result);

    boolean sendMessageToServer(SocketMessageDTO message);

    boolean performUserExistenceCheck(String username, String password);

    SocketMessageDTO getMessageFromQueue();

    void bindUsername(String username);

    String getBoundUsername();

    void unbindUsername();

    boolean getIsConnectionFailed();
}
