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
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.WatchRequest;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.dto.SendMailDto;
import com.ogz.mailassistance.model.Mail;
import com.ogz.mailassistance.repository.MailRepository;
import com.ogz.mailassistance.utils.Date;
import com.ogz.mailassistance.utils.UGmail;
import org.apache.commons.codec.binary.Base64;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
                        if (Objects.nonNull(tokenResponse.getRefreshToken())){
                            credential.setRefreshToken(tokenResponse.getRefreshToken());

                            //Save Refresh token to DB
                            HashMap<String,String> refreshToken = new HashMap<>();
                            refreshToken.put("token", tokenResponse.getRefreshToken());
                            userServiceClient.reRefreshToken(user.getId(),refreshToken);
                        }

                        if (Objects.nonNull(tokenResponse.getAccessToken())) {
                            credential.setAccessToken(tokenResponse.getAccessToken());

                            //Save Access token to DB
                            HashMap<String, String> accessToken = new HashMap<>();
                            accessToken.put("token", tokenResponse.getAccessToken());
                            userServiceClient.reAccessToken(user.getId(), accessToken);
                        }
                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                        System.out.println(tokenErrorResponse.toPrettyString());
                        throw new RuntimeException(tokenErrorResponse.toPrettyString());
                    }
                }).build().setAccessToken(user.getAccessToken().get("Google")).setRefreshToken(user.getAccessToken().get(
                        "Google"));
    }

    public List<Mail> getAllUserMails(User user) {
        return repository.findAllByFromUser(user.getId());
    }

    public List<Mail> getUserMailsFromLastDate(User user, LocalDateTime lastDate){
        return repository.findAllByFromUserAndSendingDateGreaterThan(user.getId(), lastDate);
    }

    public Mail sendMail(SendMailDto mailDto,User user) throws IOException, MessagingException, GeneralSecurityException {
        GoogleCredential credential = getCredential(user);

        Gmail gmail = UGmail.get(credential);

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

        return new Mail(java.util.Base64.getEncoder().encodeToString(mailDto.getContent().getBytes()), mailDto.getTitle(),user.getEmailAddresses().get("Google"),
                mailDto.getToUserList().get(0),
                LocalDateTime.now(),null);
    }

    public void saveMailWithMailId(User user, String id){
        try {
            GoogleCredential credential = getCredential(user);
            Gmail gmail = UGmail.get(credential);
            Message mail = gmail.users().messages().get(user.getEmailAddresses().get("Google"),id).execute();
            MessagePart messagePart = mail.getPayload();
            var headers = messagePart.getHeaders();
            String content = "";
            String subject = "";
            String fromUser = "";
            LocalDateTime sendingDate = null;
            for (var a:headers) {
                if (a.get("name").equals("From")){
                    String unFormattedUser = (String) a.get("value");
                    int start = unFormattedUser.indexOf("<");
                    int end = unFormattedUser.indexOf(">");
                    fromUser = unFormattedUser.substring(start+1,end);
                } else if (a.get("name").equals("Date")) {
                    String date = (String) a.get("value");
                    String[] splittedDate = date.split(" ");
                    String[] splittedTime = null;
                    if (date.contains(",")){
                        splittedTime = splittedDate[4].split(":");
                        sendingDate = LocalDateTime.of(Integer.parseInt(splittedDate[3]), Date.monthNum(splittedDate[2]),
                                Integer.parseInt(splittedDate[1]),
                                Integer.parseInt(splittedTime[0]),Integer.parseInt(splittedTime[1]),
                                Integer.parseInt(splittedTime[2]));
                    }else{
                        splittedTime = splittedDate[3].split(":");
                        sendingDate = LocalDateTime.of(Integer.parseInt(splittedDate[2]),
                                Date.monthNum(splittedDate[1]),
                                Integer.parseInt(splittedDate[0]),
                                Integer.parseInt(splittedTime[0]),
                                Integer.parseInt(splittedTime[1]),
                                Integer.parseInt(splittedTime[2]));
                    }


                    System.out.println("ID: "+id);

                } else if (a.get("name").equals("Subject")) {
                    subject = (String) a.get("value");
                }
            }

            int bodySize = messagePart.getBody().getSize();
            if (bodySize > 0) {
                content = messagePart.getBody().getData();
            }else {
                var parts = messagePart.getParts();
                int biggestIndex = -1;
                int biggestSize = 0;
                int index = 0;
                for (var part:parts) {
                    int partSize = part.getBody().getSize();
                    if (partSize > biggestSize) {
                        biggestIndex = index;
                        biggestSize = partSize;
                    }
                    index++;
                }
                if (biggestIndex >= 0)
                    content = parts.get(biggestIndex).getBody().getData();
            }

            Mail newMail = new Mail(content,subject,fromUser,user.getId(),sendingDate,id);
            repository.insert(newMail);
            // Send mail event notification
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMailWithMailId(User user, String id){
       Mail findedMail = repository.findByOriginalMailIdEquals(id);
       if (Objects.nonNull(findedMail))
           repository.deleteById(findedMail.getId());

    }

}
