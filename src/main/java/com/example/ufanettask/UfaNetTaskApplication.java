package com.example.ufanettask;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EnableRabbit
public class UfaNetTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(UfaNetTaskApplication.class, args);
    }

}
