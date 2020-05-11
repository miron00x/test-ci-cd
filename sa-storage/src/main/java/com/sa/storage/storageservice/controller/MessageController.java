package com.sa.storage.storageservice.controller;

import com.sa.storage.storageservice.entity.Message;
import com.sa.storage.storageservice.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/findAll")
    public Flux<Message> findAll() {
        return messageRepository.findAll();
    }

    @GetMapping("/findById/{id}")
    public Mono<Message> findById(@PathVariable("id") String id) {
        return messageRepository.findById(id);
    }

    @PostMapping("/create")
    public Mono<Message> create(@RequestBody Message message) {
        log.info("Create message: " + message.getText());
        return messageRepository.save(message);
    }

    @GetMapping(value = "/stream/buckets/delay", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamAllBucketsDelay() {
        return messageRepository.findAll().delayElements(Duration.ofSeconds(2));
    }
}
