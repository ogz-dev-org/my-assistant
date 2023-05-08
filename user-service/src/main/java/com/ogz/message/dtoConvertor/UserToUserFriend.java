package com.ogz.user.dtoConvertor;

import com.ogz.user.dto.UserFriendDto;
import org.ogz.model.User;

public class UserToUserFriend {

    public static UserFriendDto convert(User user){
        return new UserFriendDto(user.getId(), user.getName(), user.getSurname(), user.getGmail(),
                user.getRegisterDate());
    }

}
