package ru.ao.simplemessenger.client.defaults;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

public interface DefaultView {
    Scene getScene();

    Stage getStage();

    double getSizeMultiplier();

    void registerCloseButtonEventHandler(EventHandler<ActionEvent> handler);

    void onViewRegistration();

    void onViewShown(boolean needsReconnect);
}
