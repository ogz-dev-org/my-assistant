package com.ogz.user.service;

import com.ogz.user.model.Reminder;
import com.ogz.user.repository.ReminderRepository;
import org.ogz.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    private void fetchReminders(){
        System.out.println("Fetching");
        System.out.println(LocalDateTime.now());

    }

    public List<Reminder> getAllReminders(User user){
        return reminderRepository.findAllByCreatorId(user.getId());
    }

    public Reminder createReminder(String title, String content, LocalDateTime triggerDate,String creatorId,
                                   List<String> userList){
        return reminderRepository.insert(new Reminder(title,content,LocalDateTime.now(),LocalDateTime.now(),
                triggerDate,creatorId,userList));
    }

    public List<Reminder> getAllRemindersWhoseTimeHasCome(){
        LocalDateTime currentTime = LocalDateTime.now();

        return reminderRepository.findAllByTriggerDateLessThanEqual(LocalDateTime.of(currentTime.getYear(),
                currentTime.getMonthValue(),currentTime.getDayOfMonth(),currentTime.getHour(),currentTime.getMinute()
                ,0));
    }

}
