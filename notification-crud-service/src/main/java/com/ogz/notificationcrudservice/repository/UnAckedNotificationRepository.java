package com.ogz.notificationcrudservice.repository;

import com.ogz.notificationcrudservice.model.UnAckedNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnAckedNotificationRepository extends MongoRepository<UnAckedNotification,String> {

    List<UnAckedNotification> findAllByOwnerIdEquals(String ownerId);

}
