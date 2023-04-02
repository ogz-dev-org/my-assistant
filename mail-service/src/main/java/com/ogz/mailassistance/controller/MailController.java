package com.ogz.mailassistance.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.service.MailService;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final UserServiceClient userServiceClient;
    private final MailService service;

    public MailController(UserServiceClient userServiceClient, MailService service) {
        this.userServiceClient = userServiceClient;
        this.service = service;
    }

    @GetMapping("/{id}")
    ResponseEntity<String> test (@PathVariable String id) throws GeneralSecurityException, IOException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();

        return new ResponseEntity<>("Mail Sayisi: " + service.getUserEmails(user),HttpStatus.OK);
    }

    @GetMapping("/")
    ResponseEntity<String> helloWord() {
        System.out.println("naber");

        return new ResponseEntity<>("Hello World from mail service", HttpStatus.OK);
    }

    @GetMapping("/getUsersEmails")
    ResponseEntity<String> getUserEmails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException, GeneralSecurityException {
        System.out.println("getUserEmails");
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        System.out.println(user.getGoogleID());
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        service.getUserEmails(user);
        return null;
    }
    @GetMapping("/getAwaitUser")
    ResponseEntity<AwaitUser> getAwaitUser() {
        return new ResponseEntity<>(userServiceClient.getFirstAwaitUser().getBody(),HttpStatus.OK);
    }

    @PostMapping("/watch/{id}")
    ResponseEntity<String> watchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.watch(user),HttpStatus.OK);
    }

    @PostMapping("/unWatch/{id}")
    ResponseEntity<String> unWatchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.unWatch(user),HttpStatus.OK);
    }



}
