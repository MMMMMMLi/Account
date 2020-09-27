package com.imengli.appletServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AppletServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppletServerApplication.class, args);
    }

}
