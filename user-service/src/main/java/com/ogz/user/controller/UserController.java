package com.ogz.user.controller;


import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.user.clients.MailServiceClient;
import com.ogz.user.dto.LoginHeaderDto;
import com.ogz.user.dto.TokenDto;
import com.ogz.user.service.UserService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

import static org.ogz.constants.ClientID.IOSClientID;
import static org.ogz.constants.ClientID.WebAppClientID;
import static org.ogz.constants.Secrets.CLIENT_SECRET_FILE;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final MailServiceClient mailClient;

    public UserController(UserService userService, MailServiceClient mailClient) {

        this.userService = userService;

        this.mailClient = mailClient;
    }
    @Hidden
    @GetMapping("/")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello from User Service",HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/byId/{id}")
    ResponseEntity<User> findUserById(@PathVariable String id){
        return new ResponseEntity<>( userService.findUserById(id), HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/{googleId}")
    ResponseEntity<User> findUserByGoogleId(@PathVariable String googleId){
        return new ResponseEntity<>( userService.findUserByGoogleId(googleId), HttpStatus.OK);
    }

    @GetMapping("/email/gmail/{mail}")
    ResponseEntity<User> findUserByGmail(@PathVariable String mail){
        return new ResponseEntity<>( userService.findUserByGmail(mail), HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<TokenDto> login(@RequestHeader("X-IdToken") String idTokenString,
                                        @RequestHeader("X-AuthToken") String authCode) throws IOException, GeneralSecurityException {
        System.out.println("Login Olmaya calisiyor");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(WebAppClientID, IOSClientID))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException  | IllegalArgumentException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
                System.out.println(clientSecrets.getDetails().toPrettyString());
                GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        clientSecrets.getDetails().getClientId(),
                        clientSecrets.getDetails().getClientSecret(),
                        authCode,
                        "")
                        .execute();
                GoogleCredential credential = new GoogleCredential().toBuilder()
                        .setClientSecrets(GoogleClientSecrets.load(
                                GsonFactory.getDefaultInstance(),
                                new FileReader(CLIENT_SECRET_FILE)))
                        .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                        .setJsonFactory(GsonFactory.getDefaultInstance())
                        .build().setAccessToken(tokenResponse.getAccessToken()).
                                                                     setRefreshToken(tokenResponse.getRefreshToken());
                Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                        .setApplicationName("My-Asisstance-Mail-Service")
                        .build();
                WatchRequest request = new WatchRequest();
                request.setTopicName("projects/graphic-transit-370816/topics/gmail");
                gmail.users().watch(payload.getEmail(), request).execute();
                //gmail.users().stop(payload.getEmail());
                User user = userService.findUserByGoogleId(new String(Base64.getEncoder().encode(userId.getBytes())));
                if (Objects.isNull(user)) {
                    HashMap<String, String> accessTokens = new HashMap<>();
                    accessTokens.put("Google",tokenResponse.getAccessToken());
                    HashMap<String, String> refreshTokens = new HashMap<>();
                    refreshTokens.put("Google", tokenResponse.getRefreshToken());
                    user = userService.addUser(new User(Base64.getEncoder().encodeToString(userId.getBytes()),
                            (String) payload.get("given_name"),(String) payload.get("family_name"), (String) payload.get("picture"),
                            LocalDateTime.now(),true,accessTokens,refreshTokens,payload.getEmail()));
                    try {
                        mailClient.getUserEmails(user.getGoogleID());
                    } catch (FeignException feignException){
                        switch (feignException.status()) {
                            case (503) -> System.out.println("Mikro servise ulasilamiyor.");
                            case (500) -> System.out.println("Istek atilan mikro servise ulasilamiyor.");
                        }
                    }
                }
                    return new ResponseEntity<>(new TokenDto(user.getGoogleID()),HttpStatus.OK);
            } catch (IOException | GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid ID token.");
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }

    @Hidden
    @GetMapping("/verifyToken")
    boolean verifyToken(@RequestParam String token) {
        return userService.findUserByGoogleId(Base64.getEncoder().encodeToString(token.getBytes())) != null;
    }

    @GetMapping("/all")
    ResponseEntity<List<User>> findAllUsers(){
        List<User> userList = userService.findAll();
        if (Objects.isNull(userList) || userList.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/refreshToken/{id}")
    ResponseEntity<User> reRefreshToken(@PathVariable String id, @RequestBody HashMap body) {
        User user = userService.reRefreshToken(id, String.valueOf(body.get("token")));
        if (Objects.isNull(user))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @Hidden
    @PostMapping("/accessToken/{id}")
    ResponseEntity<User> reAccessToken(@PathVariable String id, @RequestBody HashMap body) {
        User user = userService.reAccessToken(id, String.valueOf(body.get("token")));
        if (Objects.isNull(user))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<User>> searchUsers(@RequestParam String search){
        List<User> userList = userService.searchUsers(search);
        if (Objects.isNull(userList) || userList.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }
}
