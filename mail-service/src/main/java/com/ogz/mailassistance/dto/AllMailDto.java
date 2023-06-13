package com.ogz.mailassistance.dto;

import org.ogz.model.Mail;

import java.util.List;

public class AllMailDto {
    private List<Mail> mails;

    public AllMailDto(List<Mail> mails) {
        this.mails = mails;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }
}
