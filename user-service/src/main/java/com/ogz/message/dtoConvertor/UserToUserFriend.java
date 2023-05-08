package com.ogz.message.dtoConvertor;

import com.ogz.message.dto.UserFriendDto;
import org.ogz.model.User;

public class UserToUserFriend {

    public static UserFriendDto convert(User user){
        return new UserFriendDto(user.getId(), user.getName(), user.getSurname(), user.getGmail(),
                user.getRegisterDate());
    }

}
