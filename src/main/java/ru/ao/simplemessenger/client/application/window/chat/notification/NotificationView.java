package ru.ao.simplemessenger.client.application.window.chat.notification;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.ToggleSwitch;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class NotificationView extends AnchorPane implements DefaultNotificationView {

    private final ToggleSwitch toggleSwitch;
    private final Button settingsButton;
    private final Button notificationsButton;
    private final Circle notificationCircle;

    public NotificationView() {
        super();

        NodeFactory.decorateNotificationsRootPane(this);

        this.toggleSwitch = NodeFactory.createEmptyToggleSwitch();
        AnchorManager.setAnchors(this.toggleSwitch,
                32d,
                null,
                32d,
                24d);

        this.toggleSwitch.setSelected(true);

        this.settingsButton = NodeFactory.createSettingsButton();
        AnchorManager.setAnchors(this.settingsButton,
                23d,
                null,
                23d,
                85d);

        this.notificationsButton = NodeFactory.createNotificationsButton();

        AnchorManager.setAnchors(this.notificationsButton,
                0d,
                0d,
                0d,
                0d);

        this.notificationCircle = new Circle(5);
        this.notificationCircle.setFill(Color.web("0078CE"));
        this.notificationCircle.setVisible(false);

        AnchorManager.setAnchors(this.notificationCircle,
                0d,
                10d,
                10d,
                0d);

        AnchorPane notificationIconPane = new AnchorPane();

        notificationIconPane.getChildren().addAll(
                this.notificationsButton, this.notificationCircle
        );
        AnchorManager.setAnchors(notificationIconPane,
                23d,
                null,
                23d,
                114d);

        this.getChildren().addAll(
                this.toggleSwitch, this.settingsButton, notificationIconPane
        );
    }

    @Override
    public void registerToggleSwitchStateListener(ChangeListener<Boolean> listener) {
        this.toggleSwitch.selectedProperty().addListener(listener);
    }

    @Override
    public void setNotificationCircleVisibility(boolean isVisible) {
        this.notificationCircle.setVisible(isVisible);
    }

    @Override
    public void addSettingsButtonEventHandler(EventType type, EventHandler handler) {
        this.settingsButton.addEventHandler(type, handler);
    }

    @Override
    public void addNotificationsButtonEventHandler(EventType type, EventHandler handler) {
        this.notificationsButton.addEventHandler(type, handler);
    }
}
