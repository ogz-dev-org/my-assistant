package org.ogz.dto;

import java.time.LocalDateTime;

public class MailSocketDto {
    private String title;
    private LocalDateTime sendDate;
    private String mailId;

    private String toUser;

    public MailSocketDto(String title, LocalDateTime sendDate, String mailId, String toUser) {
        this.title = title;
        this.sendDate = sendDate;
        this.mailId = mailId;
        this.toUser = toUser;
    }

    public MailSocketDto() {
    }
}
