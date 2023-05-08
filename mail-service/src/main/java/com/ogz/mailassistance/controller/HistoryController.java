package com.ogz.mailassistance.controller;

import com.ogz.mailassistance.dto.HistoryDto;
import com.ogz.mailassistance.model.MailHistory;
import com.ogz.mailassistance.service.HistoryService;
import org.ogz.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController("/api/v1/history")
public class HistoryController {

    private final HistoryService historyService;
    private final UserServiceClient userServiceClient;

    public HistoryController(HistoryService historyService, UserServiceClient userServiceClient) {
        this.historyService = historyService;
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("/")
    public ResponseEntity<HistoryDto> getUserMailHistoryId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        MailHistory mailHistory = historyService.getHistoryIdByUserId(user);
        return new ResponseEntity<>(new HistoryDto(mailHistory.getMailHistoryId()), HttpStatus.NO_CONTENT);
    }

}
