package ru.ao.simplemessenger.client.enums;

public enum AppTheme {
    LIGHT("light-default.css"),
    DARK("dark-default.css");

    private final String FOLDER_PATH = "styles/";
    private final String themePath;

    AppTheme(String url) {
        this.themePath = FOLDER_PATH + url;
    }

    public String getThemePath(){
        return this.themePath;
    }
}
