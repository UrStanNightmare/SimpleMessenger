package ru.ao.simplemessenger.client.application.window.chat.sidebar;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ru.ao.simplemessenger.client.enums.MenuTabs;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;


public class SideBarView extends AnchorPane implements DefaultSideBarView {

    private final Label usernameLabel;
    private final ListView<MenuTabs> listMenu;

    public SideBarView() {
        super();
        NodeFactory.decorateNavRootPanel(this);

        AnchorPane menuContainer = NodeFactory.createDefaultAnchorContainer();
        AnchorManager.setAnchors(menuContainer,
                32d,
                32d,
                null,
                7d);

        Label menuLabel = NodeFactory.createSmallLabel("MENU");
        AnchorManager.setAnchors(menuLabel,
                0d,
                40d,
                null,
                null);

        ObservableList<MenuTabs> tabs = FXCollections.observableArrayList(
                MenuTabs.values()
        );

        this.listMenu = NodeFactory.createDefaultSideBarMenu(tabs);
        AnchorManager.setAnchors(listMenu,
                40d,
                5d,
                0d,
                5d);

        this.listMenu.getSelectionModel().select(0);

        menuContainer.getChildren().addAll(
                menuLabel, this.listMenu
        );


        AnchorPane userContainer = NodeFactory.createDefaultAnchorContainer();
        AnchorManager.setAnchors(userContainer,
                null,
                10d,
                0d,
                7d);

        StackPane userIcon = NodeFactory.createCurrentUserIcon();
        AnchorManager.setAnchors(userIcon,
                0d,
                40d,
                55d,
                40d);

        this.usernameLabel = NodeFactory.createUsernameLabel();
        AnchorManager.setAnchors(this.usernameLabel,
                null,
                0d,
                20d,
                0d);

        userContainer.getChildren().addAll(
                userIcon, this.usernameLabel
        );

        this.getChildren().addAll(
                menuContainer, userContainer
        );

    }

    @Override
    public void setUsername(String username) {
        this.usernameLabel.setText(username);
    }

    @Override
    public String getUsername() {
        return this.usernameLabel.getText();
    }

    @Override
    public void registerMenuSelectionListener(ChangeListener<MenuTabs> listener) {
        this.listMenu.getSelectionModel().selectedItemProperty().addListener(listener);
    }
}
