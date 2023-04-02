package com.ogz.user.controller;

import com.ogz.user.dto.UserFriendDto;
import com.ogz.user.service.FriendsService;
import com.ogz.user.service.UserService;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/user")
public class FriendsController {
    private final UserService userService;
    private final FriendsService friendsService;

    public FriendsController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }

    @GetMapping("/friends")
    ResponseEntity<List<UserFriendDto>> getAllFriends(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.getAllFriends(token), HttpStatus.OK);
    }

    @PostMapping("/friends")
    ResponseEntity<Set<String>> addFriend(@RequestBody String friendId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.addFriend(friendId, token).getFriendsIdList(), HttpStatus.OK);
    }

    @PutMapping("/friends/{friendId}")
    ResponseEntity<Set<String>> removeFriend(@PathVariable String friendId,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.removeFriend(friendId,token).getFriendsIdList(), HttpStatus.OK);
    }

}
