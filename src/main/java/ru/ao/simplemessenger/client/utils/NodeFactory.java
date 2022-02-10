package ru.ao.simplemessenger.client.utils;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.ToggleSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.enums.AppTheme;
import ru.ao.simplemessenger.client.enums.MenuTabs;

public class NodeFactory {
    private final static Logger log = LoggerFactory.getLogger(NodeFactory.class.getName());

    private final static String ICONS_FOLDER = "icons/";

    private static final String CROSS_IMAGE_PATH = ICONS_FOLDER + "cross.png";
    private static final String USER_ICON_PATH = ICONS_FOLDER + "user.png";
    private static final String SETTINGS_ICON_PATH = ICONS_FOLDER + "settings.png";
    private static final String SETTINGS_ICON_HOVER_PATH = ICONS_FOLDER + "settings_hover.png";
    private static final String NOTIFICATION_ICON_PATH = ICONS_FOLDER + "notification.png";
    private static final String NOTIFICATION_ICON_HOVER_PATH = ICONS_FOLDER + "notification_hover.png";
    private static final String SEND_ICON_PATH = ICONS_FOLDER + "send.png";
    private static final String SEND_ICON_HOVER_PATH = ICONS_FOLDER + "send_hover.png";
    private static final String SEND_USER_MESSAGE_ICON_PATH = ICONS_FOLDER + "sendMessage.png";
    private static final String SEND_USER_MESSAGE_ICON_HOVER_PATH = ICONS_FOLDER + "sendMessage_hover.png";

    private static String themePath;

    private NodeFactory() {
    }

    public static void setTheme(AppTheme theme) {
        themePath = theme.getThemePath();
    }

    private static String getImagePathFromUsername(String username) {
        //Add icon generation
        return USER_ICON_PATH;
    }

    public static Scene createScene(Parent root) {
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        return scene;
    }

    public static AnchorPane createDefaultRootAnchorPane() {
        AnchorPane pane = new AnchorPane();

        pane.getStyleClass().add("root-panel");
        pane.getStylesheets().add(themePath);

        return pane;
    }

    public static AnchorPane createDefaultAnchorContainer() {
        AnchorPane pane = new AnchorPane();

        pane.getStyleClass().add("container-panel");
        pane.getStylesheets().add(themePath);

        return pane;
    }

    public static Label createBigLabel(String labelText) {
        Label label = new Label();

        label.setText(labelText);

        label.getStyleClass().add("big-label");
        label.getStylesheets().add(themePath);

        return label;
    }

    public static Label createBigStatusLabel() {
        Label label = new Label();

        label.getStyleClass().add("big-status-label");
        label.getStylesheets().add(themePath);

        return label;
    }

    public static Label createSmallLabel(String labelText) {
        Label label = new Label();

        label.setText(labelText);

        label.getStyleClass().add("small-label");
        label.getStylesheets().add(themePath);

        return label;
    }

    public static Label createUsernameLabel() {
        Label label = new Label();

        label.getStyleClass().add("username-label");
        label.getStylesheets().add(themePath);

        return label;
    }

    public static TextField createDefaultTextField(String hintText) {
        TextField field = new TextField();

        field.setPromptText(hintText);

        field.getStyleClass().add("text-field-default");
        field.getStylesheets().add(themePath);

        return field;
    }

    public static PasswordField createDefaultPasswordField(String passwordHint) {
        PasswordField field = new PasswordField();

        field.setPromptText(passwordHint);

        field.getStyleClass().add("text-field-default");
        field.getStylesheets().add(themePath);

        return field;
    }

