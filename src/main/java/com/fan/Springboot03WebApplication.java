package com.fan;

import com.fan.server.DiscardServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Springboot03WebApplication implements CommandLineRunner {
    @Resource
    private DiscardServer discardServer;
    public static void main(String[] args) {
        SpringApplication.run(Springboot03WebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        discardServer.httpProcess();
    }
}
