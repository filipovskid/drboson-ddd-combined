package com.filipovski.drboson.runs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RunsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunsApplication.class, args);
	}

}
