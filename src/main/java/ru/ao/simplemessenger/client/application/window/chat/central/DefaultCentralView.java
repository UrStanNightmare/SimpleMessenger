package ru.ao.simplemessenger.client.application.window.chat.central;

import ru.ao.simplemessenger.client.enums.MenuTabs;
import ru.ao.simplemessenger.client.handlers.SendDirectButtonHandler;
import ru.ao.simplemessenger.client.handlers.SendMessageButtonPressedHandler;

public interface DefaultCentralView {
    void registerSendDirectHandler(SendDirectButtonHandler handler);

    void switchTo(MenuTabs tab);

    void addActiveUser(String username);

    void removeActiveUser(String username);

    void cleanActiveUserList();

    void addNewsPost(String label, String text, String time);

    void placeMessage(boolean isSelf, String username, String text, String time);

    void registerSendUserMessageHandler(SendMessageButtonPressedHandler handler);

    void cleanNews();
}
