package com.studentregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@EntityScan
@EnableJpaRepositories(basePackages = {"com.studentregistration.dao", "com.studentregistration.dto"})
public class StudentRegistrationWithSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentRegistrationWithSpringBootApplication.class, args);
	}

}
