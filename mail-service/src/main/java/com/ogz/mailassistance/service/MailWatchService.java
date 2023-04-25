package com.ogz.mailassistance.service;

import com.ogz.mailassistance.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;

@Service
public class MailWatchService {

    private final MailService mailService;
    private final UserServiceClient userServiceClient;

    public MailWatchService(MailService mailService, UserServiceClient userServiceClient) {
        this.mailService = mailService;
        this.userServiceClient = userServiceClient;
    }

    @Scheduled(fixedDelay = 1000*60*60*24)
    private void WatchReset(){
        List<User> userList =  userServiceClient.findAllUsers().getBody();
        if (Objects.isNull(userList) || userList.size() == 0) {
            System.out.println("There is no user to unWatch or watch");
            return;
        }

//        userList.forEach((user)->{
//            try {
//                mailService.unWatch(user);
//                mailService.watch(user);
//            } catch (IOException | GeneralSecurityException e) {
//                throw new RuntimeException(e);
//            }
//        });

    }

}
