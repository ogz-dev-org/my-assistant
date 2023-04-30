package com.ogz.mailassistance.service;

import com.ogz.mailassistance.model.MailHistory;
import com.ogz.mailassistance.repository.HistoryRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public MailHistory getHistoryIdByUserId(User user){
        System.out.println(user.toString());
        return historyRepository.findAllByUserIdEquals(user.getId());
    }

    public MailHistory createHistory(User user,String historyId){
        return historyRepository.insert(new MailHistory(user.getId(),historyId));
    }

    public MailHistory updateMailHistory(String id,String mailHistoryId){
        Optional<MailHistory> optionalMailHistory =  historyRepository.findById(id);
        if (optionalMailHistory.isEmpty())
            return null;
        MailHistory history = optionalMailHistory.get();
        history.setMailHistoryId(mailHistoryId);
        return historyRepository.save(history);
    }

}
