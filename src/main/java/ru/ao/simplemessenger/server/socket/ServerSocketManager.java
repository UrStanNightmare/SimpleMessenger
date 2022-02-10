package ru.ao.simplemessenger.server.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.server.accounts.AccountsManager;
import ru.ao.simplemessenger.transfer.UserStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerSocketManager implements DefaultServerSocketManager {
    private final static Logger log = LoggerFactory.getLogger(ServerSocketManager.class);

    private final ServerSocket serverSocket;
    private final AccountsManager accountsManager;

    private final ConcurrentLinkedQueue<ChatUserSocketManager> clients = new ConcurrentLinkedQueue<>();

    public ServerSocketManager(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.accountsManager = new AccountsManager();
    }

    @Override
    public void startServer() {
        log.info("Server socket started on {} port.", this.serverSocket.getLocalPort());
        try {
            while (true) {
                log.info("Waiting for new client connection.");
                Socket socket = this.serverSocket.accept();
                log.info("Incoming connection.");

                try {
                    this.clients.add(new ChatUserSocketManager(socket, this));
                    log.info("Client added!");
                } catch (IOException e) {
                    log.error("Can't connect with user.! {}", e.getMessage());

                    socket.close();
                }
            }
        } catch (IOException e) {
            log.error("Error on server socket! {}", e.getMessage());
        }
    }

    public synchronized ConcurrentLinkedQueue<ChatUserSocketManager> getClientsList() {
        return this.clients;
    }

    @Override
    public String getConnectedClientsUsernames() {
        ArrayList<String> usersConnected = new ArrayList<>();

        for (ChatUserSocketManager client : this.clients) {
            String username = client.getConnectedUsername();
            if (username != null && !username.isBlank()) {
                usersConnected.add(username);
            }
        }

        String usersString = String.join(",", usersConnected.stream().sorted().toList());

        return usersString;
    }

    @Override
    public synchronized boolean checkIfUserOnline(String user) {
        for (ChatUserSocketManager client : clients) {
            if (client.isUserReadyToGetMessages() && client.getConnectedUsername().equals(user)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public synchronized UserStatus getUserConnectionStatus(String username, String password) {
        return this.accountsManager.getUserStatus(username, password);
    }

    @Override
    public void closeServer() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            log.error("Can't close manager! {}", e.getMessage());
        }
    }
}
