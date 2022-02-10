package ru.ao.simplemessenger.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageMapper {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String serializeMessage(SocketMessageDTO message) throws JsonProcessingException {
        return mapper.writeValueAsString(message);
    }

    public static SocketMessageDTO deserializeMessage(String message) throws JsonProcessingException {
        return mapper.readValue(message, SocketMessageDTO.class);
    }
}
