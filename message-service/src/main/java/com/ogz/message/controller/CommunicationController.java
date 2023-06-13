package com.ogz.message.controller;

import com.ogz.message.model.Communication;
import com.ogz.message.service.CommunicationService;
import org.ogz.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController("/api/v1/communication")
public class CommunicationController {

    private final UserServiceClient userServiceClient;
    private final CommunicationService communicationService;

    public CommunicationController(UserServiceClient userServiceClient, CommunicationService communicationService) {
        this.userServiceClient = userServiceClient;
        this.communicationService = communicationService;
    }


    @PostMapping("/")
    public ResponseEntity<Communication> createCommunication(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             String secondUserId){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Communication communication = communicationService.createCommunication(user.getId(),secondUserId);

        return new ResponseEntity<>(communication,HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<List<Communication>> getCommunicationList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Communication> foundCommunications = communicationService.getAllCommunications(user.getId());
        if (foundCommunications.size() == 0) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(foundCommunications,HttpStatus.OK);
    }

}
