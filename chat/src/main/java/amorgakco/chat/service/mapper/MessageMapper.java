package amorgakco.chat.service.mapper;

import amorgakco.chat.domain.Message;
import amorgakco.chat.dto.ChatMessageRequest;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public Message toMessage(ChatMessageRequest request){
        return Message.builder()
                .messageType(request.messageType())
                .text(request.text())
                .chatRoomId(request.chatRoomId())
                .memberId(request.memberId())
                .build();
    }
}
