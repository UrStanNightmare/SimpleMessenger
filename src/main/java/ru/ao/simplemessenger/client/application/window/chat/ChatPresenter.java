package ru.ao.simplemessenger.client.application.window.chat;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ao.simplemessenger.client.application.window.chat.central.DefaultCentralView;
import ru.ao.simplemessenger.client.application.window.chat.notification.DefaultNotificationView;
import ru.ao.simplemessenger.client.application.window.chat.sidebar.DefaultSideBarView;
import ru.ao.simplemessenger.client.defaults.DefaultSceneController;
import ru.ao.simplemessenger.client.enums.MenuTabs;
import ru.ao.simplemessenger.client.enums.SceneName;
import ru.ao.simplemessenger.transfer.MessageType;
import ru.ao.simplemessenger.transfer.Response;
import ru.ao.simplemessenger.transfer.SocketMessageDTO;

import static ru.ao.simplemessenger.transfer.MessageStringHolder.*;

public class ChatPresenter implements DefaultChatPresenter {
    private final static Logger log = LoggerFactory.getLogger(ChatPresenter.class.getName());

    private final DefaultChatModel model;
    private final DefaultChatView view;
    private final DefaultSceneController sceneController;
    private Task<Void> messageCheckerTask = null;

    private final DefaultSideBarView sideBarView;
    private final DefaultNotificationView notificationView;
    private final DefaultCentralView centralView;

    public ChatPresenter(DefaultChatModel chatModel, DefaultChatView chatView, DefaultSceneController sceneController) {
        this.model = chatModel;
        this.view = chatView;
        this.sceneController = sceneController;

        this.sideBarView = chatView.getSideBarView();
        this.notificationView = chatView.getNotificationsView();
        this.centralView = chatView.getCentralView();

        this.registerViewHandlers();

//        for (int i = 0; i < 50; i++){
//            this.centralView.addNewsPost("Label" + i,"" +
//                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
//                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "11:30");
//        }
//
//        this.centralView.registerSendUserMessageHandler(messageString -> {
//            if (!messageString.isBlank()){
//                System.out.println(messageString);
//            }
//        });
//
//
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//        this.centralView.addActiveUser("Anton");
//
//        this.sideBarView.setUsername("Krevetko");
//
//        for (int i = 0; i < 50; i++){
//            this.centralView.placeMessage(false, "notAnton" + i, "Text".repeat(i+1), "10:21");
//            this.centralView.placeMessage(true, "Anton" + i, "Hello" + i, "10:21");
//        }


    }

    private void registerViewHandlers() {
        this.view.registerChatViewActivationHandler(() -> {
            this.sideBarView.setUsername(this.model.getActiveUsername());

            log.info("Trying to get active users");
            this.model.sendGetActiveUsersMessageToServer();

            SocketMessageDTO response;
            do {
                response = this.model.getMessageFromQueue();
            } while (response == null);

            if (response.getType() == MessageType.GCU) {
                String[] users = response.getBody()
                        .get(USERS_ONLINE_KEY).split(",");

                for (String username : users) {
                    this.centralView.addActiveUser(username);
                }
            }

            this.registerIncomingMessagesTask();

            new Thread(this.messageCheckerTask).start();
        });

        this.sideBarView.registerMenuSelectionListener((observable, oldValue, newValue) -> {
            if (newValue == MenuTabs.DISCONNECT) {
                SocketMessageDTO exitMessage = new Response.Builder()
                        .buildDisconnectMessageWithoutMeta();

                this.model.sendMessageToServer(exitMessage);

                this.closeChat();

                this.view.cleanChatData();

                this.sceneController.setNeedsReconnectState(false);
                this.sceneController.switchTo(SceneName.SERVER_CHOSE);
            } else {
                if (newValue != oldValue) {
                    this.centralView.switchTo(newValue);
                }
            }
        });

        this.centralView.registerSendDirectHandler(username -> {
        });

        this.centralView.registerSendUserMessageHandler(messageString -> {
            if (messageString.isBlank()) {
                return;
            }
            SocketMessageDTO message = new Response.Builder()
                    .buildChatMessageWithoutMeta(messageString);

            this.model.sendMessageToServer(message);
        });
    }

    private void registerIncomingMessagesTask() {
        this.messageCheckerTask = new Task<Void>() {
            @Override
            protected Void call() {
                log.info("Sub thread started to react on incoming messages.");
                while (true) {
                    if (messageCheckerTask.isCancelled()) {
                        break;
                    }
                    SocketMessageDTO message = null;
                    do {
                        if (model.getIsConnectionFailed()) {
                            Platform.runLater(() -> {
                                view.cleanChatData();
                                sceneController.setNeedsReconnectState(true);
                                sceneController.switchTo(SceneName.SERVER_CHOSE);
                            });
                            log.warn("Server connection lost!!!");
                            return null;
                        }

                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            log.warn("Message check thread interrupted!");
                            break;
                        }

                        message = model.getMessageFromQueue();
                    } while (message == null);

                    String messageString = message.getBody()
                            .get(MESSAGE_KEY);

                    String timeString = message.getBody()
                            .get(TIME_KEY);

                    String username = message.getSentUsername();

                    String isSelfString = message.getBody()
                            .get(IS_SELF_KEY);


                    switch (message.getType()) {
                        case UC -> {
                            Platform.runLater(() -> {
                                centralView.placeMessage(true, "", messageString, timeString);
                                centralView.addActiveUser(username);
                            });
                        }
                        case D -> {
                            Platform.runLater(() -> {
                                centralView.placeMessage(true, "", messageString, timeString);
                                centralView.removeActiveUser(username);
                            });
                        }

                        case MES -> {
                            boolean isSelf = false;
                            if (isSelfString.equals(TRUE_STRING)) {
                                isSelf = true;
                            }

                            if (isSelf) {
                                Platform.runLater(() -> {
                                    centralView.placeMessage(true, username, messageString, timeString);
                                });
                            } else {
                                Platform.runLater(() -> {
                                    centralView.placeMessage(false, username, messageString, timeString);
                                });
                            }

                        }
                    }
                }
                log.info("Closed message check thread.");
                return null;
            }
        };
    }

    private void closeChat() {
        if (this.messageCheckerTask != null) {
            this.messageCheckerTask.cancel();
        }
        this.model.closeServerConnection();
    }
}
