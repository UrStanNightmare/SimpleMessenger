package ru.ao.simplemessenger.client.application.window.userset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.defaults.DefaultSceneController;
import ru.ao.simplemessenger.client.enums.SceneName;
import ru.ao.simplemessenger.transfer.MessageType;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;
import ru.ao.simplemessenger.transfer.UserStatus;

public class UsernameSetPresenter implements DefaultUsernameSetPresenter {
    private final static Logger log = LoggerFactory.getLogger(UsernameSetPresenter.class.getName());

    private final DefaultUsernameSetModel model;
    private final DefaultUsernameSetView view;
    private final DefaultSceneController sceneController;

    public UsernameSetPresenter(DefaultUsernameSetModel usernameSetModel, DefaultUsernameSetView usernameSetView, DefaultSceneController sceneController) {
        this.model = usernameSetModel;
        this.view = usernameSetView;
        this.sceneController = sceneController;

        this.registerViewHandlers();
    }

    private void registerViewHandlers() {

        this.view.registerCloseButtonEventHandler(event -> {
            this.model.closeServerConnection();
            this.view.getStage().close();
        });

        this.view.registerLogInButtonPressedHandler((username, password) -> {
            boolean isIncorrect = this.checkInputData(username, password);
            if (isIncorrect) {
                return;
            }

            this.view.setInputComponentsDisableState(true);

            boolean isSent = this.model.checkIfUsernameExists(username, password);

            if (!isSent) {
                log.warn("Can't check user existence!");

                this.sceneController.setNeedsReconnectState(true);
                this.sceneController.switchTo(SceneName.SERVER_CHOSE);
                return;
            }
            SocketMessageDTO response;
            MessageType type = null;
            UserStatus status = null;

            //В данном состоянии должен прийти всего один ответ от сервера
            do {
                response = this.model.getMessageFromQueue();
                if (response != null) {
                    type = response.getType();
                    status = response.getUserStatus();
                }

            } while (type != MessageType.US);


            this.manageStatus(status);
        });
    }

    private boolean checkInputData(String username, String password) {
        boolean isIncorrect = false;

        if (username == null || username.isBlank()) {
            this.view.highlightUsernameField(true);
            isIncorrect = true;
        }

        if (password == null || password.isBlank()) {
            this.view.highlightPasswordField(true);
            isIncorrect = true;
        }

        if (!isIncorrect) {
            this.view.highlightUsernameField(false);
            this.view.highlightPasswordField(false);
        }
        return isIncorrect;
    }

    private void manageStatus(UserStatus status) {
        switch (status) {
            case CREATED -> {
                log.info("New user will be created!");
                this.confirmAuthorisation();
            }
            case ACCESS_GRANTED -> {
                log.info("Access granted.");
                this.confirmAuthorisation();
            }
            case WRONG_PASSWORD -> {
                log.warn("Wrong password specified.");
                this.view.setStatusLabelsVisibility(true);
                this.view.updateStatusLabel(UserStatus.WRONG_PASSWORD);
                this.view.highlightPasswordField(true);
                this.view.setInputComponentsDisableState(false);
            }

            case ALREADY_ONLINE -> {
                log.warn("User already online");
                this.view.setStatusLabelsVisibility(true);
                this.view.updateStatusLabel(UserStatus.ALREADY_ONLINE);
                this.view.highlightUsernameField(true);
                this.view.highlightPasswordField(true);
                this.view.setInputComponentsDisableState(false);
            }

        }
    }

    private void confirmAuthorisation() {
        String username = this.view.getSpecifiedUsername();

        this.model.bindUsernameToSocketManager(username);

        this.sceneController.setNeedsReconnectState(true);
        this.sceneController.switchTo(SceneName.CHAT_WINDOW);
    }
}
