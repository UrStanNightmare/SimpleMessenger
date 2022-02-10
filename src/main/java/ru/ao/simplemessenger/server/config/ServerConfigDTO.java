package ru.ao.simplemessenger.server.config;

public class ServerConfigDTO {
    private String port;

    public ServerConfigDTO() {
    }

    public ServerConfigDTO(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
