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
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.ogz.mailassistance.model.MailHistory;
import feign.FeignException;
import org.ogz.client.NotificationServiceClient;
import org.ogz.client.UserServiceClient;
import org.ogz.constants.PubSub;
import org.ogz.dto.MailSocketDto;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Objects;

import static org.ogz.constants.Secrets.CLIENT_SECRET_FILE;

@Service
public class GooglePubSubService {

    private final UserServiceClient userServiceClient;
    private final HistoryService historyService;

    private final NotificationServiceClient notificationServiceClient;

    private final MailService mailService;

    public GooglePubSubService(UserServiceClient userServiceClient, HistoryService historyService, NotificationServiceClient notificationServiceClient, MailService mailService) {
        this.userServiceClient = userServiceClient;
        this.historyService = historyService;
        this.notificationServiceClient = notificationServiceClient;
        this.mailService = mailService;
        System.out.println("Google PUB/SUB Service");
        subscribeAsyncExample(PubSub.PROJECT_ID,PubSub.SUBSCRIPTION_ID);

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
                            userServiceClient.reRefreshToken(user.getId(),tokenResponse.getRefreshToken());
                        }

                        if (Objects.nonNull(tokenResponse.getAccessToken())) {
                            credential.setAccessToken(tokenResponse.getAccessToken());

                            //Save Access token to DB
                            userServiceClient.reAccessToken(user.getId(),  tokenResponse.getAccessToken());
                        }
                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                        System.out.println(tokenErrorResponse.toPrettyString());
                        throw new RuntimeException(tokenErrorResponse.toPrettyString());
                    }
                }).build().
                setAccessToken(user.getAccessToken().getGoogle()).
                setRefreshToken(user.getRefreshToken().getGoogle());
    }

    String parseEmail(String data){
        int index = data.indexOf(",");
        return data.substring(17,index-1);
    }
    BigInteger parseHistoryId(String data){
        String rightPart = data.split(",")[1];
        rightPart = rightPart.replaceAll("}","");
        int index = rightPart.indexOf(":");
        return BigInteger.valueOf(Long.parseLong(rightPart.substring(index+1)));
    }
    void subscribeAsyncExample(String projectId, String subscriptionId) {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriptionId);

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    // Handle incoming message, then ack the received message.
                    String data =  message.getData().toStringUtf8();
                    BigInteger newHistoryId = parseHistoryId(data);
                    User user = null;
                   try {
                       user =
                               userServiceClient.findUserByGmail(parseEmail(data)).getBody();
                   }catch (FeignException feignException){
                       switch (feignException.status()) {
                           case (503) -> System.out.println("Mikro servise ulasilamiyor.");
                           case (500) -> System.out.println("Istek atilan mikro servise ulasilamiyor.");
                       }
                   }
//                    System.out.println("User: "+user);
                    if (Objects.isNull(user)) return;
                    MailHistory mailHistory = historyService.getHistoryIdByUserId(user);
//                    System.out.println("Mail History: "+mailHistory);
                    if (Objects.isNull(mailHistory)){
                        mailHistory = historyService.createHistory(user, newHistoryId.toString());
                    }
                    try {
                    GoogleCredential credential = getCredential(user);
        //TODO: GUTIL ile gmail olustur
                    Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                                .setApplicationName("My-Asisstance-Mail-Service")
                                .build();
                    var history =
                            gmail.users().history().list(user.getGmail()).setStartHistoryId(BigInteger.valueOf(Long.parseLong(mailHistory.getMailHistoryId()))).execute();
//                        System.out.println("History: "+history.toPrettyString());
                        var historyList = history.getHistory();
                        if (!Objects.isNull(historyList)) {
                            User finalUser = user;
                            historyList.forEach((hist)->{
                                var addedMessages = hist.getMessagesAdded();
                                if (!Objects.isNull(addedMessages))
                                    addedMessages.forEach((addedMessage)->{
                                        var mail = mailService.saveMailWithMailId(finalUser,addedMessage.getMessage().getId());
                                        notificationServiceClient.triggerMailEvent(new MailSocketDto(mail.getSubject(),mail.getSendingDate(),mail.getId(),mail.getToUser()));
                                    });
                                var deletedMessages = hist.getMessagesDeleted();
                                if (!Objects.isNull(deletedMessages) )
                                    deletedMessages.forEach((removedMessage)->{
                                        mailService.deleteMailWithMailId(finalUser,removedMessage.getMessage().getId());
                                    });
                            });
                        }
                        while(!Objects.isNull(history.getNextPageToken())){
                            history =
                                    gmail.users().history().list(user.getGmail()).setPageToken(history.getNextPageToken()).execute();
//                            System.out.println("Liste next page: "+history.getHistory());
                        }
                        historyService.updateMailHistory(mailHistory.getId(), String.valueOf(history.getHistoryId()));
                    } catch (IOException | GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    }
                    consumer.ack();
                };

        Subscriber subscriber = null;
        subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
        // Start the subscriber.
        subscriber.startAsync().awaitRunning();
        System.out.printf("Listening for messages on %s:\n", subscriptionName);

    }

}
