package org.ogz.template;

import org.ogz.dto.MailSocketDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WebSocketRestTemplate {


    private static WebSocketRestTemplate instance;
    private final RestTemplate restTemplate;
    private WebSocketRestTemplate(){
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
       restTemplate = new org.springframework.web.client.RestTemplate(factory);
    }

    public static WebSocketRestTemplate getInstance() {
        if (Objects.isNull(instance)) instance = new WebSocketRestTemplate();
        return instance;
    }

    public void postMailEvent(MailSocketDto dto){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<MailSocketDto> request =
                new HttpEntity<>(dto, headers);

        MailSocketDto personResultAsJsonStr =
                restTemplate.postForObject("http://localhost:3000/api/v1/notification/mailEvent", request,
                        MailSocketDto.class);

    }


}
