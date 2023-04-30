package com.ogz.notificationcrudservice.service;

import com.ogz.notificationcrudservice.dto.CreateMailEventDto;
import com.ogz.notificationcrudservice.dto.convertor.EventDtoConvertor;
import com.ogz.notificationcrudservice.model.BaseEvent;
import com.ogz.notificationcrudservice.model.MailEvent;
import com.ogz.notificationcrudservice.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public MailEvent createMailEvent(CreateMailEventDto dto) {
        return eventRepository.save(EventDtoConvertor.convertMail(dto));
    }

}
