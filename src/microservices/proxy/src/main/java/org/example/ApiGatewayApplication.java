package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@SpringBootApplication
public class ApiGatewayApplication {



    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        }));
        return restTemplate;
    }

}