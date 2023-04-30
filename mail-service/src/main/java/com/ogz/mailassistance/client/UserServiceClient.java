package com.ogz.mailassistance.client;

import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.ogz.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "user-service", path = "/api/v1/user")
public interface UserServiceClient {
    @RequestMapping("/{googleId}")
    ResponseEntity<User> findUserByGoogleId(@PathVariable String googleId);

    @RequestMapping("/byId/{id}")
    ResponseEntity<User> findUserById(@PathVariable String id);

    @RequestMapping("/email/{email}")
    ResponseEntity<User> findUserByEmail(@PathVariable String email);

    @GetMapping("/all")
    ResponseEntity<List<User>> findAllUsers();

    @PostMapping("/awaitUser/")
    void createAwaitUser(@RequestBody AwaitUserCreate awaitUser);

    @GetMapping("/awaitUser/getFirstAwaitUser")
    ResponseEntity<AwaitUser> getFirstAwaitUser();

    @PutMapping("/awaitUser/")
    ResponseEntity<AwaitUser> updateAwaitUser(@RequestBody AwaitUser awaitUser);

    @DeleteMapping("/awaitUser/{id}")
    ResponseEntity<AwaitUser> deleteAwaitUser(@PathVariable String id);

    @PostMapping("/refreshToken/{id}")
    ResponseEntity<User> reRefreshToken(@PathVariable String id, @RequestBody HashMap body);

    @PostMapping("/accessToken/{id}")
    ResponseEntity<User> reAccessToken(@PathVariable String id, @RequestBody HashMap body);
}
