package org.ogz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReminderDto {

    private String title;
    private String content;
    private LocalDateTime triggerDate;
    private List<String> userList;
}
