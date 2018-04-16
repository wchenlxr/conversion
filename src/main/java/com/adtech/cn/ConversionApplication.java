package com.adtech.cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.adtech.cn.mapper")
@EnableScheduling
public class ConversionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversionApplication.class, args);
    }
}
