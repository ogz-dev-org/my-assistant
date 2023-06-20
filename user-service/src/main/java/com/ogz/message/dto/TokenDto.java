package com.ogz.message.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String token;
    private User user;

}
