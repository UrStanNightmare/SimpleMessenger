package ru.ao.simplemessenger.client.application.window.serverset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.defaults.DefaultSceneController;
import ru.ao.simplemessenger.client.enums.ConnectionResult;
import ru.ao.simplemessenger.client.enums.SceneName;
import ru.ao.simplemessenger.client.handlers.SocketManagerListener;

public class ServerChoosePresenter implements DefaultServerChoosePresenter, SocketManagerListener {
    private final static Logger log = LoggerFactory.getLogger(ServerChoosePresenter.class.getName());

    private final DefaultServerChooseModel model;
    private final DefaultServerChooseView view;
    private final DefaultSceneController sceneController;

    public ServerChoosePresenter(DefaultServerChooseModel model, DefaultServerChooseView view, DefaultSceneController sceneController) {
        this.model = model;
        this.view = view;
        this.sceneController = sceneController;

        this.registerViewHandlers();
    }

    private void registerViewHandlers() {
        this.view.registerCloseButtonEventHandler(event -> {
            this.model.closeServerConnection();

            this.view.getStage().close();
        });

        this.view.registerConnectButtonEventHandler((ip, port) -> {
            if (ip.isBlank() || port.isBlank()) {
                if (ip.isBlank()) {
                    this.view.highlightIpField(true);
                }
                if (port.isBlank()) {
                    this.view.highlightPortField(true);
                }
                return;
            }
            int localPort;
            try {
                localPort = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                log.warn("Specified port is not a number!");
                this.view.highlightPortField(true);
                return;
            }
            this.view.setProgressIndicatorVisibility(true);

            this.view.highlightIpField(false);
            this.view.highlightPortField(false);

            this.model.updateServerInfo(ip, localPort);

            this.model.tryToConnectToServer();
        });
    }

    @Override
    public void handleConnectionResult(ConnectionResult result) {
        this.view.setProgressIndicatorVisibility(false);
        switch (result) {
            case NOT_CONNECTED -> {
                this.view.highlightIpField(true);
                this.view.highlightPortField(true);
            }
            case CONNECTED -> {
                this.sceneController.switchTo(SceneName.USERNAME_CHOSE);
            }
        }
    }

    @Override
    public String getListenerName() {
        return SceneName.SERVER_CHOSE.toString();
    }
}
