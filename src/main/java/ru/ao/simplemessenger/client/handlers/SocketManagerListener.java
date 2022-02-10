package ru.ao.simplemessenger.client.handlers;

import ru.ao.simplemessenger.client.enums.ConnectionResult;

public interface SocketManagerListener {
    void handleConnectionResult(ConnectionResult result);

    String getListenerName();
}
