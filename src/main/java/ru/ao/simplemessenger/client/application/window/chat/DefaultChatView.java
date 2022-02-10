package ru.ao.simplemessenger.client.application.window.chat;

import ru.ao.simplemessenger.client.application.window.chat.central.DefaultCentralView;
import ru.ao.simplemessenger.client.application.window.chat.notification.DefaultNotificationView;
import ru.ao.simplemessenger.client.application.window.chat.sidebar.DefaultSideBarView;
import ru.ao.simplemessenger.client.defaults.DefaultView;
import ru.ao.simplemessenger.client.handlers.ChatViewActivationHandler;

public interface DefaultChatView extends DefaultView {

    void registerChatViewActivationHandler(ChatViewActivationHandler handler);


    void cleanChatData();

    DefaultSideBarView getSideBarView();

    DefaultNotificationView getNotificationsView();

    DefaultCentralView getCentralView();
}
