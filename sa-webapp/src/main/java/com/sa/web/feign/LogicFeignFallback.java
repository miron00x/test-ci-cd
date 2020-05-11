package com.sa.web.feign;

import com.sa.web.dto.SentenceDto;
import com.sa.web.dto.SentimentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
public class LogicFeignFallback implements LogicFeignClient {
    @Override
    public SentimentDto analyseSentiment(SentenceDto sentenceDto) {
        return new SentimentDto();
    }
}
