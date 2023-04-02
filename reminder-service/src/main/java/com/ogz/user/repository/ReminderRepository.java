package com.ogz.user.repository;

import com.ogz.user.model.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends MongoRepository<Reminder,String> {

    List<Reminder> findAllByCreatorId(String creatorId);

    List<Reminder> findAllByTriggerDateLessThanEqual(LocalDateTime currentTime);
}
