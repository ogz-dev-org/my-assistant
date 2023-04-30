package com.ogz.notificationcrudservice.controller;

import com.ogz.notificationcrudservice.client.UserServiceClient;
import com.ogz.notificationcrudservice.dto.CreateUnAckedNotificationDto;
import com.ogz.notificationcrudservice.model.UnAckedNotification;
import com.ogz.notificationcrudservice.service.UnAckedNotificationService;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/unAckedNotification")
public class UnAckedNotificationController {

    private final UserServiceClient userServiceClient;
    private final UnAckedNotificationService unAckedNotificationService;

    public UnAckedNotificationController(UserServiceClient userServiceClient,UnAckedNotificationService unAckedNotificationService) {
        this.userServiceClient = userServiceClient;
        this.unAckedNotificationService = unAckedNotificationService;
    }

    @GetMapping("/")
    ResponseEntity<List<UnAckedNotification>> getAllUnAckedNotifications(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        User findedUser = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(findedUser)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(unAckedNotificationService.getAllUnAckedNotifications(findedUser),HttpStatus.OK);

    }

    @PostMapping("/")
    ResponseEntity<UnAckedNotification> createUnAckedNotification(@RequestBody CreateUnAckedNotificationDto createUnAckedNotificationDto){

        return new ResponseEntity<>(unAckedNotificationService.createUnackedNotification(createUnAckedNotificationDto),HttpStatus.OK);

    }

    @DeleteMapping("/acked/{id}")
    ResponseEntity<UnAckedNotification> ackedNotification(@PathVariable String id){

        UnAckedNotification deletedNotification = unAckedNotificationService.ackedNotification(id);

        if (Objects.isNull(deletedNotification)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(unAckedNotificationService.ackedNotification(id),HttpStatus.OK);

    }

}
