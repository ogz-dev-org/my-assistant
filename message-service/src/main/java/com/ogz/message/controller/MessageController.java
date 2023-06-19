package com.ogz.message.controller;

import com.ogz.message.dto.CommunicationListDto;
import com.ogz.message.dto.MessageSendDto;
import com.ogz.message.service.MessageService;
import org.ogz.client.NotificationServiceClient;
import org.ogz.client.UserServiceClient;
import org.ogz.model.Message;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {


    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    private final MessageService messageService;

    public MessageController(UserServiceClient userServiceClient, MessageService messageService, NotificationServiceClient notificationServiceClient) {
        this.userServiceClient = userServiceClient;
        this.notificationServiceClient = notificationServiceClient;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Message>> getAllMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(messageService.getAllMessages(user), HttpStatus.OK);
    }

    @GetMapping("/communication/{communicationId}")
    public ResponseEntity<CommunicationListDto> getAllMessagesByCommunication(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                              @PathVariable String communicationId) {
        return new ResponseEntity<>(new CommunicationListDto(messageService.getMessagesByCommunicationId(communicationId)),HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<Message>> getAllMessagesByDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @RequestParam LocalDateTime time) {
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(messageService.getAllMessagesFromDate(user,time), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable String id) {
        Message message = messageService.getMessageById(id);
        if (Objects.isNull(message)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Message> sendMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody MessageSendDto dto){
        User user = userServiceClient.findUserByGoogleId(token).getBody();
        if (Objects.isNull(user)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Message sendedMessage = messageService.sendMessage(user,dto);
        notificationServiceClient.triggerMessageEvent(sendedMessage);
        return new ResponseEntity<>(sendedMessage,HttpStatus.OK);
    }


}
