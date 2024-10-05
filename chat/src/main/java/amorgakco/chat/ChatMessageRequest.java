package amorgakco.chat;

public record ChatMessageRequest(MessageType messageType, String message, Long userId, Long chatRoomId) {
}
