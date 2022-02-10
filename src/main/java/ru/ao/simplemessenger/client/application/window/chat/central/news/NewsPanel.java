package ru.ao.simplemessenger.client.application.window.chat.central.news;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class NewsPanel extends GridPane {
    private final String time;
    private final String header;
    private final String text;

    public NewsPanel(String header, String text, String time) {
        super();

        this.time = time;
        this.header = header;
        this.text = text;

        NodeFactory.decorateNewsPanel(this);

        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("news-time-label");

        this.getRowConstraints().add(new RowConstraints());

        this.setGridLinesVisible(false);

        GridPane.setHalignment(timeLabel, HPos.LEFT);

        this.add(timeLabel, 0, 0);

        Label headerLabel = NodeFactory.createBigLabel(header);

        headerLabel.getStyleClass().add("news-header");

        GridPane.setHalignment(headerLabel, HPos.CENTER);

        this.add(headerLabel, 0, 1);

        TextArea newsText = NodeFactory.createResizableTextArea(30, 10);

        newsText.appendText(text);

        GridPane.setHalignment(newsText, HPos.LEFT);

        this.add(newsText, 0, 2);

        GridPane.setHgrow(timeLabel, Priority.ALWAYS);
        GridPane.setHgrow(headerLabel, Priority.ALWAYS);
        GridPane.setHgrow(newsText, Priority.NEVER);

        GridPane.setVgrow(newsText, Priority.ALWAYS);
    }

    public String getTime() {
        return time;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }
}
