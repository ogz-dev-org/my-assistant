package com.ogz.mailassistance.client;

import org.ogz.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", path = "/v1/user")
public interface UserServiceClient {
    @RequestMapping("/{googleId}")
    ResponseEntity<User> findUserByGoogleId(@PathVariable String googleId);

}
