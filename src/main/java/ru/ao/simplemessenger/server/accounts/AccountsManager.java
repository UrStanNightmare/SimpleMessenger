package ru.ao.simplemessenger.server.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.transfer.UserStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AccountsManager {
    private final static Logger log = LoggerFactory.getLogger(AccountsManager.class.getName());

    private final static String ACCOUNT_FILE_PATH = "ac";

    private boolean isLoadedSuccessful;

    private final ObjectMapper mapper = new ObjectMapper();
    private final File accountsFile = new File(ACCOUNT_FILE_PATH);
    private AccountsStorageDTO accounts;

    public AccountsManager() {
        try {
            this.accounts = mapper.readValue(accountsFile, AccountsStorageDTO.class);
            this.isLoadedSuccessful = true;
            log.info("Users accounts loaded successfully.");
        } catch (FileNotFoundException e) {
            log.warn("Accounts file error! Creating default. " + e.getMessage());
            this.createAccountFile();
            this.isLoadedSuccessful = true;
        } catch (IOException e) {
            this.isLoadedSuccessful = false;
            log.warn("Accounts file error! " + e.getMessage(), e);
        }
    }

    private void createAccountFile() {
        this.accounts = new AccountsStorageDTO();
        this.accounts.createDefault();

        this.saveOnDrive();
    }

    private void saveOnDrive() {
        try {
            mapper.writeValue(accountsFile, this.accounts);
        } catch (IOException e) {
            log.error("An attempt to create default config file failed! {}", e.getMessage());
        }
    }

    public synchronized UserStatus getUserStatus(String username, String hash) {
        if (!this.isLoadedSuccessful) {
            return UserStatus.CREATED;
        }

        for (AccountDTO account : this.accounts.getAccountsList()) {
            if (account.getUsername().equals(username)) {
                if (account.getPasswordHash().equals(hash)) {
                    return UserStatus.ACCESS_GRANTED;
                }
                return UserStatus.WRONG_PASSWORD;
            }
        }
        this.createNewUser(username, hash);
        return UserStatus.CREATED;
    }

    private void createNewUser(String username, String hash) {
        this.accounts.getAccountsList().add(new AccountDTO(username, hash));
        this.saveOnDrive();
    }

}
