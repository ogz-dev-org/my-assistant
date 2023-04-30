package com.ogz.mailassistance.service;

import com.ogz.mailassistance.client.UserServiceClient;
import org.apache.commons.lang.SerializationUtils;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MailFetchService {

    private final UserServiceClient userServiceClient;

    private final MailService mailService;

    public MailFetchService(UserServiceClient userServiceClient, MailService mailService) {
        this.userServiceClient = userServiceClient;
        this.mailService = mailService;
    }

    @Scheduled(fixedDelay = 1000*5)
    void fetchAwaitUserMails(){
        var awaitUserRequest = userServiceClient.getFirstAwaitUser();
        if (awaitUserRequest.getStatusCode() == HttpStatus.NO_CONTENT){
            System.out.println("Bekleyen bir kullanici yok");
            return;
        }
        AwaitUser awaitUser = awaitUserRequest.getBody();
        if (Objects.isNull(awaitUser)) return;
        if (Objects.isNull(awaitUser.getMailIds())) return;
        AwaitUser copyUser = new AwaitUser(awaitUser);
        User user = userServiceClient.findUserById(awaitUser.getUserId()).getBody();
        if (Objects.isNull(user)) {
            userServiceClient.deleteAwaitUser(awaitUser.getId());
            return;
        }
        for (int i = 0; i < awaitUser.getMailIds().size(); i++) {
            mailService.saveMailWithMailId(user,awaitUser.getMailIds().get(i));
            var newMails = copyUser.getMailIds();
            newMails.remove(0);
            copyUser.setMailIds(newMails);
            userServiceClient.updateAwaitUser(copyUser);
        }
        userServiceClient.deleteAwaitUser(awaitUser.getId());
    }

}
