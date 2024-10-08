package amorgakco.chat.service;

import amorgakco.chat.domain.MessageType;
import amorgakco.chat.dto.ChatMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ChatRoom {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public void handleMessage(WebSocketSession session, ChatMessageRequest chatMessageRequest, ObjectMapper objectMapper) throws JsonProcessingException {
        if (chatMessageRequest.messageType() == MessageType.ENTER) {
            join(session);
        }
        send(chatMessageRequest, objectMapper);
    }

    private void join(WebSocketSession session) {
        sessions.add(session);
    }

    private void send(ChatMessageRequest chatMessageRequest, ObjectMapper objectMapper) throws JsonProcessingException {
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(chatMessageRequest));
        sessions.parallelStream().forEach(session -> {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
