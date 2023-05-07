package com.ogz.mailassistance.repository;

import com.ogz.mailassistance.model.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MailRepository extends MongoRepository<Mail,String> {

    List<Mail> findAllByToUserEquals(String toUser);

    List<Mail> findAllByFromUserAndSendingDateGreaterThan(String fromUser, LocalDateTime sendingDate);

    Mail findByOriginalMailIdEquals(String originalMailId);

}
