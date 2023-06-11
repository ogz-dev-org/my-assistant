package org.ogz.template;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class RestTemplate {

    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {

        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new org.springframework.web.client.RestTemplate(factory);
    }

}
