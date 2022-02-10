package ru.ao.simplemessenger.client.application.window.userset;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.handlers.ContinueButtonEventHandler;
import ru.ao.simplemessenger.client.handlers.LogInButtonPressedHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;
import ru.ao.simplemessenger.client.utils.SizeConverter;
import ru.ao.simplemessenger.transfer.UserStatus;

public class UsernameSetView implements DefaultUsernameSetView {
    private final static Logger log = LoggerFactory.getLogger(UsernameSetView.class.getName());

    private final static String USERNAME_STRING = "Username:";
    private final static String USERNAME_HINT = "user";
    private final static String PASSWORD_STRING = "Password:";
    private final static String PASSWORD_HINT = "password";
    private final static String LOG_IN_STRING = "Log in";
    private final static String STATUS_STRING = "Status:";
    private final static String USER_CREATED_STRING = "User created!";
    private final static String WRONG_PASSWORD_STRING = "Wrong password!";
    private final static String USER_ALREADY_ONLINE = "User online";
    private final static String ACCESS_GRANTED_STRING = "Access granted!";

    private final Scene scene;
    private final double sizeMultiplier;

    private final Button closeButton;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button logInButton;
    private final Label statusLabel;

    private double xOffset = 0;
    private double yOffset = 0;

    private LogInButtonPressedHandler logInButtonPressedHandler;
    private ContinueButtonEventHandler continueButtonEventHandler;

    public UsernameSetView(double winWidth, double winHeight, double sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;

        SizeConverter converter = new SizeConverter(winWidth * sizeMultiplier, winHeight * sizeMultiplier,
                882d, 526d);

        this.closeButton = NodeFactory.createDefaultCloseButton();
        AnchorPane.setTopAnchor(this.closeButton, converter.convertFromHeight(16d));
        AnchorPane.setLeftAnchor(this.closeButton, converter.convertFromWidth(16d));
        AnchorPane.setBottomAnchor(this.closeButton, converter.getAppHeight() - AnchorPane.getTopAnchor(this.closeButton) - 14d);
        AnchorPane.setRightAnchor(this.closeButton, converter.getAppWidth() - AnchorPane.getLeftAnchor(this.closeButton) - 14d);

        Label usernameLabel = NodeFactory.createBigLabel(USERNAME_STRING);
        AnchorManager.setAnchors(usernameLabel,
                converter.convertFromHeight(115d),
                converter.convertFromWidth(68d),
                converter.convertFromHeight(363d),
                converter.convertFromWidth(68.18)
        );

        this.usernameField = NodeFactory.createDefaultTextField(USERNAME_HINT);
        AnchorManager.setAnchors(this.usernameField,
                converter.convertFromHeight(167d),
                converter.convertFromWidth(68d),
                converter.convertFromHeight(311d),
                converter.convertFromWidth(68.18)
        );

        Label passwordLabel = NodeFactory.createBigLabel(PASSWORD_STRING);
        AnchorManager.setAnchors(passwordLabel,
                converter.convertFromHeight(252d),
                converter.convertFromWidth(68d),
                converter.convertFromHeight(226d),
                converter.convertFromWidth(68.18)
        );

        this.passwordField = NodeFactory.createDefaultPasswordField(PASSWORD_HINT);
        AnchorManager.setAnchors(this.passwordField,
                converter.convertFromHeight(304d),
                converter.convertFromWidth(68.09),
                converter.convertFromHeight(174d),
                converter.convertFromWidth(68.09)
        );

        this.logInButton = NodeFactory.createDefaultButton(LOG_IN_STRING);
        AnchorManager.setAnchors(this.logInButton,
                converter.convertFromHeight(423d),
                converter.convertFromWidth(592d),
                converter.convertFromHeight(63d),
                converter.convertFromWidth(68d)
        );

        this.logInButton.setOnAction(event -> {
            if (this.logInButtonPressedHandler != null) {
                this.logInButtonPressedHandler.handle(
                        this.usernameField.getText(),
                        this.passwordField.getText()
                );
            }
        });

        this.statusLabel = NodeFactory.createBigStatusLabel();
        AnchorManager.setAnchors(this.statusLabel,
                converter.convertFromHeight(423d),
                converter.convertFromWidth(68d),
                converter.convertFromHeight(63d),
                converter.convertFromWidth(385d)
        );

        this.setStatusLabelsVisibility(false);

        AnchorPane root = NodeFactory.createDefaultRootAnchorPane();

        root.getChildren().addAll(
                this.closeButton, usernameLabel, this.usernameField, passwordLabel, this.passwordField,
                this.logInButton, this.statusLabel
        );
        this.scene = NodeFactory.createScene(root);

        this.configureScene();

        this.logInButton.requestFocus();
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

        this.scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.logInButton.fire();
            }
        });
    }

    @Override
    public void registerLogInButtonPressedHandler(LogInButtonPressedHandler handler) {
        this.logInButtonPressedHandler = handler;
    }

    @Override
    public void setStatusLabelsVisibility(boolean isVisible) {
        this.statusLabel.setVisible(isVisible);
    }

    @Override
    public void setInputComponentsDisableState(boolean state) {
        this.logInButton.setDisable(state);

        this.usernameField.setEditable(!state);
        this.passwordField.setEditable(!state);
    }

    @Override
    public void updateStatusLabel(UserStatus status) {
        String highlightStyle = "big-status-label-highlighted";

        this.statusLabel.getStyleClass().remove(highlightStyle);

        switch (status) {
            case WRONG_PASSWORD -> {
                this.statusLabel.setText(WRONG_PASSWORD_STRING);
                this.statusLabel.getStyleClass().add(highlightStyle);
            }

            case ALREADY_ONLINE -> {
                this.statusLabel.setText(USER_ALREADY_ONLINE);
                this.statusLabel.getStyleClass().add(highlightStyle);
            }

        }
    }

    @Override
    public void highlightUsernameField(boolean isHighlighted) {
        if (isHighlighted) {
            this.usernameField.getStyleClass().add("text-field-default-highlighted");
        } else {
            this.usernameField.getStyleClass().remove("text-field-default-highlighted");
        }
    }

    @Override
    public void highlightPasswordField(boolean isHighlighted) {
        if (isHighlighted) {
            this.passwordField.getStyleClass().add("text-field-default-highlighted");
        } else {
            this.passwordField.getStyleClass().remove("text-field-default-highlighted");
        }
    }

    @Override
    public void cleanUserData() {
        this.usernameField.clear();
        this.passwordField.clear();

        this.setInputComponentsDisableState(false);
        this.setStatusLabelsVisibility(false);

        this.statusLabel.setText("");

        this.logInButton.requestFocus();
    }

    @Override
    public String getSpecifiedUsername() {
        return this.usernameField.getText();
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
        this.closeButton.setOnAction(handler);
    }

    @Override
    public void onViewRegistration() {

    }

    @Override
    public void onViewShown(boolean needsReconnect) {
        if (needsReconnect) {
            this.logInButtonPressedHandler.handle(
                    this.usernameField.getText(),
                    this.passwordField.getText()
            );

        } else {
            this.cleanUserData();
        }
    }
}
