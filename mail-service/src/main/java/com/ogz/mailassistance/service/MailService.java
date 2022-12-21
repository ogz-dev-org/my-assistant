package com.ogz.mailassistance.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Scheduled(fixedDelay = 3600 * 24)
    private void refreshSubscriptions() {
//        System.out.println("Refresh Subscriptions");
    }

}
