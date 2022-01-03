package com.sip.ams;

import com.sip.ams.controllers.ArticleController;
import com.sip.ams.entities.Role;
import com.sip.ams.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootApplication
public class AmsApiApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		new File(ArticleController.uploadDirectory).mkdir();
		SpringApplication.run(AmsApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(roleRepository.findByRole("ROLE_USER") == null)
			roleRepository.save(new Role("ROLE_USER"));
		if(roleRepository.findByRole("ROLE_ADMIN") == null)
			roleRepository.save(new Role("ROLE_ADMIN"));
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
