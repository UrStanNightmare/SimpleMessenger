package ru.ao.simplemessenger.client.application.window.serverset;

import ru.ao.simplemessenger.client.socket.DefaultSocketManager;

public class ServerChooseModel implements DefaultServerChooseModel {
    private final DefaultSocketManager socketManager;

    public ServerChooseModel(DefaultSocketManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void updateServerInfo(String ip, int localPort) {
        this.socketManager.setServerInfo(ip, localPort);
    }

    @Override
    public void tryToConnectToServer() {
        this.socketManager.tryToConnect();
    }

    @Override
    public void closeServerConnection() {
        this.socketManager.closeConnection();
    }
}
