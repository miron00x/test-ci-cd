package com.example.auth;

import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository) {
		RoleEntity adminRole = new RoleEntity("1", "ROLE_ADMIN", "ADIBF");
		RoleEntity userRole = new RoleEntity("2", "ROLE_USER", "ADIBF");
		Set roles = new HashSet<RoleEntity>();
		roles.add(userRole);
		return args -> {
			roleRepository.deleteAll().
					thenMany(Flux.just(adminRole, userRole).flatMap(roleRepository::save))
					.thenMany(roleRepository.findAll()).subscribe(System.out::println);
			userRepository.deleteAll()
					.thenMany(Flux.just(
							new UserEntity("1", "VASYA", passwordEncoder().encode("12345"), roles),
							new UserEntity("2", "admin", passwordEncoder().encode("admin"), Collections.singleton(adminRole))
					).flatMap(userRepository::save))
					.thenMany(userRepository.findByUsername("VASYA"))
					.subscribe(System.out::println);
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
