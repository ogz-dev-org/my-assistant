package com.ogz.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendDto {
    private String id;
    private String name;
    private String surname;
    private String avatarUrl;
    private String gmail;
    private LocalDateTime registerDate;

}
