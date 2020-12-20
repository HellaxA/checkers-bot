package com.checkersbot.checkers.config;

import com.checkersbot.checkers.service.bot.CheckersBot;
import com.checkersbot.checkers.service.bot.CheckersBotImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitBean implements InitializingBean {

    private final ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
//        CheckersBot checkersBot = applicationContext.getBean(CheckersBotImpl.class);
//        checkersBot.startGame();
    }
}
