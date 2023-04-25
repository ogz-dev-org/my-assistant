package com.ogz.mailassistance.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.dto.AllMailDto;
import com.ogz.mailassistance.dto.SendMailDto;
import com.ogz.mailassistance.model.Mail;
import com.ogz.mailassistance.service.MailService;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final UserServiceClient userServiceClient;
    private final MailService service;

    public MailController(UserServiceClient userServiceClient, MailService service) {
        this.userServiceClient = userServiceClient;
        this.service = service;
    }

    @Hidden
    @GetMapping("/{id}")
    ResponseEntity<String> test (@PathVariable String id) throws GeneralSecurityException, IOException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();

        return new ResponseEntity<>("Mail Sayisi: " + service.getUserEmails(user),HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/")
    ResponseEntity<String> helloWord() {
        System.out.println("naber");

        return new ResponseEntity<>("Hello World from mail service", HttpStatus.OK);
    }


    @Hidden
    @GetMapping("/getUsersEmailsBackend")
    ResponseEntity<Integer> getUserEmailsBackend(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException,
            GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.getUserEmails(user), HttpStatus.OK);
    }

    @GetMapping("/getUsersEmails")
    ResponseEntity<List<Mail>> getUserEmails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @RequestParam LocalDateTime lastMailDate) {

        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(service.getUserMailsFromLastDate(user,lastMailDate), HttpStatus.OK);
    }

    @GetMapping("/getUsersEmails/all")
    ResponseEntity<AllMailDto> getUserEmailsAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new AllMailDto(service.getAllUserMails(user)), HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/getAwaitUser")
    ResponseEntity<AwaitUser> getAwaitUser() {
        return new ResponseEntity<>(userServiceClient.getFirstAwaitUser().getBody(),HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/watch/{id}")
    ResponseEntity<String> watchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.watch(user),HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/unWatch/{id}")
    ResponseEntity<String> unWatchUser(@PathVariable String id) throws IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(id).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.unWatch(user),HttpStatus.OK);
    }

    @PostMapping("/send")
    ResponseEntity<Mail> sendMail(@RequestBody SendMailDto sendMailDto,@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws MessagingException, IOException, GeneralSecurityException {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mail mail = service.sendMail(sendMailDto,user);
        if (Objects.isNull(mail)) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(mail,HttpStatus.OK);
    }

}
