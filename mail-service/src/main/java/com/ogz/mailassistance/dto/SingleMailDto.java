package com.ogz.mailassistance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.Mail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleMailDto {
    private Mail mail;
}
