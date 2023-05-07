package com.ogz.notificationcrudservice.client;

import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "user-service", path = "/api/v1/user")
public interface UserServiceClient {
    @RequestMapping("/{googleId}")
    ResponseEntity<User> findUserByGoogleId(@PathVariable String googleId);

    @RequestMapping("/email/gmail/{mail}")
    ResponseEntity<User> findUserByGmail(@PathVariable String mail);

    @GetMapping("/all")
    ResponseEntity<List<User>> findAllUsers();

    @PostMapping("/awaitUser/")
    void createAwaitUser(@RequestBody AwaitUserCreate awaitUser);

    @GetMapping("/awaitUser/getFirstAwaitUser")
    ResponseEntity<AwaitUser> getFirstAwaitUser();

    @PostMapping("/refreshToken/{id}")
    ResponseEntity<User> reRefreshToken(@PathVariable String id, @RequestBody HashMap body);

    @PostMapping("/accessToken/{id}")
    ResponseEntity<User> reAccessToken(@PathVariable String id, @RequestBody HashMap body);
}
