package ru.ao.simplemessenger.client.application.window.chat.central.messages;

import ru.ao.simplemessenger.client.handlers.SendMessageButtonPressedHandler;

public interface DefaultMessageView{
    void placeMessage(boolean isSelf, String username, String text, String time);

    void removeMessage(int position);

    void registerSendButtonClickedHandler(SendMessageButtonPressedHandler handler);
}
