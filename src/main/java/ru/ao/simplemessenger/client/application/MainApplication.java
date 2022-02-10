package ru.ao.simplemessenger.client.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.application.window.chat.ChatModel;
import ru.ao.simplemessenger.client.application.window.chat.ChatPresenter;
import ru.ao.simplemessenger.client.application.window.chat.ChatView;
import ru.ao.simplemessenger.client.application.window.serverset.ServerChooseModel;
import ru.ao.simplemessenger.client.application.window.serverset.ServerChoosePresenter;
import ru.ao.simplemessenger.client.application.window.serverset.ServerChooseView;
import ru.ao.simplemessenger.client.application.window.userset.UsernameSetModel;
import ru.ao.simplemessenger.client.application.window.userset.UsernameSetPresenter;
import ru.ao.simplemessenger.client.application.window.userset.UsernameSetView;
import ru.ao.simplemessenger.client.enums.AppTheme;
import ru.ao.simplemessenger.client.enums.SceneName;
import ru.ao.simplemessenger.client.socket.ClientSocketManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class MainApplication extends Application {
    private final static Logger log = LoggerFactory.getLogger(MainApplication.class.getName());

    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    @Override
    public void start(Stage primaryStage) {
        NodeFactory.setTheme(AppTheme.LIGHT);

        SceneController sceneController = new SceneController(primaryStage, WINDOW_WIDTH, WINDOW_HEIGHT);

        ClientSocketManager socketManager = new ClientSocketManager();

        ServerChooseView serverChooseView = new ServerChooseView(WINDOW_WIDTH, WINDOW_HEIGHT, 0.5d);

        ServerChoosePresenter serverChoosePresenter = new ServerChoosePresenter(new ServerChooseModel(socketManager), serverChooseView, sceneController);

        UsernameSetView usernameSetView = new UsernameSetView(WINDOW_WIDTH, WINDOW_HEIGHT, 0.5d);

        UsernameSetPresenter usernameSetPresenter = new UsernameSetPresenter(new UsernameSetModel(socketManager), usernameSetView, sceneController);

        ChatView chatView = new ChatView(WINDOW_WIDTH, WINDOW_HEIGHT, 1d);

        ChatPresenter chatPresenter = new ChatPresenter(new ChatModel(socketManager), chatView, sceneController);

        socketManager.addConnectionStateListener(serverChoosePresenter);

        sceneController.registerView(SceneName.SERVER_CHOSE, serverChooseView, 0.5);
        sceneController.registerView(SceneName.USERNAME_CHOSE, usernameSetView, 0.5);
        sceneController.registerView(SceneName.CHAT_WINDOW, chatView, 1d);

        sceneController.switchTo(SceneName.SERVER_CHOSE);
    }

    @Override
    public void stop() throws Exception {
        log.info("Application closed.");
        super.stop();
    }

    public void startApplication() {
        launch();
    }
}
