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
import com.google.api.services.gmail.model.History;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.ogz.mailassistance.client.UserServiceClient;
import com.ogz.mailassistance.model.MailHistory;
import feign.FeignException;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class GooglePubSubService {

    private final UserServiceClient userServiceClient;
    private final HistoryService historyService;

    private final MailService mailService;

    public GooglePubSubService(UserServiceClient userServiceClient,HistoryService historyService, MailService mailService) {
        this.userServiceClient = userServiceClient;
        this.historyService = historyService;
        this.mailService = mailService;
        System.out.println("Google PUB?SUB Service");
        String projectId = "graphic-transit-370816";
        String subscriptionId = "gmail-sub";

        subscribeAsyncExample(projectId,subscriptionId);

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
                }).build().
                setAccessToken(user.getAccessToken().get("Google")).
                setRefreshToken(user.getRefreshToken().get("Google"));
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
                    System.out.println("User: "+user);
                    if (Objects.isNull(user)) return;
                    MailHistory mailHistory = historyService.getHistoryIdByUserId(user);
                    System.out.println("Mail History: "+mailHistory);
                    if (Objects.isNull(mailHistory)){
                        mailHistory = historyService.createHistory(user, newHistoryId.toString());
                    }
                    try {
                    GoogleCredential credential = getCredential(user);
                    Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                                .setApplicationName("My-Asisstance-Mail-Service")
                                .build();
                    var history =
                            gmail.users().history().list(user.getGmail()).setStartHistoryId(BigInteger.valueOf(Long.parseLong(mailHistory.getMailHistoryId()))).execute();
                        System.out.println("History: "+history.toPrettyString());
                        var historyList = history.getHistory();
                        if (!Objects.isNull(historyList)) {
                            User finalUser = user;
                            historyList.forEach((hist)->{
                                var addedMessages = hist.getMessagesAdded();
                                if (!Objects.isNull(addedMessages))
                                    addedMessages.forEach((addedMessage)->{
                                        mailService.saveMailWithMailId(finalUser,addedMessage.getMessage().getId());
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
                            System.out.println("Liste next page: "+history.getHistory());
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
