package ru.ao.simplemessenger.client.application.window.serverset;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.handlers.ConnectButtonPressedHandler;
import ru.ao.simplemessenger.client.utils.AnchorManager;
import ru.ao.simplemessenger.client.utils.NodeFactory;
import ru.ao.simplemessenger.client.utils.SizeConverter;

public class ServerChooseView implements DefaultServerChooseView {
    private final static Logger log = LoggerFactory.getLogger(ServerChooseView.class.getName());

    private final String SERVER_IP_STRING = "Server IP:";
    private final String SERVER_IP_HINT = "xxx.xxx.xxx.xxx";
    private final String SERVER_PORT_STRING = "Server Port:";
    private final String SERVER_PORT_HINT = "xxxxx";
    private final String CONNECT_STRING = "Connect";

    private final double ELEMENT_SPACING = 10d;

    private final Scene scene;
    private final double sizeMultiplier;

    private final Button closeButton;
    private final TextField serverIpField;
    private final TextField serverPortField;
    private final Button connectButton;
    private ConnectButtonPressedHandler connectButtonEventHandler;
    private final ProgressIndicator progressIndicator;

    private double xOffset = 0;
    private double yOffset = 0;

    public ServerChooseView(double winWidth, double winHeight, double sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;

        SizeConverter converter = new SizeConverter(winWidth * sizeMultiplier, winHeight * sizeMultiplier,
                882d, 526d);

        this.closeButton = NodeFactory.createDefaultCloseButton();
        AnchorPane.setTopAnchor(this.closeButton, converter.convertFromHeight(16d));
        AnchorPane.setLeftAnchor(this.closeButton, converter.convertFromWidth(16d));
        AnchorPane.setBottomAnchor(this.closeButton, converter.getAppHeight() - AnchorPane.getTopAnchor(this.closeButton) - 14d);
        AnchorPane.setRightAnchor(this.closeButton, converter.getAppWidth() - AnchorPane.getLeftAnchor(this.closeButton) - 14d);

        Label ipLabel = NodeFactory.createBigLabel(SERVER_IP_STRING);
        AnchorManager.setAnchors(ipLabel,
                converter.convertFromHeight(115d),
                converter.convertFromWidth(68.09),
                converter.convertFromHeight(363),
                converter.convertFromWidth(68.09)
        );

        this.serverIpField = NodeFactory.createDefaultTextField(SERVER_IP_HINT);
        AnchorManager.setAnchors(this.serverIpField,
                converter.convertFromHeight(167d),
                converter.convertFromWidth(68.09),
                converter.convertFromHeight(311d),
                converter.convertFromWidth(68.09)
        );

        Label serverPortLabel = NodeFactory.createBigLabel(SERVER_PORT_STRING);
        AnchorManager.setAnchors(serverPortLabel,
                converter.convertFromHeight(252d),
                converter.convertFromWidth(68.09),
                converter.convertFromHeight(226d),
                converter.convertFromWidth(68.09)
        );

        this.serverPortField = NodeFactory.createDefaultTextField(SERVER_PORT_HINT);
        AnchorManager.setAnchors(this.serverPortField,
                converter.convertFromHeight(304d),
                converter.convertFromWidth(68.09),
                converter.convertFromHeight(174d),
                converter.convertFromWidth(68.09)
        );

        this.connectButton = NodeFactory.createDefaultButton(CONNECT_STRING);
        AnchorManager.setAnchors(this.connectButton,
                converter.convertFromHeight(423d),
                converter.convertFromWidth(330d),
                converter.convertFromHeight(64d),
                converter.convertFromWidth(330d)
        );

        this.connectButton.setOnAction(event -> {
            if (this.connectButtonEventHandler != null) {
                this.connectButtonEventHandler.handle(
                        this.serverIpField.getText(),
                        this.serverPortField.getText()
                );
            }
        });

        this.progressIndicator = NodeFactory.createDefaultProgressIndicator();
        AnchorManager.setAnchors(this.progressIndicator,
                converter.convertFromHeight(368d),
                converter.convertFromWidth(421d),
                converter.convertFromHeight(118d),
                converter.convertFromWidth(421d)
        );


        AnchorPane root = NodeFactory.createDefaultRootAnchorPane();

        root.getChildren().addAll(
                this.closeButton, ipLabel, this.serverIpField, serverPortLabel, this.serverPortField,
                this.connectButton, this.progressIndicator
        );
        this.scene = NodeFactory.createScene(root);

        this.configureScene();

        this.connectButton.requestFocus();
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
            if (event.getCode() == KeyCode.ENTER){
                this.connectButton.fire();
            }
        });
    }

    private void setLockedState(boolean isLocked) {
        this.serverIpField.setEditable(!isLocked);
        this.serverPortField.setEditable(!isLocked);
        this.connectButton.setDisable(isLocked);
    }


    @Override
    public void registerConnectButtonEventHandler(ConnectButtonPressedHandler handler) {
        this.connectButtonEventHandler = handler;
    }

    @Override
    public void highlightPortField(boolean isHighlighted) {
        if (isHighlighted) {
            this.serverPortField.getStyleClass().add("text-field-default-highlighted");
        } else {
            this.serverPortField.getStyleClass().remove("text-field-default-highlighted");
        }
    }

    @Override
    public void highlightIpField(boolean isHighlighted) {
        if (isHighlighted) {
            this.serverIpField.getStyleClass().add("text-field-default-highlighted");

        } else {
            this.serverIpField.getStyleClass().remove("text-field-default-highlighted");
        }
    }

    @Override
    public void setProgressIndicatorVisibility(boolean isVisible) {
        this.progressIndicator.setVisible(isVisible);
        this.setLockedState(isVisible);
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
            this.connectButton.fire();
        }
    }
}
