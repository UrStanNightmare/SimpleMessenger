package ru.ao.simplemessenger.client.application.window.chat.central.users;

import ru.ao.simplemessenger.client.handlers.SendDirectButtonHandler;

public interface DefaultUsersView {
    void registerSendDirectButtonHandler(SendDirectButtonHandler handler);

    void addActiveUser(String username);

    void cleanUsers();

    void removeActiveUser(String username);
}
