package com.chitranshu.mainTransactionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.chitranshu.persistence")
@EntityScan(basePackages = "com.chitranshu.bean")
@SpringBootApplication(scanBasePackages = "com.chitranshu")
public class TransactionServiceEurekaClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceEurekaClientApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	

}
