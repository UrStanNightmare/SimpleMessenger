package ru.ao.simplemessenger.client.defaults;

import ru.ao.simplemessenger.client.enums.SceneName;

public interface DefaultSceneController {
    void switchTo(SceneName sceneName);

    void registerView(SceneName sceneName, DefaultView view, double sizeModifier);

    void setNeedsReconnectState(boolean state);
}
