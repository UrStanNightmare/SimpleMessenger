package ru.ao.simplemessenger.client.application.window.chat.central.users;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import ru.ao.simplemessenger.client.handlers.SendDirectButtonHandler;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class UserPanel extends GridPane {
    private final String username;
    private final Button sendDirectButton;

    public UserPanel(String username) {
        super();

        this.username = username;

        Label usernameLabel = NodeFactory.createBigLabel(username);

        StackPane smallUserIcon = NodeFactory.createSmallUserIcon(username);

        this.sendDirectButton = NodeFactory.createSendDirectButton();

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(10d);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(80d);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10d);

        this.getColumnConstraints().addAll(
                col0, col1, col2
        );

        GridPane.setHalignment(smallUserIcon, HPos.CENTER);
        this.add(smallUserIcon, 0, 0);

        GridPane.setHalignment(usernameLabel, HPos.LEFT);
        this.add(usernameLabel, 1, 0);

        GridPane.setHalignment(this.sendDirectButton, HPos.CENTER);
        this.add(this.sendDirectButton, 2, 0);

        GridPane.setVgrow(smallUserIcon, Priority.NEVER);
        GridPane.setVgrow(usernameLabel, Priority.ALWAYS);
        GridPane.setVgrow(sendDirectButton, Priority.NEVER);

        GridPane.setHgrow(usernameLabel, Priority.ALWAYS);

        this.setGridLinesVisible(false);
        NodeFactory.decorateUserPanel(this);
    }

    public String getUsername() {
        return username;
    }

    public void setButtonHandler(SendDirectButtonHandler sendDirectHandler) {
        this.sendDirectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            sendDirectHandler.handle(this.username);
        });
    }
}
