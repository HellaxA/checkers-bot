package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.*;
import com.checkersbot.checkers.service.rest.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CheckersBotImpl implements CheckersBot {
    private final ApplicationContext applicationContext;
    private final RestService restService;
    private String token;

    @Override
    public void startGame() {
        ConnectionDto connectionDto = restService.getConnectionDtoPlainJSON();
        String teamColor = connectionDto.getData().getColor();
        token = connectionDto.getData().getToken();

        CheckersBotRules checkersBotRules = applicationContext.getBean(CheckersBotRulesImpl.class);

        while(true) {
            GameDto theGameDto = restService.getGameDtoPlainJSON();

            if (theGameDto.getData().getWhose_turn().equals(teamColor)) {
                Checker[] checkers = theGameDto.getData().getBoard();

                List<Move> availableMoves = checkersBotRules.getAllAvailableMoves
                        (checkers, teamColor);

                List<PseudoBoardData> pseudoBoardData
                        = getAllPseudoBoardsForAllBoards(availableMoves, checkers);

                List<PseudoBoardData> pseudoBoardDataWithScoreOne
                        = getAllPseudoBoardsWithScore(pseudoBoardData, teamColor, checkersBotRules);

                System.out.println(pseudoBoardDataWithScoreOne);

                Move move = pseudoBoardDataWithScoreOne.get(0).getMove();

                log.info(String.valueOf(move));
                MoveRequest moveRequest = new MoveRequest(new int[]{
                        move.getFrom(),
                        move.getTo()
                });

                MoveServerResponse moveServerResponse = null;
                while (moveServerResponse == null) {
                    moveServerResponse =
                            restService.getMoveServerResponse(
                                    token, moveRequest);
                    if (moveServerResponse == null) {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            log.info(e.getMessage());
                        }
                    }
                }
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    log.info(e.getMessage());
                }
            }
        }
    }

    private List<PseudoBoardData> getAllPseudoBoardsWithScore(List<PseudoBoardData> pseudoBoardData,
                                                              String teamColor,
                                                              CheckersBotRules checkersBotRules) {

        for (PseudoBoardData pseudoBoard : pseudoBoardData) {
            List<Move> tempMoves = checkersBotRules.getPseudoAvailableMoves(
                            getOpponentColor(teamColor),
                            pseudoBoard.getPseudoCheckers());

            List<Move> newTempMoves = new ArrayList<>();

            tempMoves
                    .stream()
                    .filter((move -> move.getPriority() != 0))
                    .forEach(newTempMoves::add);

            if (!newTempMoves.isEmpty()) {
                pseudoBoard.setScore(1);
            }

        }

        List<PseudoBoardData> pseudoBoardDataListFiltered = new ArrayList<>();
        pseudoBoardData
                .stream()
                .filter((move -> move.getScore() != 1))
                .forEach(pseudoBoardDataListFiltered::add);

        if (pseudoBoardDataListFiltered.isEmpty()) {
            return pseudoBoardData;
        } else {
            return pseudoBoardDataListFiltered;
        }


    }

    private String getOpponentColor(String teamColor) {
        if (teamColor.equals(CheckersBotRulesImpl.BLACK)) {
            return CheckersBotRulesImpl.RED;
        } else {
            return CheckersBotRulesImpl.BLACK;
        }
    }

    private List<PseudoBoardData> getAllPseudoBoardsForAllBoards(List<Move> availableMoves,
                                                                 Checker[] checkers) {

        MinimaxAlgo minimaxAlgo = applicationContext.getBean(MinimaxAlgoImpl.class);
        List<PseudoBoardData> listOfPseudoBoardData = new ArrayList<>();

        for (Move move : availableMoves) {
            Checker[] pseudoCheckers = minimaxAlgo.makePseudoMoves(move, checkers);
            // List<Move> pseudoMoves = checkersBotRules.getPseudoAvailableMoves(teamColor, pseudoCheckers);
            listOfPseudoBoardData.add(new PseudoBoardData(move, pseudoCheckers));
        }

        return listOfPseudoBoardData;
    }
}