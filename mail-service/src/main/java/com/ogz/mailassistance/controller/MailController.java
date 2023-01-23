package com.ogz.mailassistance.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.ogz.mailassistance.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    private final UserServiceClient userServiceClient;

    public MailController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/")
    ResponseEntity<String> helloWord() {
        System.out.println("naber");
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> addUserToQueue(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException {
        // Get user by token
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken().get("Google"));
                Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Auth Code Exchange Demo")
                        .build();
                var deneme = gmail.users().messages().list(user.getEmails().get("Google")).execute();
                var liste = deneme.getMessages();
                System.out.println("Emails: " + liste);
        return null;
    }

    @PostMapping("/test")
    ResponseEntity<Map> getMails(@RequestHeader Map<String, String> headers) {
////         Use access token to call API
//                GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
//                Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
//                        .setApplicationName("Auth Code Exchange Demo")
//                        .build();
//                var deneme = gmail.users().messages().list(idToken.getPayload().getEmail()).execute();
//                var liste = deneme.getMessages();
//                System.out.println("Emails: " + liste);
//                var test = new ResponseEntity<>(liste,HttpStatus.OK);
//                System.out.println(test.getBody());
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }

}
