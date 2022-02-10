package ru.ao.simplemessenger.transfer;

public enum MessageType {
    //simple message with "message" : String body
    MES,
    //user status
    US,
    //check user
    CU,
    //command type
    CT,
    //getConnectedUsers
    GCU,
    //server info
    SI,
    //disconnected
    D,
    //user connected
    UC;
}
