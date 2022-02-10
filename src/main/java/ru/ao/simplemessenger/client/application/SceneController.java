package ru.ao.simplemessenger.client.application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.defaults.DefaultSceneController;
import ru.ao.simplemessenger.client.defaults.DefaultView;
import ru.ao.simplemessenger.client.enums.SceneName;

import java.util.EnumMap;
import java.util.Map;

public class SceneController implements DefaultSceneController {
    private final static Logger log = LoggerFactory.getLogger(SceneController.class.getName());

    private Stage stage;
    private final Map<SceneName, DefaultView> sceneMap;
    private boolean isNeedsReconnect = false;
    private double lastMultiplication;

    private final int fadeTime = 500;

    /**
     * Класс для переключения сцен окна. И вызовов события регистрации view и переключения.
     *
     * @param stage        stage окна
     * @param windowWidth  ширина окна
     * @param windowHeight высота окна
     */
    public SceneController(Stage stage, int windowWidth, int windowHeight) {
        this.sceneMap = new EnumMap<>(SceneName.class);

        this.stage = stage;

        this.stage.setTitle("Chat client");

        this.stage.setWidth(windowWidth);
        this.stage.setHeight(windowHeight);

        this.stage.setMinWidth(512d);
        this.stage.setMinHeight(384d);


        this.stage.initStyle(StageStyle.TRANSPARENT);

        this.stage.setResizable(false);

        this.stage.show();
    }


    @Override
    public void switchTo(SceneName sceneName) {
        log.info("Switch to {}", sceneName);

        if (this.lastMultiplication != 0) {
            this.stage.setWidth(this.stage.getWidth() / lastMultiplication);
            this.stage.setHeight(this.stage.getHeight() / lastMultiplication);
        }

        DefaultView view = sceneMap.get(sceneName);

        double multiplier = view.getSizeMultiplier();

        this.stage.setWidth(this.stage.getWidth() * multiplier);
        this.stage.setHeight(this.stage.getHeight() * multiplier);

        this.lastMultiplication = multiplier;

        Scene newScene = this.getRegisteredScene(sceneName);

        this.stage.setScene(newScene);


        view.onViewShown(this.isNeedsReconnect);
    }

    @Override
    public void registerView(SceneName sceneName, DefaultView view, double sizeModifier) {
        this.sceneMap.put(sceneName, view);
    }

    private Scene getRegisteredScene(SceneName sceneName) {
        DefaultView view = this.sceneMap.get(sceneName);
        view.onViewRegistration();

        return view.getScene();
    }

    public void setNeedsReconnectState(boolean state) {
        this.isNeedsReconnect = state;
    }
}
