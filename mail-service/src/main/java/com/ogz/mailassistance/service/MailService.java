package com.ogz.mailassistance.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Scheduled(fixedDelay = 3600 * 24)
    private void refreshSubscriptions() {
//        System.out.println("Refresh Subscriptions");
    }

    @Scheduled(fixedDelay = 1000)
    private void checkEmailFetchAwaitUsers(){
        //This scheduled service will be use for fetch all user emails from gmail.
    }

}
