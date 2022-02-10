package ru.ao.simplemessenger.client.application.window.serverset;

import ru.ao.simplemessenger.client.defaults.DefaultModel;

public interface DefaultServerChooseModel extends DefaultModel {
    void updateServerInfo(String ip, int localPort);

    void tryToConnectToServer();

}
