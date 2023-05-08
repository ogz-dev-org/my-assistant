package com.ogz.message.client;

import com.ogz.message.model.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "notification-service", path = "/api/v1/notification")
public interface NotificationServiceClient {

    @GetMapping("/reminderEvent")
    void triggerReminderEvent();

    @GetMapping("/mailEvent")
    void triggerMailEvent();

    @GetMapping("/messageEvent")
    void triggerMessageEvent(Message message);

    @GetMapping("/callEvent")
    void triggerCallEvent();

}
