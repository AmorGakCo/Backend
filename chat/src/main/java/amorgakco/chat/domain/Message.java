package amorgakco.chat.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "text")
@Getter
public class Message {
    @Id
    private String id;
    private final MessageType messageType;
    private final Long memberId;
    private final Long chatRoomId;
    private final String text;

    @Builder
    public Message(final MessageType messageType, final Long memberId, final Long chatRoomId, final String text) {
        this.messageType = messageType;
        this.memberId = memberId;
        this.chatRoomId = chatRoomId;
        this.text = text;
    }
}
