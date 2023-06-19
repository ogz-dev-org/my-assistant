package com.ogz.mailassistance.dto;

import org.ogz.model.Mail;

public class MailToUndetailedMailConverter {
    public static MailWithoutDetails convert(Mail mail){
        return MailWithoutDetails.builder().id(mail.getId()).subject(mail.getSubject()).labels(mail.getLabels()).sendingDate(mail.getSendingDate()).fromUser(mail.getFromUser()).build();
    }
}
