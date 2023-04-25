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

    public GooglePubSubService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
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
                        userServiceClient.reAccessToken(user.getId(),accessToken);
                    }

                    @Override
                    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                        System.out.println(tokenErrorResponse.toPrettyString());
                        throw new RuntimeException(tokenErrorResponse.toPrettyString());
                    }
                }).build();
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
                    System.out.println("Id: " + message.getMessageId());
                    System.out.println("Data: " + message.getData().toStringUtf8());
                    //TODO: Get History id
                    // Get all actions happened in that history
                    // Add all messages and delete all messages from db.
                    String data =  message.getData().toStringUtf8();

                    User user =
                            userServiceClient.findUserByEmail(parseEmail(data)).getBody();
                    if (Objects.isNull(user)) return;
                    try {
                    GoogleCredential credential = getCredential(user).
                                setAccessToken(user.getAccessToken().get("Google")).
                                setRefreshToken(user.getRefreshToken().get("Google"));
                    Gmail gmail = new Gmail.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                                .setApplicationName("My-Asisstance-Mail-Service")
                                .build();
                        System.out.println("History Id: "+parseHistoryId(data));
                    var history = gmail.users().history().list(user.getEmailAddresses().get(
                            "Google")).setStartHistoryId(parseHistoryId(data)).execute();
                        System.out.println("Liste: "+history.toPrettyString());
                        while(!Objects.isNull(history.getNextPageToken())){
                            history = gmail.users().history().list(user.getEmailAddresses().get(
                                    "Google")).setPageToken(history.getNextPageToken()).execute();
                            System.out.println("Liste next page: "+history.getHistory());
                        }
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
