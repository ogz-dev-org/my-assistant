package com.ogz.mailassistance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailWithoutDetails {
    private String id;
    private String subject;
    private String fromUser;
    private Date sendingDate;
    private List<String> labels;
}
