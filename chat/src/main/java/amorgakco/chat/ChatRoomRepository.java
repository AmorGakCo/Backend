package amorgakco.chat;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatRoomRepository {
    private final Map<Long, ChatRoom> chatRoomMap = new HashMap<>();

    public ChatRoom getChatRoom(Long id) {
        if(chatRoomMap.containsKey(id)){
            return chatRoomMap.get(id);
        }else{
            ChatRoom chatRoom = new ChatRoom();
            chatRoomMap.put(id,new ChatRoom());
            return chatRoom;
        }
    }
}
