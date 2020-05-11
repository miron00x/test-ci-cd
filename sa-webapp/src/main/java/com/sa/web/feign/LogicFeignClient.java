package com.sa.web.feign;

import com.sa.web.dto.SentenceDto;
import com.sa.web.dto.SentimentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sa-logic", fallback = LogicFeignFallback.class)
public interface LogicFeignClient {

    @GetMapping("/analyse/sentiment")
    SentimentDto analyseSentiment(@RequestBody SentenceDto sentenceDto);

}
