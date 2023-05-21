package com.ogz.message.service;

import com.ogz.message.dto.UserFriendDto;
import com.ogz.message.dtoConvertor.UserToUserFriend;
import com.ogz.message.model.Friends;
import com.ogz.message.repository.FriendsRepository;
import org.ogz.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendsService {
    private final UserService userService;
    private final FriendsRepository friendsRepository;

    public FriendsService(UserService userService, FriendsRepository friendsRepository) {
        this.userService = userService;
        this.friendsRepository = friendsRepository;
    }

    public List<UserFriendDto> getAllFriends(String userToken){
        User user = userService.findUserByGoogleId(userToken);
        Friends friends = friendsRepository.getFriendsByOwnerId(user.getId());
        if (Objects.isNull(friends)) return List.of();
        List<UserFriendDto> friendList = new ArrayList<>();
        for (String friend: friends.getFriendsIdList() ) {
            User findedUser = userService.findUserById(friend);
            if (Objects.isNull(findedUser)) continue;
            friendList.add(UserToUserFriend.convert(findedUser));
        }

        return friendList;
    }

    public Friends addFriend(String friendId,String userToken){
        User user = userService.findUserByGoogleId(userToken);
        Friends friends = friendsRepository.getFriendsByOwnerId(user.getId());
        if (Objects.isNull(friends))
            return friendsRepository.insert(new Friends(user.getId(),Set.of(friendId)));
        if (friends.getFriendsIdList().add(friendId))
            return friendsRepository.save(friends);
        return friends;
    }

    public Friends removeFriend(String friendId,String userToken){
        User user = userService.findUserByGoogleId(userToken);
        Friends friends = friendsRepository.getFriendsByOwnerId(user.getId());
        if (Objects.isNull(friends))
            return null;
        if (friends.getFriendsIdList().remove(friendId))
            return friendsRepository.save(friends);
        return friends;
    }

}