    public static TextArea createResizableTextArea(int initialValue, int rowHeight) {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setStyle("-fx-text-fill: #324552");

        SimpleIntegerProperty count = new SimpleIntegerProperty(initialValue);

        textArea.prefHeightProperty().bindBidirectional(count);
        textArea.minHeightProperty().bindBidirectional(count);
        textArea.scrollTopProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > rowHeight) {
                count.setValue(count.get() + newValue.intValue());
            }
        }));

        textArea.setOnScrollStarted(event -> event.consume());

        return textArea;
    }

    public static TextArea createTransparentTextArea(String hint) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        textArea.setPromptText(hint);

        textArea.getStyleClass().add("transparent-text-area");
        textArea.getStylesheets().add(themePath);

        return textArea;
    }

    public static Button createDefaultButton(String buttonText) {
        Button button = new Button(buttonText);

        button.getStyleClass().add("default-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    public static Button createDefaultCloseButton() {
        Button button = new Button();

        button.getStyleClass().add("close-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    public static Button createSettingsButton() {
        Button button = NodeFactory.createButtonWithImageAndHover(SETTINGS_ICON_PATH, SETTINGS_ICON_HOVER_PATH);

        button.getStyleClass().add("settings-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    public static Button createNotificationsButton() {
        Button button = NodeFactory.createButtonWithImageAndHover(NOTIFICATION_ICON_PATH, NOTIFICATION_ICON_HOVER_PATH);

        button.getStyleClass().add("notifications-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    public static Button createSendDirectButton() {
        Button button = NodeFactory.createButtonWithImageAndHover(SEND_ICON_PATH, SEND_ICON_HOVER_PATH);

        button.getStyleClass().add("send-direct-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    public static Button createSendUserMessageButton() {
        Button button = NodeFactory.createButtonWithImageAndHover(SEND_USER_MESSAGE_ICON_PATH, SEND_USER_MESSAGE_ICON_HOVER_PATH);

        button.getStyleClass().add("send-user-message-button");
        button.getStylesheets().add(themePath);

        return button;
    }

    private static Button createButtonWithImage(String imagePath) {
        Image imageIcon = new Image(imagePath);

        ImageView imageView = new ImageView(imageIcon);

        imageView.setPreserveRatio(true);

        Button button = new Button();

        button.setGraphic(imageView);

        imageView.fitWidthProperty().bind(button.widthProperty());
        imageView.fitHeightProperty().bind(button.heightProperty());


        return button;
    }

    private static Button createButtonWithImageAndHover(String imagePath, String hoverImagePath) {
        Image imageIcon = new Image(imagePath);

        ImageView imageView = new ImageView(imageIcon);

        imageView.setPreserveRatio(true);

        Button button = new Button();

        button.setGraphic(imageView);

        imageView.fitWidthProperty().bind(button.widthProperty());
        imageView.fitHeightProperty().bind(button.heightProperty());

        Image hoverIcon = new Image(hoverImagePath);

        ImageView imageHoverView = new ImageView(hoverIcon);

        imageHoverView.setPreserveRatio(true);

        imageHoverView.setPreserveRatio(true);

        imageHoverView.fitWidthProperty().bind(button.widthProperty());
        imageHoverView.fitHeightProperty().bind(button.heightProperty());

        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setGraphic(imageHoverView);
            } else {
                button.setGraphic(imageView);
            }
        });


        return button;
    }

    public static ProgressIndicator createDefaultProgressIndicator() {
        ProgressIndicator indicator = new ProgressIndicator();

        indicator.setVisible(false);

        return indicator;
    }

    public static ScrollPane createDefaultScrollPane() {
        ScrollPane pane = new ScrollPane();

        pane.getStyleClass().add("default-scroll-pane");
        pane.getStylesheets().add(themePath);

        pane.setPannable(true);

        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setMaxWidth(ScrollPane.USE_PREF_SIZE);

        return pane;
    }

    public static ListView<MenuTabs> createDefaultSideBarMenu(ObservableList<MenuTabs> tabs) {
        ListView<MenuTabs> listView = new ListView<>(tabs);

        listView.getStyleClass().add("menu-list-view");
        listView.getStylesheets().add(themePath);

        return listView;
    }

    public static <T extends Node> ListView<T> createDefaultListView(ObservableList<T> observableList) {
        ListView<T> listView = new ListView<>(observableList);

        listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                ListCell<T> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                    }
                };

                return cell;
            }
        });

        listView.setEditable(false);

        listView.getStyleClass().add("default-list-view");
        listView.getStylesheets().add(themePath);

        return listView;
    }

    public static StackPane createCurrentUserIcon() {
        StackPane pane = NodeFactory.createPanelWithImage(USER_ICON_PATH);

        pane.getStyleClass().add("current-user-image-pane");
        pane.getStylesheets().add(themePath);

        return pane;
    }

    public static StackPane createSmallUserIcon(String username) {
        String imagePath = NodeFactory.getImagePathFromUsername(username);
        StackPane pane = NodeFactory.createPanelWithImage(imagePath);

        pane.getStyleClass().add("user-image-pane");
        pane.getStylesheets().add(themePath);

        return pane;
    }

    private static StackPane createPanelWithImage(String imagePath) {
        Image icon = new Image(imagePath);

        ImageView imageView = new ImageView(icon);

        imageView.setPreserveRatio(true);

        StackPane pane = new StackPane(imageView);

        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());

        StackPane.setAlignment(imageView, Pos.CENTER);

        return pane;
    }

    public static ToggleSwitch createEmptyToggleSwitch() {
        ToggleSwitch toggleSwitch = new ToggleSwitch();

        toggleSwitch.getStyleClass().add("default-toggle-switch");
        toggleSwitch.getStylesheets().add(themePath);

        return toggleSwitch;
    }

    public static void decorateNavRootPanel(AnchorPane sideBar) {
        sideBar.getStyleClass().add("root-sidebar-panel");
        sideBar.getStylesheets().add(themePath);
    }

    public static void decorateNotificationsRootPane(AnchorPane notificationBar) {
        notificationBar.getStyleClass().add("root-notifications-panel");
        notificationBar.getStylesheets().add(themePath);
    }

    public static void decorateCentralRootPanel(AnchorPane centralView) {
        centralView.getStyleClass().add("root-central-panel");
        centralView.getStylesheets().add(themePath);
    }

    public static void decorateNewsView(AnchorPane newsView) {
        newsView.getStyleClass().add("root-news-panel");
        newsView.getStylesheets().add(themePath);
    }

    public static void decorateNewsPanel(GridPane newsPanel) {
        newsPanel.getStyleClass().add("news-data-panel");
        newsPanel.getStylesheets().add(themePath);

        newsPanel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 8d, 0d, 0d, 2d));
    }

    public static void decorateRootMessagesPanel(AnchorPane messagesPanel) {
        messagesPanel.getStyleClass().add("root-messages-panel");
        messagesPanel.getStylesheets().add(themePath);
    }

    public static void decorateSelfMessage(Pane selfMessage) {
        selfMessage.getStyleClass().add("message-self-panel");
        selfMessage.getStylesheets().add(themePath);
    }

    public static void decorateForeignMessage(Pane foreignMessage) {
        foreignMessage.getStyleClass().add("message-foreign-panel");
        foreignMessage.getStylesheets().add(themePath);

    }

    public static void decorateUsersRootPane(AnchorPane usersView) {
        usersView.getStyleClass().add("users-root-panel");
        usersView.getStylesheets().add(themePath);
    }

    public static void decorateUserPanel(Pane userPanel) {
        userPanel.getStyleClass().add("user-panel");
        userPanel.getStylesheets().add(themePath);
    }
}
