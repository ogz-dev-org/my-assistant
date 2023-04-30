package com.ogz.notificationcrudservice.repository;

import com.ogz.notificationcrudservice.model.BaseEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<BaseEvent,String> {
}
