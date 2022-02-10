package ru.ao.simplemessenger.client.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.enums.ConnectionResult;
import ru.ao.simplemessenger.client.handlers.ClientSocketReaderListener;
import ru.ao.simplemessenger.client.handlers.SocketManagerListener;
import ru.ao.simplemessenger.transfer.MessageMapper;
import ru.ao.simplemessenger.transfer.Response;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс для работы с сокетом пользователя.
 * Выполняет подключение к серверу в отдельном потоке.
 * Пытается подключиться RECONNECT_ATTEMPTS раз.
 * Создаёт ридер сообщений от сервера в отдельном потоке.
 */
public class ClientSocketManager implements DefaultSocketManager, ClientSocketReaderListener {
    private final static Logger log = LoggerFactory.getLogger(ClientSocketManager.class.getName());

    private final static int RECONNECT_ATTEMPTS = 10;

    private List<SocketManagerListener> listeners = new ArrayList<>();

    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private String ip;
    private int port;
    private boolean needsToAddUsername;

    private String activeUsername;

    private ClientSocketReader clientSocketReader;

    private Task<Void> connectTask = null;

    private final AtomicInteger attemptsToConnectDone = new AtomicInteger(0);

    //Очередь прочитанных сообщений.
    private final ConcurrentLinkedQueue<SocketMessageDTO> messageQueue = new ConcurrentLinkedQueue<>();

    private final AtomicBoolean connectionFailed = new AtomicBoolean(false);

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


    public ClientSocketManager() {

    }

