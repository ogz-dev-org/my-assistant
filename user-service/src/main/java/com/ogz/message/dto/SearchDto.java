package com.ogz.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ogz.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {
    private List<User> users;

}
