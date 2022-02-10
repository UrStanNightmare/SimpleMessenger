package ru.ao.simplemessenger.client.enums;

public enum MenuTabs {
    NEWS,
    CHAT,
    USERS,
    DISCONNECT;


    @Override
    public String toString() {
        String name = super.toString();
        return name.substring(0, 1) + name.substring(1).toLowerCase();
    }
}
