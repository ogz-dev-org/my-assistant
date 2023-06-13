package com.ogz.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDto {
    private String toUser;
    private String communicationId;
    private String toGroup;
    private String message;
}
