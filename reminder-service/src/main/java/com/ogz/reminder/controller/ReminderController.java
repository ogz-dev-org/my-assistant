package com.ogz.reminder.controller;

import org.ogz.dto.CreateReminderDto;
import com.ogz.reminder.model.Reminder;
import com.ogz.reminder.service.ReminderService;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/reminder")
public class ReminderController {

    private final UserServiceClient userServiceClient;
    private final ReminderService reminderService;

    @Hidden
    @GetMapping("/hello")
    public String hello(){
        return "Hello from Reminder Service";
    }

    public ReminderController(UserServiceClient userServiceClient, ReminderService reminderService) {
        this.userServiceClient = userServiceClient;
        this.reminderService = reminderService;
    }
    @GetMapping("/")
    public ResponseEntity<List<Reminder>> getAllReminders(@RequestHeader(HttpHeaders.AUTHORIZATION)String token){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(reminderService.getAllReminders(user),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderDetail(@PathVariable String id){return null;}
    @PostMapping("/")
    public ResponseEntity<Reminder> createReminder(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,
                                                   @RequestBody CreateReminderDto body)
     {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
         System.out.println("new reminder created.");
        return new ResponseEntity<>(reminderService.createReminder(body.getTitle(), body.getContent(), body.getTriggerDate()
                ,user.getId(),body.getUserList()),
                HttpStatus.OK);
    }
    @PutMapping("/")
    public ResponseEntity<Reminder> updateReminder(@RequestHeader(HttpHeaders.AUTHORIZATION)String token,
                                                   @RequestBody CreateReminderDto body){return null;}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable String id){
        Reminder reminder = reminderService.getReminder(id);
        if (Objects.isNull(reminder)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (!user.getId().equals(reminder.getCreatorId())) {
            return new ResponseEntity<>("You don't have permission to delete this reminder !",HttpStatus.UNAUTHORIZED);
        }
        reminderService.deleteReminder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/check/{id}")
    public ResponseEntity checkReminder(@PathVariable String id){
        reminderService.checkReminder(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
