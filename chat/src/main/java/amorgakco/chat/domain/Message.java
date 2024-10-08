package amorgakco.chat.domain;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "text")
public class Message {
    @Id
    String id;
    MessageType messageType;
    Long memberId;
    Long chatRoomId;
    String text;

    @Builder
    public Message(final MessageType messageType, final Long memberId, final Long chatRoomId, final String text) {
        this.messageType = messageType;
        this.memberId = memberId;
        this.chatRoomId = chatRoomId;
        this.text = text;
    }
}
