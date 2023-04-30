package com.ogz.notificationcrudservice.dto.convertor;

import com.ogz.notificationcrudservice.dto.CreateUnAckedNotificationDto;
import com.ogz.notificationcrudservice.model.UnAckedNotification;

public class UnAckedNotificationDtoConvertor {

    public static UnAckedNotification convert(CreateUnAckedNotificationDto dto){
        return new UnAckedNotification(dto.getEventId(), dto.getOwnerId(), dto.getEventType());
    }

}
