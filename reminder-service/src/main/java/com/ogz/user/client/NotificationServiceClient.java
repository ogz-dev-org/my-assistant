package com.ogz.user.client;

import com.ogz.user.dto.ReminderEventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "notification-service", path = "/api/v1/notification")
public interface NotificationServiceClient {

    @GetMapping("/reminderEvent")
    void triggerReminderEvent(@RequestBody ReminderEventDto body);

    @GetMapping("/mailEvent")
    void triggerMailEvent();

    @GetMapping("/messageEvent")
    void triggerMessageEvent();

    @GetMapping("/callEvent")
    void triggerCallEvent();

}
