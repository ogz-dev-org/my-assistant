package com.ogz.mailassistance.repository;

import com.ogz.mailassistance.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MailRepository extends MongoRepository<Message,String> {
}
