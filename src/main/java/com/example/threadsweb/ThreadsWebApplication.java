package com.example.threadsweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.threadsweb.mapper")
@SpringBootApplication
public class ThreadsWebApplication {

    public static void main (String[] args) {
        SpringApplication.run(ThreadsWebApplication.class, args);
    }

}
