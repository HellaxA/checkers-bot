package com.checkersbot.checkers.controller;

import com.checkersbot.checkers.service.bot.CheckersBot;
import com.checkersbot.checkers.service.bot.CheckersBotImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StartGameController {
    private final ApplicationContext applicationContext;

    @PostMapping("/start")
    public String startGame() {
        CheckersBot checkersBot = applicationContext.getBean(CheckersBotImpl.class);
        checkersBot.startGame();

        return "Bot has been started";
    }

    @PostMapping("/start2")
    public String startGame2() {
        CheckersBot checkersBot = applicationContext.getBean(CheckersBotImpl.class);
        checkersBot.startGame();

        return "Bot has been started";
    }
}
