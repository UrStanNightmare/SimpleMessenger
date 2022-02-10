package ru.ao.simplemessenger.client.application.window.userset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.socket.DefaultSocketManager;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsernameSetModel implements DefaultUsernameSetModel {
    private final static Logger log = LoggerFactory.getLogger(UsernameSetModel.class.getName());

    private final DefaultSocketManager socketManager;

    public UsernameSetModel(DefaultSocketManager socketManager) {
        this.socketManager = socketManager;
    }

    @Override
    public void closeServerConnection() {
        this.socketManager.closeConnection();
    }

    @Override
    public boolean checkIfUsernameExists(String username, String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String stringHash = new String(messageDigest.digest());
            return this.socketManager.performUserExistenceCheck(username, stringHash);
        } catch (NoSuchAlgorithmException e) {
            log.error("{}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public SocketMessageDTO getMessageFromQueue() {
        return this.socketManager.getMessageFromQueue();
    }

    @Override
    public void bindUsernameToSocketManager(String username) {
        this.socketManager.bindUsername(username);
    }
}
