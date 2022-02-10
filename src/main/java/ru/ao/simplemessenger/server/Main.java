package ru.ao.simplemessenger.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.server.config.ConfigManager;
import ru.ao.simplemessenger.server.socket.ServerSocketManager;

import java.io.IOException;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class.getName());


    public static void main(String[] args) {
        log.info("Server app started.");

        try {
            ConfigManager configManager = new ConfigManager();
            int port = configManager.getPort();

            ServerSocketManager manager = new ServerSocketManager(port);

            manager.startServer();
        } catch (NumberFormatException e) {
            log.error("Input port number is not a number!! {}", e.getMessage());
        } catch (IOException e) {
            log.error("Can't create server manager! Fatal error! {}", e.getMessage());
        }

        log.info("Server app stopped.");

    }
}
