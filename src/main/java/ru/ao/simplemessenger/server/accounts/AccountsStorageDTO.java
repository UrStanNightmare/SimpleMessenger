package ru.ao.simplemessenger.server.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class AccountsStorageDTO {
    private ArrayList<AccountDTO> accountsList;

    public AccountsStorageDTO() {
    }

    public AccountsStorageDTO(ArrayList<AccountDTO> accountsList) {
        this.accountsList = accountsList;
    }

    public ArrayList<AccountDTO> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(ArrayList<AccountDTO> accountsList) {
        this.accountsList = accountsList;
    }

    @JsonIgnore
    public void createDefault() {
        this.accountsList = new ArrayList<>();
    }
}
