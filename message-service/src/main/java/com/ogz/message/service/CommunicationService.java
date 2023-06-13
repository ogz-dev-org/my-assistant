package com.ogz.message.service;

import com.ogz.message.model.Communication;
import com.ogz.message.repository.CommunicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationService {

    private final CommunicationRepository repository;


    public CommunicationService(CommunicationRepository repository) {
        this.repository = repository;
    }

    public Communication createCommunication(String firstUser, String secondUser){
        return repository.insert(new Communication(null, List.of(firstUser,secondUser)));
    }

    public List<Communication> getAllCommunications(String userId){
        return repository.findByListContainingElement(userId);
    }

}
