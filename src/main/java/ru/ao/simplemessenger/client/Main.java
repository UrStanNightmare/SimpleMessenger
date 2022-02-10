package ru.ao.simplemessenger.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.application.MainApplication;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class.getName());


    public static void main(String[] args) {
        log.info("Application started!");

        MainApplication application = new MainApplication();

        application.startApplication();


    }
}
