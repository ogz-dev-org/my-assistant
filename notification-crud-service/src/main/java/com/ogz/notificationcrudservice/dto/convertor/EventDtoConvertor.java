package com.ogz.notificationcrudservice.dto.convertor;

import com.ogz.notificationcrudservice.dto.CreateMailEventDto;
import com.ogz.notificationcrudservice.model.MailEvent;

public class EventDtoConvertor {

    public static MailEvent convertMail(CreateMailEventDto dto){
        return new MailEvent();
    }

}
