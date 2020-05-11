package com.sa.storage.storageservice;

import com.sa.storage.storageservice.entity.Message;
import com.sa.storage.storageservice.repository.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
@EnableEurekaClient
public class StorageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(MessageRepository messageRepository) {
		return args -> {
			messageRepository.deleteAll()
					.thenMany(Flux.just(
							new Message("1", "LOL", "LOL", new Date(), new Date()),
							new Message("2", "bdfb", "bf", new Date(), new Date()),
							new Message("3", "bfd", "LOL", new Date(), new Date()),
							new Message("4", "aa", "LOL", new Date(), new Date()),
							new Message("5", "bb", "LOL", new Date(), new Date()),
							new Message("6", "cc", "LOL", new Date(), new Date()),
							new Message("7", "dd", "LOL", new Date(), new Date())
					).flatMap(messageRepository::save))
					.thenMany(messageRepository.findAll())
					.subscribe(System.out::println);

		};
	}

}
