package com.monetamedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
//@PropertySource("file:${user.dir}/.env")
public class MonetaMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonetaMediaApplication.class, args);
    }

}
