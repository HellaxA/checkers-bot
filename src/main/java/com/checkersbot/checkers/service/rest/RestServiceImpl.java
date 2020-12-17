package com.checkersbot.checkers.service.rest;

import com.checkersbot.checkers.entity.ConnectionDto;
import com.checkersbot.checkers.entity.GameDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestServiceImpl implements RestService{
    private final RestTemplate restTemplate;

    public RestServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Value("${checkers-server.url}")
    private String serverUrl;

    public GameDto getGameDtoPlainJSON() {
        String url = serverUrl + "/game";
        GameDto gameDto = restTemplate.getForObject(url, GameDto.class);
        log.info(String.valueOf(gameDto));
        return gameDto;
    }

    @Override
    public ConnectionDto getConnectionDtoPlainJSON() {
        return null;
    }
}
