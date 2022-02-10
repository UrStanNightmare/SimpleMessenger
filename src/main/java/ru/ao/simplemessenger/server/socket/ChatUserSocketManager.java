package ru.ao.simplemessenger.server.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.transfer.*;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatUserSocketManager extends Thread {
    private final static Logger log = LoggerFactory.getLogger(ChatUserSocketManager.class);

    private final static String DISCONNECTED_STRING = " отключился от чата!";
    private final static String CONNECTED_STRING = " подключился к чату.";

    private final Socket socket;

    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final DefaultServerSocketManager socketManager;
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private boolean isUserChecked;
    private final AtomicBoolean isUserReadyToGetMessages = new AtomicBoolean(false);

    private volatile String usernameConnected;

    public ChatUserSocketManager(Socket socket, DefaultServerSocketManager socketManager) throws IOException {
        this.socket = socket;

        this.socketManager = socketManager;

        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        start();
    }

    @Override
    public void run() {
        log.info("New client manager started!");
        try {
            while (true) {
                String inputString = reader.readLine();

                if (inputString == null) {
                    break;
                }

                SocketMessageDTO message = MessageMapper.deserializeMessage(inputString);

                this.manageMessage(message);

            }

        } catch (IOException e) {
            log.warn("{}", e.getMessage());
            try {
                this.performSocketClose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            this.close();
        }
    }

    private void performSocketClose() throws IOException {
        SocketMessageDTO respM = new Response.Builder()
                .messageType(MessageType.D)
                .addBodyPart(MessageStringHolder.MESSAGE_KEY, this.getConnectedUsername() + DISCONNECTED_STRING)
                .addBodyPart(MessageStringHolder.TIME_KEY, this.getCurrentTime())
                .addBodyPart(MessageStringHolder.USERNAME_KEY, this.getConnectedUsername())
                .build();
        this.sendMessageToAllClients(respM, true);

        this.socketManager.getClientsList().remove(this);
    }

    void manageMessage(SocketMessageDTO message) throws IOException {
        switch (message.getType()) {
            case CU -> {
                log.info("Got check user command.");
                if (!this.isUserChecked) {

                    String user = message.getBody()
                            .get(MessageStringHolder.USERNAME_KEY);

                    String passwordHash = message.getBody()
                            .get(MessageStringHolder.PASSWORD_KEY);

                    if (this.socketManager.checkIfUserOnline(user)) {
                        log.warn("User already online!!!");
                        SocketMessageDTO response = new Response.Builder()
                                .buildStatusMessage(UserStatus.ALREADY_ONLINE);
                        this.sendMessageToConnectedClient(response);
                        return;
                    }

                    UserStatus status = this.socketManager.getUserConnectionStatus(user, passwordHash);

                    switch (status) {
                        case WRONG_PASSWORD -> {
                            log.warn("Wrong password attempt!!!");
                            SocketMessageDTO response = new Response.Builder()
                                    .buildStatusMessage(UserStatus.WRONG_PASSWORD);
                            this.sendMessageToConnectedClient(response);
                            return;
                        }

                        case ACCESS_GRANTED -> {
                            log.info("User logged in.");
                            SocketMessageDTO response = new Response.Builder()
                                    .buildStatusMessage(UserStatus.ACCESS_GRANTED);
                            this.sendMessageToConnectedClient(response);
                        }

                        case CREATED -> {
                            log.info("New user will be saved to db.");

                            SocketMessageDTO response = new Response.Builder()
                                    .buildStatusMessage(UserStatus.CREATED);
                            this.sendMessageToConnectedClient(response);
                        }
                    }

                    this.usernameConnected = user;

                    this.isUserChecked = true;

                    SocketMessageDTO welcomeMessage = new Response.Builder()
                            .messageType(MessageType.UC)
                            .addBodyPart(MessageStringHolder.MESSAGE_KEY, user + CONNECTED_STRING)
                            .addBodyPart(MessageStringHolder.USERNAME_KEY, user)
                            .addBodyPart(MessageStringHolder.TIME_KEY, this.getCurrentTime())
                            .build();

                    this.sendMessageToAllClients(welcomeMessage, true);
                }
            }

            case GCU -> {
                if (this.isUserChecked) {
                    log.info("Got send active users request.");

                    String users = this.socketManager.getConnectedClientsUsernames();

                    SocketMessageDTO usersResponse = new Response.Builder()
                            .buildSendActiveUserListMessage(users);

                    this.sendMessageToConnectedClient(usersResponse);

                    this.isUserReadyToGetMessages.set(true);
                }
            }
            case MES -> {
                if (this.isUserChecked && this.isUserReadyToGetMessages.get()) {
                    String messageString = message.getBody()
                            .get(MessageStringHolder.MESSAGE_KEY);

                    SocketMessageDTO respM = new Response.Builder()
                            .buildChatMessageWithMeta(messageString, this.getConnectedUsername(), this.getCurrentTime());

                    this.sendMessageToAllClients(respM, false);
                }
            }
            case D -> this.performSocketClose();
        }
    }

    private void sendMessageToConnectedClient(SocketMessageDTO message) throws IOException {
        String responseString = MessageMapper.serializeMessage(message);

        writer.write(responseString + "\n");
        writer.flush();
    }

    private void sendMessageToAllClients(SocketMessageDTO message, boolean ignoreSelf) {
        ConcurrentLinkedQueue<ChatUserSocketManager> clients = this.socketManager.getClientsList();

        for (ChatUserSocketManager client : clients) {
            if (client == this) {
                if (ignoreSelf) {
                    continue;
                }
                message.specifySelfStatus(MessageStringHolder.TRUE_STRING);
            } else {
                message.specifySelfStatus(MessageStringHolder.FALSE_STRING);
            }

            if (client.isUserReadyToGetMessages()) {
                try {
                    client.sendMessageToConnectedClient(message);
                } catch (IOException e) {
                    log.error("Can't send message to client {}", e.getMessage(), e);
                }
            }
        }

    }

    public String getConnectedUsername() {
        if (this.isUserChecked) {
            return this.usernameConnected;
        }
        return null;
    }

    private void close() {
        try {
            this.reader.close();
        } catch (IOException e) {
            log.error("Can't close reader! {}", e.getMessage());
        }
        try {
            this.writer.close();
        } catch (IOException e) {
            log.error("Can't close writer! {}", e.getMessage());
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Can't close socket! {}", e.getMessage());
        }
        log.info("Client manager closed successfully!");
    }

    public synchronized boolean isUserReadyToGetMessages() {
        return isUserReadyToGetMessages.get();
    }

    private String getCurrentTime() {
        Date date = new Date();
        return this.dateFormat.format(date);
    }
}
