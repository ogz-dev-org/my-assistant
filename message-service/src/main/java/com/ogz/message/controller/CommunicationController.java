package com.ogz.message.controller;

import com.ogz.message.dto.ComDtoList;
import com.ogz.message.dto.CommunicationDto;
import com.ogz.message.model.Communication;
import com.ogz.message.service.CommunicationService;
import com.ogz.message.service.MessageService;
import org.ogz.client.UserServiceClient;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/communication")
public class CommunicationController {

    private final UserServiceClient userServiceClient;
    private final CommunicationService communicationService;
    private final MessageService messageService;
    public CommunicationController(UserServiceClient userServiceClient, CommunicationService communicationService, MessageService messageService) {
        this.userServiceClient = userServiceClient;
        this.communicationService = communicationService;
        this.messageService = messageService;
    }


    @PostMapping("/")
    public ResponseEntity<Communication> createCommunication(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @RequestParam String secondUserId){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        System.out.println("FIrst user:" + user.getId());
        System.out.println("Second user:"+secondUserId);
        Communication communication = communicationService.createCommunication(user.getId(),secondUserId);

        return new ResponseEntity<>(communication,HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<ComDtoList> getCommunicationList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<Communication> foundCommunications = communicationService.getAllCommunications(user.getId());
        if (foundCommunications.size() == 0) return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        else {
           var commList = foundCommunications.stream().map((communication)->{
                String otherUserId = null;
                for (String id: communication.getUserIds()) {
                    if (!id.equals(user.getId()))
                        otherUserId = id;
                }
                User otherUser = userServiceClient.findUserById(otherUserId).getBody();
               String lastMessage = "No message";
               LocalDateTime lastMessageDate = null;
               var message = messageService.getLastMessage(communication.getId());
               if (Objects.nonNull(message)){
                   lastMessage = message.getMessage();
                   lastMessageDate = message.getSendDate();
               }

                return new CommunicationDto(otherUser, communication.getId(), lastMessage, lastMessageDate);
            }).toList();
            return new ResponseEntity<>(new ComDtoList(commList),HttpStatus.OK);
        }
    }



}
