package com.example.eurekaserveranaplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaserveranaplanApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaserveranaplanApplication.class, args);
	}

}
