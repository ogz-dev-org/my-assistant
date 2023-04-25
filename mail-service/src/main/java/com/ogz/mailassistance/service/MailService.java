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
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.dto.SendMailDto;
import com.ogz.mailassistance.model.Mail;
import com.ogz.mailassistance.repository.MailRepository;
import org.apache.commons.codec.binary.Base64;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

import static org.ogz.constants.Secrets.CLIENT_SECRET_FILE;


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

    public void saveEmail(Mail mail) {
        repository.save(mail);
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
        return Objects.isNull(gmail.users().stop(user.getEmailAddresses().get("Google")).execute())?"false":"true";
    }

    GoogleCredential getCredential(User user) throws IOException, GeneralSecurityException {
        return new GoogleCredential().toBuilder()
                .setClientSecrets(GoogleClientSecrets.load(
                        GsonFactory.getDefaultInstance(),
                        new FileReader(CLIENT_SECRET_FILE)))
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
                        System.out.println(tokenErrorResponse.toPrettyString());
                        throw new RuntimeException(tokenErrorResponse.toPrettyString());
                    }
                }).build();
    }

    public List<Mail> getAllUserMails(User user) {
        return repository.findAllByFromUser(user.getId());
    }

    public List<Mail> getUserMailsFromLastDate(User user, LocalDateTime lastDate){
        return repository.findAllByFromUserAndSendingDateGreaterThan(user.getId(), lastDate);
    }

    public Mail sendMail(SendMailDto mailDto,User user) throws IOException, MessagingException, GeneralSecurityException {
        GoogleCredential credential = getCredential(user);

        Gmail gmail = new Gmail.Builder(new NetHttpTransport(),GsonFactory.getDefaultInstance(),credential).build();

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(user.getEmailAddresses().get("Google")));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(mailDto.getToUserList().get(0)));
        email.setSubject(mailDto.getTitle());
        email.setText(mailDto.getContent());

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        gmail.users().messages().send("me",message).execute();

        return new Mail(mailDto.getContent(), user.getEmailAddresses().get("Google"),mailDto.getToUserList().get(0),
                LocalDateTime.now());
    }

}
