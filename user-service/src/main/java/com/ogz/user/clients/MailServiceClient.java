package com.ogz.user.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mail-service", path = "/api/v1/mail")
public interface MailServiceClient {
    @GetMapping("/getUsersEmails")
    ResponseEntity<String> getUserEmails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @GetMapping("/")
    ResponseEntity<String> helloWord();

}
