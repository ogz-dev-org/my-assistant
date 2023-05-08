package com.ogz.message.controller;

import com.ogz.message.service.AwaitUserService;
import io.swagger.v3.oas.annotations.Hidden;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Hidden
@RestController
@RequestMapping("/api/v1/user")
public class AwaitUserController {

    private final AwaitUserService awaitUserService;

    public AwaitUserController(AwaitUserService awaitUserService) {
        this.awaitUserService = awaitUserService;
    }

    @GetMapping("/awaitUser")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello from User Service await user", HttpStatus.OK);
    }

    @PostMapping("/awaitUser/")
    void createAwaitUser(@RequestBody AwaitUserCreate awaitUser) {
        awaitUserService.createAwaitUser(awaitUser);
    }

    @PutMapping("/awaitUser/")
    ResponseEntity<AwaitUser> updateAwaitUser(@RequestBody AwaitUser awaitUser) {
        return new ResponseEntity<>(awaitUserService.updateAwaitUser(awaitUser),
                HttpStatus.OK);
    }

    @DeleteMapping("/awaitUser/{id}")
    ResponseEntity<AwaitUser> deleteAwaitUser(@PathVariable String id) {
        return new ResponseEntity<>(awaitUserService.deleteAwaitUser(id), HttpStatus.OK);
    }

    @GetMapping("/awaitUser/getFirstAwaitUser")
    ResponseEntity<AwaitUser> getFirstAwaitUser(){
        AwaitUser user = awaitUserService.getFirstAwaituser();
        if (Objects.isNull(user))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/awaitUser/all")
    ResponseEntity<List<AwaitUser>> getAllAwaitUser(){
        return new ResponseEntity<>( awaitUserService.getAll(), HttpStatus.OK);
    }

}
