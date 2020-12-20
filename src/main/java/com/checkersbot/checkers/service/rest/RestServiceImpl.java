package com.checkersbot.checkers.service.rest;

import com.checkersbot.checkers.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class RestServiceImpl implements RestService {
    private final RestTemplate restTemplate;

    public RestServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Value("${checkers-server.url}")
    private String serverUrl;

    public GameDto getGameDtoPlainJSON() {
        String url = serverUrl + "/game";
        GameDto gameDto = restTemplate.getForObject(url, GameDto.class);
        return gameDto;
    }

    @Override
    public ConnectionDto getConnectionDtoPlainJSON() {
        String url = serverUrl + "/game?team_name=KYS";
        ConnectionDto connectionDto = restTemplate.postForObject(url, null, ConnectionDto.class);
        log.info(String.valueOf(connectionDto));
        return connectionDto;
    }

    @Override
    public MoveServerResponse getMoveServerResponse(String token, MoveRequest moveRequest) {
        String url = serverUrl + "/move";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + token);
        final HttpEntity<MoveRequest> entity = new HttpEntity<>(moveRequest, headers);

        try {
            ResponseEntity<MoveServerResponse> objectResponseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, entity, MoveServerResponse.class);

            return objectResponseEntity.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
