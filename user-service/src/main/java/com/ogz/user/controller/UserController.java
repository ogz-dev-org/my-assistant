package com.ogz.user.controller;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import com.ogz.user.constants.ClientID;

import static com.ogz.user.constants.ClientID.IOSClientID;
import static com.ogz.user.constants.ClientID.WebAppClientID;
import static com.ogz.user.constants.Secrets.CLIENT_SECRET_FILE;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello To User Service", HttpStatus.OK);
    }

    @PostMapping("/verifyUser")
    ResponseEntity<List<Message>> verifyUser(@RequestHeader Map<String, String> headers){
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
                String accessToken = tokenResponse.getAccessToken();
                System.out.println(accessToken);
                System.out.println("Scopelar: " + tokenResponse.getScope());
                // Use access token to call API
                GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                System.out.println(credential);
                System.out.println("User Access:"+ credential.getAccessToken());
                System.out.println("Scopes"+ credential.getServiceAccountScopesAsString());
                Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Auth Code Exchange Demo")
                        .build();
                var deneme = gmail.users().messages().list(idToken.getPayload().getEmail()).execute();
                var liste = deneme.getMessages();
                System.out.println("Emails: " + liste);
                var test = new ResponseEntity<>(liste,HttpStatus.OK);
                System.out.println(test.getBody());
                return test;
                // Use or store profile information
                // ...
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            // Get profile information from payload
//            String email = payload.getEmail();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");
//            System.out.println("User ID: " + payload.toString());
        } else {
            System.out.println("Invalid ID token.");
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
        //Verify User
        // If user verified +
        //  If User Is Not Saved To DB Save -
        //  Else Check If Access Token is exprired -
        //      If Access is Expired -
        //          Get New Access Token with Refresh Token -
    }

}
