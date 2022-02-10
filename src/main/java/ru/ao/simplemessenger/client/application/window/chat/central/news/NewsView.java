package ru.ao.simplemessenger.client.application.window.chat.central.news;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class NewsView extends AnchorPane implements DefaultNewsView {
    private final String NEWS_LABEL = "News";

    private final ObservableList<NewsPanel> newsList = FXCollections.observableArrayList();

    public NewsView() {
        super();

        Label newsPanelLabel = NodeFactory.createBigLabel(NEWS_LABEL);
        AnchorManager.setAnchors(newsPanelLabel,
                0d,
                0d,
                null,
                null);

        ListView<NewsPanel> newsListView = NodeFactory.createDefaultListView(newsList);


        AnchorManager.setAnchors(newsListView,
                50d,
                5d,
                5d,
                5d);


        this.getChildren().addAll(
                newsPanelLabel, newsListView
        );

        NodeFactory.decorateNewsView(this);
    }

    @Override
    public void addNews(String label, String text, String time) {
        this.newsList.add(new NewsPanel(label, text, time));
    }

    @Override
    public void removeNews(String label) {
        NewsPanel toRemove = null;

        for (NewsPanel panel : this.newsList) {
            if (panel.getHeader().equals(label)) {
                toRemove = panel;
                break;
            }
        }

        if (toRemove != null) {
            this.newsList.remove(toRemove);
        }

    }

    @Override
    public void removeNews(int position) {
        this.newsList.remove(position);
    }

    @Override
    public void cleanNews() {
        this.newsList.clear();
    }
}
