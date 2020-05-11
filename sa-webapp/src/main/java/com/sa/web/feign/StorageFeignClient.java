package com.sa.web.feign;

import com.sa.web.dto.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "sa-storage")
@Qualifier("storageFeignClient")
public interface StorageFeignClient {

    @PostMapping("/message/create")
    Mono<Message> create(@RequestBody Message message);

    @GetMapping("/message/findAll")
    Flux<Message> findAll();

}
