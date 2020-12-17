package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.Checker;
import com.checkersbot.checkers.entity.ConnectionDto;
import com.checkersbot.checkers.entity.GameDto;
import com.checkersbot.checkers.entity.Move;
import com.checkersbot.checkers.service.rest.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckersBotImpl implements CheckersBot {
    public static final String RED = "RED";
    public static final int ROWS = 8;
    public static final int COLS = 4;
    public static final int CHECKERS_ON_LINE = 4;
    public static final int MAX_POSITION = 32;

    private final RestService restService;

    private List<Move> getAllAvailableMoves(String teamColor) {
        GameDto theGameDto = restService.getGameDtoPlainJSON();
        Checker[] checkers = theGameDto.getData().getBoard();

        List<Move> list = new ArrayList<>();

        for (Checker checker : checkers) {
            int from = checker.getPosition();

            if (from + CHECKERS_ON_LINE <= MAX_POSITION) {
                Move theMove;
                int to = from + CHECKERS_ON_LINE;

                theMove = new Move(from, to);
                list.add(theMove);

                if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                    int secondTo = from + CHECKERS_ON_LINE + 1;
                    Move theSecondMove = new Move(from, secondTo);

                    list.add(theSecondMove);

                } else if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                    int secondTo = from + CHECKERS_ON_LINE - 1;
                    Move theSecondMove = new Move(from, secondTo);

                    list.add(theSecondMove);

                }
            }
        }

        return list;
    }

    @Override
    public void startGame() {
        ConnectionDto connectionDto = restService.getConnectionDtoPlainJSON();
        // String teamColor = connectionDto.getData().getColor();

        List<Move> availableMoves = getAllAvailableMoves("RED");

        log.info(String.valueOf(availableMoves));
    }
}
