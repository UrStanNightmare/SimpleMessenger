package ru.ao.simplemessenger.client.application.window.chat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.application.window.chat.central.CentralView;
import ru.ao.simplemessenger.client.application.window.chat.central.DefaultCentralView;
import ru.ao.simplemessenger.client.application.window.chat.notification.DefaultNotificationView;
import ru.ao.simplemessenger.client.application.window.chat.notification.NotificationView;
import ru.ao.simplemessenger.client.application.window.chat.sidebar.DefaultSideBarView;
import ru.ao.simplemessenger.client.application.window.chat.sidebar.SideBarView;
import ru.ao.simplemessenger.client.handlers.ChatViewActivationHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;
import ru.ao.simplemessenger.client.utils.SizeConverter;

public class ChatView implements DefaultChatView {
    private final static Logger log = LoggerFactory.getLogger(ChatView.class.getName());

    private final Scene scene;
    private final double sizeMultiplier;
    private final DefaultSideBarView sideBarView;
    private final DefaultNotificationView notificationView;
    private final DefaultCentralView centralView;

    private double xOffset = 0;
    private double yOffset = 0;
    private ChatViewActivationHandler chatViewActivationHandler;

    public ChatView(double winWidth, double winHeight, double sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;

        SizeConverter converter = new SizeConverter(winWidth * sizeMultiplier, winHeight * sizeMultiplier,
                1440d, 1024d);


        this.sideBarView = new SideBarView();
        AnchorManager.setAnchors((AnchorPane) this.sideBarView,
                0d,
                0d,
                0d,
                converter.convertFromWidth(1199d));

        this.notificationView = new NotificationView();
        AnchorManager.setAnchors((AnchorPane) this.notificationView,
                0d,
                converter.convertFromWidth(241d),
                converter.convertFromHeight(936d),
                0d);

        this.centralView = new CentralView();
        AnchorManager.setAnchors((AnchorPane) this.centralView,
                converter.convertFromHeight(88d),
                converter.convertFromWidth(241d),
                0d,
                0d);

        AnchorPane root = NodeFactory.createDefaultRootAnchorPane();

        root.getChildren().addAll(
                (AnchorPane) this.sideBarView, (AnchorPane) this.notificationView, (AnchorPane) this.centralView
        );

        this.scene = NodeFactory.createScene(root);

        this.configureScene();
    }

    private void configureScene() {
        this.scene.setOnMousePressed(event -> {
            this.xOffset = event.getSceneX();
            this.yOffset = event.getSceneY();
        });

        this.scene.setOnMouseDragged(event -> {
            Stage stage = this.getStage();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    @Override
    public Stage getStage() {
        return (Stage) this.getScene().getWindow();
    }

    @Override
    public double getSizeMultiplier() {
        return this.sizeMultiplier;
    }

    @Override
    public void registerCloseButtonEventHandler(EventHandler<ActionEvent> handler) {

    }

    @Override
    public void onViewRegistration() {
    }

    @Override
    public void onViewShown(boolean needsReconnect) {
        if (this.chatViewActivationHandler != null) {
            this.chatViewActivationHandler.handle();
        }
    }

    @Override
    public void registerChatViewActivationHandler(ChatViewActivationHandler handler) {
        this.chatViewActivationHandler = handler;
    }

    @Override
    public void cleanChatData() {
        this.centralView.cleanActiveUserList();
        this.centralView.cleanNews();
    }

    @Override
    public DefaultSideBarView getSideBarView() {
        return this.sideBarView;
    }

    @Override
    public DefaultNotificationView getNotificationsView() {
        return this.notificationView;
    }

    @Override
    public DefaultCentralView getCentralView() {
        return this.centralView;
    }
}
