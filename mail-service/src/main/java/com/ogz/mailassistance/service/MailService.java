package com.ogz.mailassistance.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.model.Message;
import com.ogz.mailassistance.repository.MailRepository;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.stream.Collectors;


@Service
public class MailService {

    private final MailRepository repository;
    private final UserServiceClient userServiceClient;

    public MailService(MailRepository repository, UserServiceClient userServiceClient) {
        this.repository = repository;
        this.userServiceClient = userServiceClient;
    }

    public int getUserEmails(User user) throws IOException, GeneralSecurityException {

        GoogleCredential credential = getCredential(user).setAccessToken(user.getAccessToken().get("Google"))
                        .setRefreshToken(user.getRefreshToken().get("Google"));

        final NetHttpTransport httpTransport =  GoogleNetHttpTransport.newTrustedTransport();
        Gmail gmail = new Gmail.Builder(httpTransport, GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("My-Assistant")
                .build();
        String userID = user.getEmailAddresses().get("Google");
        var emailIDs = gmail.users().messages().list(userID).execute().getMessages();
        var emailIdStringList =
                emailIDs.stream().map(com.google.api.services.gmail.model.Message::getId).toList();
        userServiceClient.createAwaitUser(new AwaitUserCreate(user.getId(),emailIdStringList));
        return Math.toIntExact(emailIDs.size());
    }

    public void saveEmail(Message message) {
        repository.save(message);
    }

    public String watch(User user) throws IOException, GeneralSecurityException {
        GoogleCredential credential = getCredential(user).setAccessToken(user.getAccessToken().get("Google")).
                        setRefreshToken(user.getRefreshToken().get("Goggle"));
        Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("My-Assistant")
                .build();
        WatchRequest request = new WatchRequest();
        request.setTopicName("projects/graphic-transit-370816/topics/gmail");
        return gmail.users().watch(user.getEmailAddresses().get("Google"), request).execute().toPrettyString();
    }

    public String unWatch(User user) throws IOException, GeneralSecurityException {
        System.out.println(user.getRefreshToken().get("Google"));
        GoogleCredential credential = getCredential(user).setAccessToken(user.getAccessToken().get("Google")).
                        setRefreshToken(user.getRefreshToken().get("Goggle"));
        Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("My-Assistant")
                .build();
        return gmail.users().stop(user.getEmailAddresses().get("Google")).execute().toString();
    }

    GoogleCredential getCredential(User user) throws IOException, GeneralSecurityException {
        return new GoogleCredential().toBuilder()
                .setClientSecrets(GoogleClientSecrets.load(
                        GsonFactory.getDefaultInstance(),
                        new FileReader("/Users/oguzugurdogan/Documents/my-assistant/user-service/client_secret_912067323625-htgebd2uj0o9i9td64vpus52ibv6731s.apps.googleusercontent.com.json")))
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .addRefreshListener(new CredentialRefreshListener() {
                    @Override
                    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
                        System.out.println("Refresh Token: " + tokenResponse.getRefreshToken());
                        System.out.println("Access Token: " + tokenResponse.getAccessToken());
                        credential.setAccessToken(tokenResponse.getAccessToken());
                        credential.setRefreshToken(tokenResponse.getRefreshToken());
                        //Save Refresh token to DB
                        HashMap<String,String> refreshToken = new HashMap<>();
                        refreshToken.put("token", tokenResponse.getRefreshToken());
                        userServiceClient.reRefreshToken(user.getId(),refreshToken);

                        //Save Access token to DB
                        HashMap<String,String> accessToken = new HashMap<>();
                        accessToken.put("token", tokenResponse.getAccessToken());
                        userServiceClient.reRefreshToken(user.getId(),accessToken);
                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                        throw new RuntimeException(tokenErrorResponse.toPrettyString());
                    }
                }).build();
    }

}
