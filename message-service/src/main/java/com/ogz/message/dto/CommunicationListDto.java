package com.ogz.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.Message;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunicationListDto {
    private List<Message> list;
}
