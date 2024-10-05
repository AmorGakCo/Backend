package amorgakco.chat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "message")
public class Message {
    @Id
    String id;
    MessageType messageType;
    Long memberId;
    Long chatRoomId;
    String message;
}
