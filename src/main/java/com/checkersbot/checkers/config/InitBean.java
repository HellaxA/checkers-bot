package com.checkersbot.checkers.config;

import com.checkersbot.checkers.service.bot.CheckersBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitBean implements InitializingBean {

    private final CheckersBot checkersBot;

    @Override
    public void afterPropertiesSet() {
        checkersBot.startGame();
    }
}
