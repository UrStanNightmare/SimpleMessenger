package ru.ao.simplemessenger.client.application.window.chat.central;

import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.application.window.chat.central.messages.DefaultMessageView;
import ru.ao.simplemessenger.client.application.window.chat.central.messages.MessageView;
import ru.ao.simplemessenger.client.application.window.chat.central.news.DefaultNewsView;
import ru.ao.simplemessenger.client.application.window.chat.central.news.NewsView;
import ru.ao.simplemessenger.client.application.window.chat.central.users.DefaultUsersView;
import ru.ao.simplemessenger.client.application.window.chat.central.users.UsersView;
import ru.ao.simplemessenger.client.enums.MenuTabs;
import ru.ao.simplemessenger.client.handlers.SendDirectButtonHandler;
import ru.ao.simplemessenger.client.handlers.SendMessageButtonPressedHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class CentralView extends AnchorPane implements DefaultCentralView {
    private final Logger log = LoggerFactory.getLogger(CentralView.class.getName());

    private final DefaultNewsView newsView;
    private final DefaultMessageView messageView;
    private final DefaultUsersView usersView;

    private MenuTabs currentTab;

    public CentralView() {
        super();

        this.newsView = new NewsView();
        AnchorManager.setAnchors((AnchorPane) this.newsView,
                0d,
                0d,
                0d,
                0d);

        this.messageView = new MessageView();
        AnchorManager.setAnchors((AnchorPane) this.messageView,
                0d,
                0d,
                0d,
                0d);

        this.usersView = new UsersView();
        AnchorManager.setAnchors((AnchorPane) this.usersView,
                0d,
                0d,
                0d,
                0d);

        this.getChildren().addAll(
                (AnchorPane) this.newsView
        );
        NodeFactory.decorateCentralRootPanel(this);
    }

    @Override
    public void registerSendDirectHandler(SendDirectButtonHandler handler) {
        this.usersView.registerSendDirectButtonHandler(handler);
    }

    @Override
    public void switchTo(MenuTabs tab) {
        this.currentTab = tab;

        switch (tab) {
            case NEWS -> {
                this.getChildren().clear();
                this.getChildren().add((NewsView) this.newsView);
            }
            case CHAT -> {
                this.getChildren().clear();
                this.getChildren().add((MessageView) this.messageView);
            }

            case USERS -> {
                this.getChildren().clear();
                this.getChildren().add((UsersView) this.usersView);
            }
        }
    }

    @Override
    public void addActiveUser(String username) {
        this.usersView.addActiveUser(username);
    }

    @Override
    public void removeActiveUser(String username) {
        this.usersView.removeActiveUser(username);
    }

    @Override
    public void cleanActiveUserList() {
        this.usersView.cleanUsers();
    }

    @Override
    public void addNewsPost(String label, String text, String time) {
        this.newsView.addNews(label, text, time);
    }

    @Override
    public void placeMessage(boolean isSelf, String username, String text, String time) {
        this.messageView.placeMessage(isSelf, username, text, time);
    }

    @Override
    public void registerSendUserMessageHandler(SendMessageButtonPressedHandler handler) {
        this.messageView.registerSendButtonClickedHandler(handler);
    }

    @Override
    public void cleanNews() {
        this.newsView.cleanNews();
    }
}
