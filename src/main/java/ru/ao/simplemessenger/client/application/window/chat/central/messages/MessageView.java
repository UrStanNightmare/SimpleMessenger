package ru.ao.simplemessenger.client.application.window.chat.central.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ru.ao.simplemessenger.client.handlers.SendMessageButtonPressedHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.CustomKeyCombo;
import ru.ao.simplemessenger.client.utils.NodeFactory;

public class MessageView extends AnchorPane implements DefaultMessageView {
    private final String MESSAGES_STRING = "Messages";
    private final String TYPE_A_MESSAGE_STRING = "Type a message...";


    private final ObservableList<GridPane> messagesList = FXCollections.observableArrayList();

    private SendMessageButtonPressedHandler sendHandler;

    private final TextArea messageInputArea;
    private final Button sendButton;
    private final ListView<GridPane> messagesListView;
    private final CustomKeyCombo nextLineCombo = new CustomKeyCombo(KeyCode.ENTER, KeyCode.SHIFT);

    public MessageView() {
        super();

        NodeFactory.decorateRootMessagesPanel(this);

        Label messagesPaneLabel = NodeFactory.createBigLabel(MESSAGES_STRING);

        AnchorPane infoPane = new AnchorPane();
        AnchorManager.setAnchors(infoPane,
                0d,
                0d,
                null,
                0d);

        infoPane.getChildren().addAll(
                messagesPaneLabel
        );

        this.messagesListView = NodeFactory.createDefaultListView(this.messagesList);

        AnchorManager.setAnchors(this.messagesListView,
                50d,
                5d,
                100d,
                5d);

        this.messageInputArea = NodeFactory.createTransparentTextArea(TYPE_A_MESSAGE_STRING);

        AnchorManager.setAnchors(this.messageInputArea,
                605d,
                5d,
                10d,
                100d);

        this.sendButton = NodeFactory.createSendUserMessageButton();
        this.sendButton.setOnAction(event -> {
            if (this.sendHandler != null) {
                this.sendHandler.handle(this.messageInputArea.getText());
                this.messageInputArea.clear();
            }
        });

        this.messageInputArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SHIFT) {
                this.nextLineCombo.addKey(event.getCode());

                if (event.getCode() == KeyCode.ENTER){
                    this.messageInputArea.deletePreviousChar();
                }
            }
        });

        this.messageInputArea.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SHIFT) {
                if (this.nextLineCombo.checkCombo()) {
                    this.messageInputArea.appendText(System.lineSeparator());
                } else {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (this.sendHandler != null) {
                            this.sendHandler.handle(this.messageInputArea.getText());
                            this.messageInputArea.clear();
                        }
                    }
                }
                this.nextLineCombo.removeKey(event.getCode());
            }
        });


        AnchorManager.setAnchors(this.sendButton,
                625d,
                780d,
                30d,
                25d);


        this.getChildren().addAll(
                infoPane, messagesListView, this.messageInputArea, this.sendButton
        );
    }

    @Override
    public void placeMessage(boolean isSelf, String username, String text, String time) {
        if (isSelf) {
            SelfMessage mes = new SelfMessage(username, text, time);
            mes.setMaxWidth(391.5);

            this.messagesList.add(mes);

        } else {
            ForeignMessage mes = new ForeignMessage(username, text, time);
            mes.setMaxWidth(391.5 * 2);

            mes.setPadding(new Insets(0, 0, 0, 391.5));

            this.messagesList.add(mes);
        }
        this.messagesListView.scrollTo(this.messagesListView.getItems().size() + 1);
    }

    @Override
    public void removeMessage(int position) {
        this.messagesList.remove(position);
    }

    @Override
    public void registerSendButtonClickedHandler(SendMessageButtonPressedHandler handler) {
        this.sendHandler = handler;
    }
}
