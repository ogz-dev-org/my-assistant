package com.ogz.message.dto;

import com.ogz.message.model.Communication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.User;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationDto {
    private User otherUser;
    private String communicationId;
    private String lastMessage;
    private LocalDateTime lastMessageDate;
}
