package amorgakco.chat.dto;

import amorgakco.chat.domain.MessageType;

public record ChatMessageRequest(MessageType messageType, String text, Long memberId, Long chatRoomId) {
}
