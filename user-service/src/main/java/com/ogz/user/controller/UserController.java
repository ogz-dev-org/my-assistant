package com.ogz.user.controller;


import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ogz.user.clients.MailServiceClient;
import com.ogz.user.service.UserService;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

import static com.ogz.user.constants.ClientID.IOSClientID;
import static com.ogz.user.constants.ClientID.WebAppClientID;
import static com.ogz.user.constants.Secrets.CLIENT_SECRET_FILE;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final MailServiceClient mailClient;

    public UserController(UserService userService, MailServiceClient mailClient) {

        this.userService = userService;

        this.mailClient = mailClient;
    }

    @GetMapping("/")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello from User Service and" + mailClient.helloWord().getBody(), HttpStatus.OK);
    }

    @GetMapping("/{googleId}")
    ResponseEntity<User> findUserByGoogleId(@PathVariable String googleId){
        return new ResponseEntity<>( userService.findUserByGoogleId(googleId), HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<String> verifyUser(@RequestHeader Map<String, String> headers) throws IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(WebAppClientID, IOSClientID))
                .build();
        String idTokenString = headers.get("idtoken");
        String authCode = headers.get("authcode");

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if (idToken != null) { // User verified
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            System.out.println(userId);
            System.out.println(payload.toPrettyString());
            GoogleClientSecrets clientSecrets = null;
            try {
                clientSecrets = GoogleClientSecrets.load(
                        GsonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE));
                GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        clientSecrets.getDetails().getClientId(),
                        clientSecrets.getDetails().getClientSecret(),
                        authCode,
                        "")
                        .execute();
//                GoogleCredential credential = new GoogleCredential().setAccessToken(tokenResponse.getAccessToken());
//                Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
//                        .setApplicationName("Auth Code Exchange Demo")
//                        .build();
//                WatchRequest request = new WatchRequest();
//                request.setTopicName("projects/graphic-transit-370816/topics/gmail");
//                var den = gmail.users().watch(payload.getEmail(), request).execute();
                //gmail.users().stop(payload.getEmail()).execute();
                User user = userService.findUserByGoogleId(new String(Base64.getEncoder().encode(userId.getBytes())));
                if (Objects.isNull(user)) {
                    HashMap<String, String> emails = new HashMap<>();
                    emails.put("Google", payload.getEmail());
                    HashMap<String, String> accessTokens = new HashMap<>();
                    accessTokens.put("Google",tokenResponse.getAccessToken());
                    HashMap<String, String> refreshTokens = new HashMap<>();
                    refreshTokens.put("Google", tokenResponse.getRefreshToken());
                    user = userService.addUser(new User(Base64.getEncoder().encodeToString(userId.getBytes()),
                            (String) payload.get("given_name"),(String) payload.get("family_name"),
                            new HashMap<String, Integer>(),
                            LocalDateTime.now(),true,emails,accessTokens,refreshTokens));
                    mailClient.getUserEmails(user.getGoogleID());
                }
                    return new ResponseEntity<>(user.getGoogleID(),HttpStatus.OK);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid ID token.");
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/verifyToken")
    boolean verifyToken(@RequestParam String token) {
        return userService.findUserByGoogleId(Base64.getEncoder().encodeToString(token.getBytes())) != null;
    }

}