    /**
     * Подключение к сокету сервера.
     *
     * @param ip   ip сервера
     * @param port порт сервера
     */
    private void updateServerSocket(String ip, int port) {
        this.attemptsToConnectDone.set(0);
        InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
        this.clientSocket = new Socket();
        this.connectTask = new Task<>() {
            @Override
            protected Void call() {
                log.info("Sub thread started to connect.");
                while (true) {
                    if (connectTask.isCancelled()) {
                        log.info("Closed connection thread.");
                        return null;
                    }

                    int attempt = attemptsToConnectDone.incrementAndGet();
                    if (attempt > RECONNECT_ATTEMPTS) {
                        break;
                    }
                    log.info("Attempt {} to connect.", attempt);
                    try {
                        clientSocket = new Socket();
                        clientSocket.connect(socketAddress);

                        log.info("Sub thread connected to server.");
                        Platform.runLater(() -> {
                            updateIOs();
                        });
                        return null;
                    } catch (IOException e) {
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            log.error("{}, ex", ex.getMessage());
                        }
                        log.warn("Can't connect to server. {}. Retrying.", e.getMessage());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ignored) {
                        }

                    }
                }
                Platform.runLater(() -> {
                    connectTask = null;
                    sendConnectionResultToListeners(ConnectionResult.NOT_CONNECTED);
                });
                log.info("Failed to connect.");
                return null;
            }
        };

        new Thread(this.connectTask).start();
    }


    private void updateBufferedWriter() throws IOException {
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


    }

    private void updateBufferedReader() throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        this.clientSocketReader = new ClientSocketReader(this, this.reader);
        this.clientSocketReader.start();
    }

    @Override
    public void setServerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private synchronized void updateIOs() {
        try {
            this.updateBufferedWriter();
            this.updateBufferedReader();

            this.sendConnectionResultToListeners(ConnectionResult.CONNECTED);
            this.connectionFailed.set(false);
        } catch (IOException e) {
            log.error("{}", e.getMessage(), e);

            this.sendConnectionResultToListeners(ConnectionResult.NOT_CONNECTED);
            this.connectionFailed.set(true);
        }
    }

    private String getCurrentDate() {
        Date date = new Date();
        return this.dateFormat.format(date);
    }

    @Override
    public void tryToConnect() {
        log.info("Trying to connect to server.");
        this.updateServerSocket(this.ip, this.port);
    }

    @Override
    public void closeConnection() {
        try {
            if (this.activeUsername != null) {
                this.unbindUsername();
            }

            if (this.connectTask != null) {

                this.connectTask.cancel();
            }

            if (this.clientSocketReader != null) {
                this.clientSocketReader.interrupt();
            }

            if (this.writer != null) {
                this.writer.close();
            }
            if (this.clientSocket != null) {
                this.clientSocket.close();
            }

        } catch (IOException e) {
            log.error("Can't close the socket! {}", e.getMessage());
        }
    }

    @Override
    public void addConnectionStateListener(SocketManagerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeConnectionStateListener(String name) {
        this.listeners.stream().filter(
                        listener -> listener.getListenerName().equals(name))
                .forEach(listener -> this.listeners.remove(listener));
    }

    @Override
    public void sendConnectionResultToListeners(ConnectionResult result) {
        this.listeners.stream().forEach(listener -> {
            listener.handleConnectionResult(result);
        });
    }

    /**
     * Отправка сообщения на серверный сокет.
     *
     * @param message сообщение для отправки.
     * @return true в случае отпарки, false в случае ошибки.
     */
    @Override
    public boolean sendMessageToServer(SocketMessageDTO message) {
        SocketMessageDTO sendMessage = message;
        if (this.needsToAddUsername && message.getBody() != null) {
            sendMessage.specifyUsername(this.activeUsername);
        }

        sendMessage.specifyDate(this.getCurrentDate());

        String sendString;
        try {
            sendString = MessageMapper.serializeMessage(sendMessage);
        } catch (JsonProcessingException e) {
            log.error("Can't create message send string. Will not send! {}", e.getMessage());
            return false;
        }

        try {
            writer.write(sendString + "\n");
            writer.flush();
            return true;

        } catch (IOException e) {
            log.error("Can't send message to server! {}", e.getMessage());
            return false;
        } catch (NullPointerException e) {
            log.error("Can't send message !{}", e.getMessage());
            return false;
        }

    }

    /**
     * Запросить у сервера проверку наличия пользователя в сети и в бд. После этого сервер отправит результат.
     *
     * @param username имя пользователя.
     * @param password хэшированный пароль.
     * @return было ли отправлено сообщение.
     */
    @Override
    public boolean performUserExistenceCheck(String username, String password) {
        SocketMessageDTO message = new Response.Builder()
                .buildCheckUserMessage(username, password);

        return this.sendMessageToServer(message);
    }

    /**
     * Метод для получения сообщений из очереди сообщений.
     *
     * @return первое сообщение в очереди.
     */
    @Override
    public synchronized SocketMessageDTO getMessageFromQueue() {
        return this.messageQueue.poll();
    }

    /**
     * Метод для привязки имени пользователя к менеджеру.
     *
     * @param username имя пользователя для привязки.
     * @see #getBoundUsername()
     * @see #unbindUsername()
     */
    @Override
    public void bindUsername(String username) {
        this.activeUsername = username;
        this.needsToAddUsername = true;
    }

    @Override
    public String getBoundUsername() {
        return this.activeUsername;
    }

    @Override
    public void unbindUsername() {
        this.activeUsername = null;
        this.needsToAddUsername = false;
    }

    /**
     * Метод для добавления прочитанного сообщения в конец очереди.
     *
     * @param message сообщение для добавления.
     */
    @Override
    public void handleReadString(SocketMessageDTO message) {
        try {
            this.messageQueue.offer(message);
        } catch (NullPointerException e) {
            log.error("Can't add input message to queue! {}", e.getMessage());
        }

    }

    @Override
    public synchronized void handleServerClosedEvent() {
        this.connectionFailed.set(true);
        this.closeConnection();
        this.messageQueue.clear();
    }

    @Override
    public synchronized boolean getIsConnectionFailed() {
        return this.connectionFailed.get();
    }

}
