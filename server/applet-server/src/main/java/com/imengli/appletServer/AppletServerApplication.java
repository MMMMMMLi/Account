package com.imengli.appletServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableEurekaClient
@EnableScheduling
public class AppletServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppletServerApplication.class, args);
    }

}
