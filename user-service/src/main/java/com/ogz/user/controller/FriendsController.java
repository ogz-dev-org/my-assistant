package com.ogz.user.controller;

import com.ogz.user.dto.FriendAddDto;
import com.ogz.user.dto.UserFriendDto;
import com.ogz.user.service.FriendsService;
import com.ogz.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/friends")
public class FriendsController {

    private final FriendsService friendsService;

    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @Hidden
    @GetMapping("/hello")
    ResponseEntity<String> hello(){
        return new ResponseEntity<>("Hello world from friends service",HttpStatus.OK);
    }

    @GetMapping("/")
    ResponseEntity<List<UserFriendDto>> getAllFriends(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.getAllFriends(token), HttpStatus.OK);
    }

    @PostMapping("/")
    ResponseEntity<Set<String>> addFriend(@RequestBody FriendAddDto friendId,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.addFriend(friendId.getFriendsId(), token).getFriendsIdList(), HttpStatus.OK);
    }

    @DeleteMapping("/{friendId}")
    ResponseEntity<Set<String>> removeFriend(@PathVariable String friendId,@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(friendsService.removeFriend(friendId,token).getFriendsIdList(), HttpStatus.OK);
    }

}
