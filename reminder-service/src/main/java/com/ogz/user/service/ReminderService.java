package com.ogz.user.service;

import com.ogz.user.client.NotificationServiceClient;
import com.ogz.user.dto.ReminderEventDto;
import com.ogz.user.model.Reminder;
import com.ogz.user.repository.ReminderRepository;
import org.ogz.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            notificationServiceClient.triggerReminderEvent(new ReminderEventDto(reminder.getCreatorId(),
                    reminder.getUserList(),reminder.getTitle(),reminder.getContent(),reminder.getId()));
        });

    }

    public List<Reminder> getAllReminders(User user){
        return reminderRepository.findAllByCreatorId(user.getId());
    }

    public Reminder getReminder(String id){
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        return optionalReminder.orElse(null);
    }

    public Reminder createReminder(String title, String content, String triggerDate,String creatorId,
                                   List<String> userList){
        LocalDateTime time = LocalDateTime.now().plusMinutes(1);
        return reminderRepository.insert(new Reminder(title,content,LocalDateTime.now(),LocalDateTime.now(),
               LocalDateTime.of(time.getYear(),time.getMonthValue(),time.getDayOfMonth(), time.getHour(),
                       time.getMinute(),0) ,
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
