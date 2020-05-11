package com.sa.web;

import com.sa.web.dto.Message;
import com.sa.web.dto.SentenceDto;
import com.sa.web.dto.SentimentDto;
import com.sa.web.feign.LogicFeignClient;
import com.sa.web.feign.StorageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class SentimentController {

    private final LogicFeignClient logicFeignClient;

    private final StorageFeignClient storageFeignClient;

    private final KafkaTemplate<Long, Message> kafkaTemplate;

    public SentimentController(LogicFeignClient logicFeignClient, StorageFeignClient storageFeignClient, KafkaTemplate<Long, Message> kafkaTemplate) {
        this.logicFeignClient = logicFeignClient;
        this.storageFeignClient = storageFeignClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "Message", topics = {"server.message"}, containerFactory = "singleFactory")
    public void consume(Message message) {
        log.info("=> consumed {}", message.getText());
    }

    @PostMapping("/sentiment")
    public SentimentDto sentimentAnalysis(@RequestBody SentenceDto sentenceDto, Session session) {
        Message message = new Message();
        message.setText(sentenceDto.getSentence());
        message.setSession(session.toString());
        kafkaTemplate.send("server.message", message);
        storageFeignClient.create(message).subscribe();
        return logicFeignClient.analyseSentiment(sentenceDto);
    }

    @GetMapping("/testHealth")
    public String testHealth() {
        return "testHealth";
    }
}


