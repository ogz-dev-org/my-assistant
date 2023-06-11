package com.ogz.notificationcrudservice.controller;

import com.ogz.notificationcrudservice.dto.CreateMailEventDto;
import com.ogz.notificationcrudservice.model.MailEvent;
import com.ogz.notificationcrudservice.model.MessageEvent;
import com.ogz.notificationcrudservice.model.ReminderEvent;
import com.ogz.notificationcrudservice.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/mail")
    ResponseEntity<MailEvent> createMail(@RequestBody CreateMailEventDto dto){

        return new ResponseEntity<>(eventService.createMailEvent(dto), HttpStatus.OK);

    }

    @PostMapping("/message")
    ResponseEntity<MessageEvent> createMessage(@RequestBody CreateMailEventDto dto){

        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @PostMapping("/reminder")
    ResponseEntity<ReminderEvent> createReminder(@RequestBody CreateMailEventDto dto){

        return new ResponseEntity<>(null, HttpStatus.OK);

    }

}
