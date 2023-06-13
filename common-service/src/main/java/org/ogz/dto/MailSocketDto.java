package org.ogz.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class MailSocketDto {
    private String title;
    private Date sendDate;
    private String mailId;

    private String toUser;

    public MailSocketDto(String title, Date sendDate, String mailId, String toUser) {
        this.title = title;
        this.sendDate = sendDate;
        this.mailId = mailId;
        this.toUser = toUser;
    }

    public MailSocketDto() {
    }
}
