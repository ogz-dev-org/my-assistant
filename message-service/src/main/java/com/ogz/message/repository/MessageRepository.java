package com.ogz.message.repository;

import org.ogz.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findMessagesByFromUser(String fromUser);

    List<Message> findMessagesByFromUserAndSendDateLessThanEqual(String fromUser, LocalDateTime sendDate);

}
