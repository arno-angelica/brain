package com.example.testa;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan(basePackages = "com.example.testa.dao.mapper")
@SpringBootApplication(scanBasePackages = {"com.example.testa.*", "com.arno.*"})
public class TestAApplication {


    public static void main(String[] args) {
        SpringApplication.run(TestAApplication.class, args);
        log.info("test A start success");
    }

}
