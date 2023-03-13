package com.ogz.user.controller;

import com.ogz.user.service.AwaitUserService;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class AwaitUserController {

    private final AwaitUserService awaitUserService;

    public AwaitUserController(AwaitUserService awaitUserService) {
        this.awaitUserService = awaitUserService;
    }

    @GetMapping("/awaitUser")
    ResponseEntity<String> hello() {
        return new ResponseEntity<String>("Hello from User Service await user", HttpStatus.OK);
    }

    @PostMapping("/awaitUser/")
    void createAwaitUser(@RequestBody AwaitUserCreate awaitUser) {
        awaitUserService.createAwaitUser(awaitUser);
    }

    @GetMapping("/awaitUser/getFirstAwaitUser")
    AwaitUser getFirstAwaitUser(){
        AwaitUser user = awaitUserService.getFirstAwaituser();
        System.out.println("User Test:"  + user.toString());
        return user;
    }

    @GetMapping("/awaitUser/all")
    ResponseEntity<List<AwaitUser>> getAllAwaitUser(){
        return new ResponseEntity<>(awaitUserService.getAll(),HttpStatus.OK);
    }

}
