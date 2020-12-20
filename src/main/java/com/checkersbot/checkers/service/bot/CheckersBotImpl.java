package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.*;
import com.checkersbot.checkers.service.rest.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CheckersBotImpl implements CheckersBot {
    public static final String RED = "RED";
    public static final String BLACK = "BLACK";
    public static final int ROWS = 8;
    public static final int COLS = 4;
    public static final int CHECKERS_ON_LINE = 4;
    public static final int MAX_POSITION = 32;
    private static final int MIN_POSITION = 1;
    private final RestService restService;
    private String token;
    private String teamColor;

    @Override
    public void startGame() {
        ConnectionDto connectionDto = restService.getConnectionDtoPlainJSON();
        teamColor = connectionDto.getData().getColor();
        token = connectionDto.getData().getToken();

        while(true) {
            GameDto theGameDto = restService.getGameDtoPlainJSON();

            if (theGameDto.getData().getWhose_turn().equals(teamColor)) {
                List<Move> availableMoves = getAllAvailableMoves(theGameDto);

                Move move = availableMoves.get(0);

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

    private List<Move> getAllAvailableMoves(GameDto theGameDto) {
        Checker[] checkers = theGameDto.getData().getBoard();

        List<Move> allMovesForParticularTeam = getAllMovesForParticularTeam(Arrays.asList(checkers));
        List<Move> allMovesOnlyWithHighPriority = new ArrayList<>();
        allMovesForParticularTeam
                .stream()
                .filter((move -> move.getPriority() != 0))
                .forEach(allMovesOnlyWithHighPriority::add);

        if (allMovesOnlyWithHighPriority.isEmpty()) {
            return allMovesForParticularTeam;
        }

        return allMovesOnlyWithHighPriority;
    }

    private List<Move> getAllMovesForParticularTeam(List<Checker> checkers) {
        List<Move> list = new ArrayList<>();

        if (teamColor.equals(BLACK)) {
            for (Checker checker : checkers) {
                if (checker.getColor().equals(BLACK) && !checker.getKing()) {
                    int from = checker.getPosition();

                    list.addAll(findOpponentsNearBy(from, checkers));

                    if (from - CHECKERS_ON_LINE >= MIN_POSITION) {
                        int to = from - CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, list, from, to);

                        if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from - CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);

                        } else if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from - CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);
                        }

                    }
                } else if (checker.getColor().equals(BLACK) && checker.getKing()) {
                    int from = checker.getPosition();

                    list.addAll(findOpponentsNearBy(from, checkers));

                    if (from + CHECKERS_ON_LINE <= MAX_POSITION) {
                        int to = from + CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, list, from, to);

                        if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from + CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);
                        } else if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from + CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);

                        }
                    }
                }
            }
        } else {
            for (Checker checker : checkers) {
                if (checker.getColor().equals(RED) && !checker.getKing()) {
                    int from = checker.getPosition();

                    list.addAll(findOpponentsNearBy(from, checkers));

                    if (from + CHECKERS_ON_LINE <= MAX_POSITION) {
                        int to = from + CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, list, from, to);

                        if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from + CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);
                        } else if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from + CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);

                        }
                    }
                } else if (checker.getColor().equals(RED) && checker.getKing()) {
                    int from = checker.getPosition();

                    if (from - CHECKERS_ON_LINE >= MIN_POSITION) {
                        int to = from - CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, list, from, to);

                        if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from - CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);

                        } else if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from - CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, list, from, secondTo);
                        }

                    }
                }
            }
        }

        return list;
    }

    private List<Move> findOpponentsNearBy(int from, List<Checker> checkers) {
        return findPositionsNearBy(
                Objects.requireNonNull(findCheckerByPosition(from, checkers)), checkers);
    }

    private List<Move> findPositionsNearBy(Checker checkerFrom, List<Checker> checkers) {
        List<Move> res = new ArrayList<>();
        int from = checkerFrom.getPosition();

        if (checkerFrom.getRow() % 2 == 0) {

            int leftDown = from - 3;
            int leftUp = from - 4;
            int rightDown = from + 5;
            int rightUp = from + 4;

            if (checkerFrom.getRow() != 0 && checkerFrom.getColumn() != 3) {

                Checker leftDownChecker = null;
                if (teamColor.equals(BLACK) || checkerFrom.getKing()) {
                    leftDownChecker = findCheckerByPosition(leftDown, checkers);
                }

                if (leftDownChecker != null && !leftDownChecker.getColor().equals(teamColor)) {
                    int destination = leftDownChecker.getPosition() - 4;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }

                }
            }
            if (checkerFrom.getRow() != 0 && checkerFrom.getColumn() != 0) {

                Checker leftUpChecker = null;
                if (teamColor.equals(BLACK) || checkerFrom.getKing()) {
                    leftUpChecker = findCheckerByPosition(leftUp, checkers);
                }

                if (leftUpChecker != null && !leftUpChecker.getColor().equals(teamColor)) {
                    int destination = leftUpChecker.getPosition() - 5;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }
            if (checkerFrom.getColumn() != 3) {

                Checker rightDownChecker = null;
                if (teamColor.equals(RED) || checkerFrom.getKing()) {
                    rightDownChecker = findCheckerByPosition(rightDown, checkers);
                }

                if (rightDownChecker != null && !rightDownChecker.getColor().equals(teamColor)) {
                    int destination = rightDownChecker.getPosition() + 4;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }

            }
            if (checkerFrom.getColumn() != 0) {

                Checker rightUpChecker = null;
                if (teamColor.equals(RED) || checkerFrom.getKing()) {
                    rightUpChecker = findCheckerByPosition(rightUp, checkers);
                }

                if (rightUpChecker != null && !rightUpChecker.getColor().equals(teamColor)) {
                    int destination = rightUpChecker.getPosition() + 3;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }


        } else {
            int leftDown = from - 4;
            int leftUp = from - 5;
            int rightDown = from + 4;
            int rightUp = from + 3;


            if (checkerFrom.getColumn() != 3) {
                Checker leftDownChecker = null;

                if (teamColor.equals(BLACK) || checkerFrom.getKing()) {
                    leftDownChecker = findCheckerByPosition(leftDown, checkers);
                }

                if (leftDownChecker != null && !leftDownChecker.getColor().equals(teamColor)) {
                    int destination = leftDownChecker.getPosition() - 3;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }

            if (checkerFrom.getColumn() != 0 && checkerFrom.getRow() != 1) {

                Checker leftUpChecker = null;
                if (teamColor.equals(BLACK) || checkerFrom.getKing()) {
                    leftUpChecker = findCheckerByPosition(leftUp, checkers);
                }

                if (leftUpChecker != null && !leftUpChecker.getColor().equals(teamColor)) {
                    int destination = leftUpChecker.getPosition() - 4;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }
            if (checkerFrom.getRow() != 7 && checkerFrom.getColumn() != 3 && checkerFrom.getRow() != 6) {

                Checker rightDownChecker = null;
                if (teamColor.equals(RED) || checkerFrom.getKing()) {
                    rightDownChecker = findCheckerByPosition(rightDown, checkers);
                }
                if (rightDownChecker != null && !rightDownChecker.getColor().equals(teamColor)) {
                    int destination = rightDownChecker.getPosition() + 5;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }
            if (checkerFrom.getRow() != 7 && checkerFrom.getColumn() != 0) {

                Checker rightUpChecker = null;
                if (teamColor.equals(RED) || checkerFrom.getKing()) {
                    rightUpChecker = findCheckerByPosition(rightUp, checkers);
                }

                if (rightUpChecker != null && !rightUpChecker.getColor().equals(teamColor)) {
                    int destination = rightUpChecker.getPosition() + 4;
                    Checker tempChecker = findCheckerByPosition(destination, checkers);

                    if (tempChecker == null) {
                        Move move = new Move(from, destination, 1);
                        res.add(move);
                    }
                }
            }
        }

        return res;
    }

    private void checkPosAndAddToList(List<Checker> checkers,
                                      List<Move> list, int from,
                                      int to) {

        int checkedPos = checkPos(to, checkers);

        if (checkedPos != -1) {
            int priority = checkedPos == to ? 0 : 1;
            Move theSecondMove = new Move(from, to, priority);
            list.add(theSecondMove);
        }
    }

    private int checkPos(int to, List<Checker> checkers) {

        Checker checkerTo = findCheckerByPosition(to, checkers);

        if (checkerTo == null) {
            return to;
        } else if (checkerTo.getColor().equals(teamColor)) {
            return -1;
        } else if (!checkerTo.getColor().equals(teamColor)) {
            return -1;
        }


        return to;
    }

    private Checker findCheckerByPosition(int to, List<Checker> checkers) {
        for (Checker checker : checkers) {
            if (checker.getPosition() == to) {
                return checker;
            }
        }

        return null;
    }
}