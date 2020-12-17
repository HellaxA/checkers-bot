package com.checkersbot.checkers.service.rest;

import com.checkersbot.checkers.entity.ConnectionDto;
import com.checkersbot.checkers.entity.GameDto;

public interface RestService {
    GameDto getGameDtoPlainJSON();

    ConnectionDto getConnectionDtoPlainJSON();
}
