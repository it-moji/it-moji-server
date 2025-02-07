package com.itmoji.itmojiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ItMojiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItMojiServerApplication.class, args);
    }

}
