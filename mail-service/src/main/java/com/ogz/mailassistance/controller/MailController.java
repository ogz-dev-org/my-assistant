package com.ogz.mailassistance.controller;


import com.sun.net.httpserver.Headers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    @GetMapping("/")
    ResponseEntity<String> helloWord() {
        System.out.println("naber");
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @PostMapping("/test")
    ResponseEntity<Map> getMails(@RequestHeader Map<String, String> headers) {
        System.out.println(headers.get("accesstoken"));
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://gmail.googleapis.com/gmail/v1/users/bedirhan.altun6159@gmail.com/messages");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty ("Authorization",  "Bearer "+headers.get("accesstoken"));
            connection.setDoOutput(true);

            String status = connection.getResponseCode() + "";
            System.out.println(status);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //https://gmail.googleapis.com/gmail/v1/users/ugurdoganoguz@gmail.com/messages
        //-header 'Authorization: Bearer [YOUR_ACCESS_TOKEN]' \
        return new ResponseEntity<>(headers,HttpStatus.OK);
    }

}
