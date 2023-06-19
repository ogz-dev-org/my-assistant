package com.ogz.message.service;

import com.ogz.message.dto.MessageSendDto;
import org.ogz.client.NotificationServiceClient;
import org.ogz.model.Message;
import com.ogz.message.repository.MessageRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final NotificationServiceClient notificationServiceClient;
    private final MessageRepository messageRepository;

    public MessageService(NotificationServiceClient notificationServiceClient, MessageRepository messageRepository) {
        this.notificationServiceClient = notificationServiceClient;
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages(User user){
        return messageRepository.findMessagesByFromUser(user.getId());
    }

    public List<Message> getAllMessagesFromDate(User user,LocalDateTime time){
        return messageRepository.findMessagesByFromUserAndSendDateLessThanEqual(user.getId(), time);
    }

    public Message getMessageById(String id){
        Optional<Message> message = messageRepository.findById(id);
        return message.orElse(null);
    }

    public List<Message> getMessagesByCommunicationId(String communicationId){
        List<Message> messages = messageRepository.findAllByCommunicationId(communicationId);
        Collections.reverse(messages);
        return messages;
    }

    public Message sendMessage(User from, MessageSendDto dto){
        //notificationServiceClient.triggerMessageEvent(message);
        return messageRepository.insert(new Message(null, from.getName()+" "+from.getSurname() ,dto.getMessage(),
                dto.getToUser(), LocalDateTime.now(),dto.getCommunicationId()));
    }

    public Message getLastMessage(String communicationId){
        return messageRepository.findFirstByCommunicationIdOrderBySendDateDesc(communicationId);
    }

}
