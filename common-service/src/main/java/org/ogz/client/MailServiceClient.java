package org.ogz.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "mail-service", path = "/api/v1/mail")
public interface MailServiceClient {
    @RequestMapping("/getUsersEmailsBackend")
    ResponseEntity<String> getUserEmails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @RequestMapping  ("/")
    ResponseEntity<String> helloWord();

}
