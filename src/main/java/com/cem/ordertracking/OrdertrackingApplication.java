package com.cem.ordertracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.cem.ordertracking.repository")
public class OrdertrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdertrackingApplication.class, args);
	}

}
