package ru.ao.simplemessenger.server.socket;

import ru.ao.simplemessenger.transfer.UserStatus;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface DefaultServerSocketManager {
    void startServer();

    void closeServer();

    ConcurrentLinkedQueue<ChatUserSocketManager> getClientsList();

    String getConnectedClientsUsernames();

    boolean checkIfUserOnline(String username);

    UserStatus getUserConnectionStatus(String username, String password);
}
