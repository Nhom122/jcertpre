package com.jcertpre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.jcertpre.model")
@EnableJpaRepositories(basePackages = "com.jcertpre.repositories")
public class JcertpreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcertpreApplication.class, args);
	}

}
