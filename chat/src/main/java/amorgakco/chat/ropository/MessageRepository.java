package amorgakco.chat.ropository;

import amorgakco.chat.domain.Message;

public interface MessageRepository extends MongoRepository<Message,String> {
}
