package com.chitranshu.mainCardService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.chitranshu")
@EntityScan(basePackages = "com.chitranshu.bean")
@EnableJpaRepositories(basePackages = "com.chitranshu.persistence")
@EnableEurekaClient
public class CardServiceEurekaClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardServiceEurekaClientApplication.class, args);
	}

}
