package com.example.whosbookupdate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.whosbookupdate.mapper")
public class WhosbookupdateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhosbookupdateApplication.class, args);
    }

}
