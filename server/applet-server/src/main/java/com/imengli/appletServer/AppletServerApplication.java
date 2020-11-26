package com.imengli.appletServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
public class AppletServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppletServerApplication.class, args);
    }

}
