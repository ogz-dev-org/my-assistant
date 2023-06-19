package com.ogz.reminder.service;

import org.ogz.dto.ReminderEventDto;
import com.ogz.reminder.model.Reminder;
import com.ogz.reminder.repository.ReminderRepository;
import org.ogz.client.NotificationServiceClient;
import org.ogz.model.User;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final NotificationServiceClient notificationServiceClient;

    public ReminderService(ReminderRepository reminderRepository, NotificationServiceClient notificationServiceClient) {
        this.reminderRepository = reminderRepository;
        this.notificationServiceClient = notificationServiceClient;
    }

    @Scheduled(cron = "0 * * * * *")
    private void fetchReminders(){
        System.out.println("Fetching");
        System.out.println(LocalDateTime.now());
        List<Reminder> currentReminders = getAllRemindersWhoseTimeHasCome();
        System.out.println(currentReminders);
        currentReminders.forEach((reminder)->{
            checkReminder(reminder.getId());
            notificationServiceClient.triggerReminderEvent(new ReminderEventDto(reminder.getCreatorId(),
                    reminder.getUserList(),reminder.getTitle(),reminder.getContent(),reminder.getId()));
        });

    }

    public List<Reminder> getAllReminders(User user){
        List<Reminder> reminders = reminderRepository.findAllByCreatorId(user.getId());
        Collections.reverse(reminders);
        return reminders;
    }

    public Reminder getReminder(String id){
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        return optionalReminder.orElse(null);
    }

    public Reminder createReminder(String title, String content, LocalDateTime triggerDate, String creatorId,
                                   List<String> userList){
        return reminderRepository.insert(new Reminder(title,content,LocalDateTime.now(),LocalDateTime.now(),
               LocalDateTime.of(triggerDate.getYear(),triggerDate.getMonthValue(),triggerDate.getDayOfMonth(), triggerDate.getHour(),
                       triggerDate.getMinute(),0) ,
                creatorId,userList,false));
    }

    public List<Reminder> getAllRemindersWhoseTimeHasCome(){
        LocalDateTime currentTime = LocalDateTime.now();

        return reminderRepository.findAllByTriggerDateLessThanEqualAndIsTriggeredFalse(LocalDateTime.of(currentTime.getYear(),
                currentTime.getMonthValue(),currentTime.getDayOfMonth(),currentTime.getHour(),currentTime.getMinute()
                ,0));
    }

    public void checkReminder(String id) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            reminder.setTriggered(true);
            reminderRepository.save(reminder);
        }
    }

    public void deleteReminder(String id){
        reminderRepository.deleteById(id);
    }

}
