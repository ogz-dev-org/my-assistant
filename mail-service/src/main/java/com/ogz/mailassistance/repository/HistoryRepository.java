package com.ogz.mailassistance.repository;

import com.ogz.mailassistance.model.MailHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryRepository extends MongoRepository<MailHistory, String> {

    MailHistory findAllByUserIdEquals(String userId);

}
