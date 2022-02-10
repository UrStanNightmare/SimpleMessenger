package ru.ao.simplemessenger.client.application.window.chat.notification;


import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface DefaultNotificationView {
    void registerToggleSwitchStateListener(ChangeListener<Boolean> listener);

    void setNotificationCircleVisibility(boolean isVisible);

    void addSettingsButtonEventHandler(EventType type, EventHandler handler);

    void addNotificationsButtonEventHandler(EventType type, EventHandler handler);
}
