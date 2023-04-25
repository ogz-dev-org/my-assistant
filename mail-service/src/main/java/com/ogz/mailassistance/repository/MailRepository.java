package com.ogz.mailassistance.repository;

import com.ogz.mailassistance.model.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MailRepository extends MongoRepository<Mail,String> {

    List<Mail> findAllByFromUser(String fromUser);

    List<Mail> findAllByFromUserAndSendingDateGreaterThan(String fromUser, LocalDateTime sendingDate);

}
