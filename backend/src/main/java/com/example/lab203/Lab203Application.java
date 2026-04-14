package com.example.lab203;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ?203???????????
 */
@SpringBootApplication
@MapperScan("com.example.lab203.mapper")
public class Lab203Application {

    /**
     * ?? Spring Boot ???
     *
     * @param args ????
     */
    public static void main(String[] args) {
        SpringApplication.run(Lab203Application.class, args);
    }
}
