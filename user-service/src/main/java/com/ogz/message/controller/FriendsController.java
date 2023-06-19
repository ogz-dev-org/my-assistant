package com.ogz.message.controller;

import com.ogz.message.dto.FriendAddDto;
import com.ogz.message.dto.UserFriendDto;
import com.ogz.message.dto.UserFriendsDto;
import com.ogz.message.service.FriendsService;
import io.swagger.v3.oas.annotations.Hidden;
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
    ResponseEntity<UserFriendsDto> getAllFriends(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(new UserFriendsDto(friendsService.getAllFriends(token)), HttpStatus.OK);
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
