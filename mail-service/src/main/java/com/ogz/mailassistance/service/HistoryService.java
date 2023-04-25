package com.ogz.mailassistance.service;

import com.ogz.mailassistance.model.MailHistory;
import com.ogz.mailassistance.repository.HistoryRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public MailHistory getHistoryIdByUserId(User user){
        return historyRepository.findAllByUserIdEquals(user.getId());
    }

}
