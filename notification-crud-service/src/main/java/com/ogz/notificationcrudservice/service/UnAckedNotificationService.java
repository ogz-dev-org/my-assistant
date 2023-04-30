package com.ogz.notificationcrudservice.service;

import com.ogz.notificationcrudservice.dto.CreateUnAckedNotificationDto;
import com.ogz.notificationcrudservice.dto.convertor.UnAckedNotificationDtoConvertor;
import com.ogz.notificationcrudservice.model.UnAckedNotification;
import com.ogz.notificationcrudservice.repository.UnAckedNotificationRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnAckedNotificationService {

    private final UnAckedNotificationRepository unAckedNotificationRepository;

    public UnAckedNotificationService(UnAckedNotificationRepository unAckedNotificationRepository) {
        this.unAckedNotificationRepository = unAckedNotificationRepository;
    }

    public List<UnAckedNotification> getAllUnAckedNotifications(User user){
        return unAckedNotificationRepository.findAllByOwnerIdEquals(user.getId());
    }

    public UnAckedNotification createUnackedNotification(CreateUnAckedNotificationDto dto){

        return unAckedNotificationRepository.save(UnAckedNotificationDtoConvertor.convert(dto));

    }

    public UnAckedNotification ackedNotification(String id){

        Optional<UnAckedNotification> findedUnAckedNotifiation = unAckedNotificationRepository.findById(id);

        if (findedUnAckedNotifiation.isEmpty()) return null;
        unAckedNotificationRepository.deleteById(id);
        return findedUnAckedNotifiation.get();

    }

}
