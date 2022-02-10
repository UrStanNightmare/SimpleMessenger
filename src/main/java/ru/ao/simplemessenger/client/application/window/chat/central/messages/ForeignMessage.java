package ru.ao.simplemessenger.client.application.window.chat.central.messages;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class ForeignMessage extends GridPane implements DefaultMessage {
    private final String username;
    private final String text;
    private final String time;

    public ForeignMessage(String username, String text, String time) {
        super();

        this.username = username;
        this.text = text;
        this.time = time;

        NodeFactory.decorateForeignMessage(this);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(70);

        this.getColumnConstraints().addAll(col1, col2);

        this.getRowConstraints().addAll(row1, row2);

        this.setVgap(15);

        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("foreign-message-time-label");

        GridPane.setHalignment(timeLabel, HPos.RIGHT);

        this.add(timeLabel, 1, 0);

        Label headerLabel = new Label(username);

        headerLabel.getStyleClass().add("message-username-label");

        GridPane.setHalignment(headerLabel, HPos.LEFT);

        this.add(headerLabel, 0, 0);

        TextArea messageText = NodeFactory.createResizableTextArea(30, 10);

        messageText.appendText(text);

        GridPane.setHalignment(messageText, HPos.CENTER);
        GridPane.setColumnSpan(messageText, 2);

        this.add(messageText, 0, 1);

        GridPane.setHgrow(timeLabel, Priority.ALWAYS);
        GridPane.setHgrow(headerLabel, Priority.ALWAYS);
        GridPane.setHgrow(messageText, Priority.NEVER);

        GridPane.setVgrow(messageText, Priority.ALWAYS);
    }
}
