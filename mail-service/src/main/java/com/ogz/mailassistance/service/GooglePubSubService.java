package com.ogz.mailassistance.service;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class GooglePubSubService {

    public GooglePubSubService() {
        System.out.println("Google PUB?SUB Service");
        String projectId = "graphic-transit-370816";
        String subscriptionId = "gmail-sub";

        subscribeAsyncExample(projectId,subscriptionId);

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
                    consumer.ack();
                };

        Subscriber subscriber = null;
        subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
        // Start the subscriber.
        subscriber.startAsync().awaitRunning();
        System.out.printf("Listening for messages on %s:\n", subscriptionName);
//        try {
//
//            // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
//            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
//        } catch (TimeoutException timeoutException) {
//            // Shut down the subscriber after 30s. Stop receiving messages.
//            subscriber.stopAsync();
//        }
    }

}
