package com.jcertpre;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.jcertpre.model")
@EnableJpaRepositories(basePackages = "com.jcertpre.repositories")
public class JcertpreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcertpreApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadTestAdvisor(IUserRepository userRepository) {
		return args -> {
			String learnerEmail = "a@gmail.com";
			String advisorEmail = "advisor@example.com";
			User learner = new User(learnerEmail, "123456", "a", User.Role.LEARNER);
			userRepository.save(learner);
			if (!userRepository.existsByEmail(advisorEmail)) {
				User advisor = new User(advisorEmail, "123456", "Cố Vấn Nguyễn Văn A", User.Role.ADVISOR);
				userRepository.save(advisor);
				System.out.println("✅ Tài khoản cố vấn đã được tạo: " + advisorEmail);
			} else {
				System.out.println("ℹ️ Tài khoản cố vấn đã tồn tại: " + advisorEmail);
			}
		};
	}
}
