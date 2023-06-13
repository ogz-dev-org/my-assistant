package com.ogz.mailassistance.repository;

import org.ogz.model.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface MailRepository extends MongoRepository<Mail,String> {

    List<Mail> findAllByToUserEquals(String toUser);

    List<Mail> findAllByFromUserAndSendingDateGreaterThan(String fromUser, Date sendingDate);

    Mail findByOriginalMailIdEquals(String originalMailId);

}
