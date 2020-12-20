package com.checkersbot.checkers.service.rest;

import com.checkersbot.checkers.entity.ConnectionDto;
import com.checkersbot.checkers.entity.GameDto;
import com.checkersbot.checkers.entity.MoveRequest;
import com.checkersbot.checkers.entity.MoveServerResponse;

public interface RestService {
    GameDto getGameDtoPlainJSON();

    ConnectionDto getConnectionDtoPlainJSON();

    MoveServerResponse getMoveServerResponse(String token, MoveRequest moveRequest);
}
