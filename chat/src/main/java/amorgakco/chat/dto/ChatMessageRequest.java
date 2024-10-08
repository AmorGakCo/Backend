package amorgakco.chat.dto;

import amorgakco.chat.domain.MessageType;

public record ChatMessageRequest(MessageType messageType, String message, Long memberId, Long chatRoomId) {
}
