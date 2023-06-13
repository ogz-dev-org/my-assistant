package org.ogz.client;

import org.ogz.dto.MailSocketDto;
import org.ogz.dto.ReminderEventDto;
import org.ogz.model.Mail;
import org.ogz.model.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/api/v1/notification")
public interface NotificationServiceClient {

    @GetMapping("/reminderEvent")
    void triggerReminderEvent(@RequestBody ReminderEventDto body);

    @GetMapping("/mailEvent")
    void triggerMailEvent(MailSocketDto mail);

    @GetMapping("/messageEvent")
    void triggerMessageEvent(Message message);

    @GetMapping("/callEvent")
    void triggerCallEvent();

}
