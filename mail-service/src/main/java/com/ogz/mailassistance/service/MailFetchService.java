package com.ogz.mailassistance.service;

import com.ogz.mailassistance.client.UserServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailFetchService {

    private final UserServiceClient userServiceClient;

    public MailFetchService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Scheduled(fixedDelay = 1000*5)
    void fetchAwaitUserMails(){
//        System.out.println("Bekleten kullanicinin emailleri cekilecek.");
//        if (userServiceClient.getFirstAwaitUser().getStatusCode() == HttpStatus.NO_CONTENT) System.out.println(
//                "Bekleyen bir kullanici yok");
//        System.out.println(userServiceClient.getFirstAwaitUser().getBody().toString());
    }

}
