package ru.ao.simplemessenger.client.application.window.chat.central.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import ru.ao.simplemessenger.client.handlers.SendDirectButtonHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class UsersView extends AnchorPane implements DefaultUsersView {
    private final String USERS_STRING = "Users";
    private final ObservableList<UserPanel> usersList;

    private SendDirectButtonHandler sendDirectHandler;

    public UsersView() {
        super();

        this.usersList = FXCollections.observableArrayList();

        NodeFactory.decorateUsersRootPane(this);

        Label usersPaneLabel = NodeFactory.createBigLabel(USERS_STRING);
        AnchorManager.setAnchors(usersPaneLabel,
                0d,
                0d,
                null,
                null);

        ListView<UserPanel> usersListView = NodeFactory.createDefaultListView(usersList);

        AnchorManager.setAnchors(usersListView,
                50d,
                5d,
                5d,
                5d);

        this.getChildren().addAll(
                usersPaneLabel, usersListView
        );
    }

    @Override
    public void registerSendDirectButtonHandler(SendDirectButtonHandler handler) {
        this.sendDirectHandler = handler;
    }

    @Override
    public void addActiveUser(String username) throws NullPointerException {
        if (this.sendDirectHandler == null) {
            throw new NullPointerException("Active user handler is not specified!");
        }
        UserPanel activeUser = new UserPanel(username);
        activeUser.setButtonHandler(this.sendDirectHandler);

        this.usersList.add(activeUser);
    }

    @Override
    public void cleanUsers() {
        this.usersList.clear();
    }

    @Override
    public void removeActiveUser(String username) {
        UserPanel deleteUser = null;

        for (UserPanel userPanel : usersList) {
            if (userPanel.getUsername().equals(username)) {
                deleteUser = userPanel;
                break;
            }
        }

        if (deleteUser != null) {
            this.usersList.remove(deleteUser);
        }
    }
}
