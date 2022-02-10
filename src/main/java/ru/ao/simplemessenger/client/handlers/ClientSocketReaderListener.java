package ru.ao.simplemessenger.client.handlers;

import ru.ao.simplemessenger.transfer.SocketMessageDTO;

public interface ClientSocketReaderListener {
    void handleReadString(SocketMessageDTO message);

    void handleServerClosedEvent();
}
