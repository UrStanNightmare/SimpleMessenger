package ru.ao.simplemessenger.client.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.handlers.ClientSocketReaderListener;
import ru.ao.simplemessenger.transfer.MessageMapper;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс для чтения сообщений от сервера.
 */
public class ClientSocketReader extends Thread {
    private final static Logger log = LoggerFactory.getLogger(ClientSocketReader.class);
    private final static String THREAD_NAME = "Reader thread";


    private final ClientSocketReaderListener listener;
    private final BufferedReader reader;

    /**
     * @param listener слушатель прочитанных сообщений
     * @param reader   ридер для чтения сообщений.
     */
    public ClientSocketReader(ClientSocketReaderListener listener, BufferedReader reader) {
        super(THREAD_NAME);
        this.listener = listener;
        this.reader = reader;
    }

    @Override
    public void run() {
        log.info("Server reader started.");
        while (!isInterrupted()) {
            while (true) {
                if (isInterrupted()) {
                    log.info("Interrupted");
                    this.interrupt();
                    break;
                }
                try {
                    String inputString = this.reader.readLine();
                    if (inputString != null) {

                        SocketMessageDTO message = MessageMapper.deserializeMessage(inputString);
                        this.listener.handleReadString(message);
                    }

                } catch (IOException e) {
                    log.warn(e.getMessage());
                    this.listener.handleServerClosedEvent();
                    this.interrupt();
                    break;
                }
            }
        }

        try {
            this.reader.close();
            log.info("Buffered reader closed.");
        } catch (IOException ex) {
            log.error("Can't close reader {}", ex.getMessage(), ex);
        }
        log.info("Server reader stopped.");

    }
}