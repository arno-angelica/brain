package com.example.testb;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan(basePackages = "com.example.testb.dao.mapper")
@SpringBootApplication(scanBasePackages = {"com.example.testb.*", "com.arno.*"})
public class TestBApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestBApplication.class, args);
        log.info("test B start success");
    }

}
