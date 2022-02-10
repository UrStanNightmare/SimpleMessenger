package ru.ao.simplemessenger.client.application.window.userset;

import ru.ao.simplemessenger.client.defaults.DefaultModel;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

public interface DefaultUsernameSetModel extends DefaultModel {
    boolean checkIfUsernameExists(String username, String password);

    SocketMessageDTO getMessageFromQueue();

    void bindUsernameToSocketManager(String username);
}
