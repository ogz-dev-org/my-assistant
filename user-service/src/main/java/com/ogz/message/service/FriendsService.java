package com.ogz.user.service;

import com.ogz.user.dto.UserFriendDto;
import com.ogz.user.dtoConvertor.UserToUserFriend;
import com.ogz.user.model.Friends;
import com.ogz.user.repository.FriendsRepository;
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
            return friendsRepository.insert(friends);
        return friends;
    }

    public Friends removeFriend(String friendId,String userToken){
        User user = userService.findUserByGoogleId(userToken);
        Friends friends = friendsRepository.getFriendsByOwnerId(user.getId());
        if (Objects.isNull(friends))
            return null;
        if (friends.getFriendsIdList().remove(friendId))
            return friendsRepository.insert(friends);
        return friends;
    }

}
