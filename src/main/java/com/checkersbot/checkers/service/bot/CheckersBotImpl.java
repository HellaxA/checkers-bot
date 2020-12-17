package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.Checker;
import com.checkersbot.checkers.entity.ConnectionDto;
import com.checkersbot.checkers.entity.GameDto;
import com.checkersbot.checkers.entity.Move;
import com.checkersbot.checkers.service.rest.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckersBotImpl implements CheckersBot {
    public static final String RED = "RED";
    public static final String BLACK = "BLACK";
    public static final int ROWS = 8;
    public static final int COLS = 4;
    public static final int CHECKERS_ON_LINE = 4;
    public static final int MAX_POSITION = 32;
    private static final int MIN_POSITION = 1;
    private final RestService restService;

    @Override
    public void startGame() {
        ConnectionDto connectionDto = restService.getConnectionDtoPlainJSON();
        // String teamColor = connectionDto.getData().getColor();

        List<Move> availableMoves = getAllAvailableMoves(BLACK);

        log.info(String.valueOf(availableMoves));
    }

    private List<Move> getAllAvailableMoves(String teamColor) {
        GameDto theGameDto = restService.getGameDtoPlainJSON();
        Checker[] checkers = theGameDto.getData().getBoard();

        List<Move> list = getAllMovesForParticularTeam(Arrays.asList(checkers), teamColor);

        return list;
    }

    private List<Move> getAllMovesForParticularTeam(List<Checker> checkers, String teamColor) {
        List<Move> list = new ArrayList<>();

        if (teamColor.equals(BLACK)) {
            for (Checker checker : checkers) {
                if (checker.getColor().equals(BLACK)) {
                    int from = checker.getPosition();

                    List<Checker> opponentsNearBy = findOpponentsNearBy(from, checkers, teamColor);



                    if (from - CHECKERS_ON_LINE >= MIN_POSITION) {
                        int to = from - CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, teamColor, list, from, to, to);

                        if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from - CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, teamColor, list, from, to, secondTo);

                        } else if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from - CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, teamColor, list, from, to, secondTo);
                        }

                    }
                }
            }
        } else {
            for (Checker checker : checkers) {
                if (checker.getColor().equals(RED)) {
                    int from = checker.getPosition();

                    List<Checker> opponentsNearBy = findOpponentsNearBy(from, checkers, teamColor);

                    if (from + CHECKERS_ON_LINE <= MAX_POSITION) {
                        int to = from + CHECKERS_ON_LINE;

                        checkPosAndAddToList(checkers, teamColor, list, from, to, to);

                        if (checker.getRow() % 2 == 0 && checker.getColumn() != COLS - 1) {
                            int secondTo = from + CHECKERS_ON_LINE + 1;

                            checkPosAndAddToList(checkers, teamColor, list, from, to, secondTo);
                        } else if (checker.getRow() % 2 != 0 && checker.getColumn() != 0) {
                            int secondTo = from + CHECKERS_ON_LINE - 1;

                            checkPosAndAddToList(checkers, teamColor, list, from, to, secondTo);

                        }
                    }
                }
            }
        }

        return list;
    }

    private List<Checker> findOpponentsNearBy(int from, List<Checker> checkers, String teamColor) {
        List<Checker> positionsNearBy = findPositionsNearBy(
                Objects.requireNonNull(findCheckerByPosition(from, checkers)), checkers, teamColor

        );

        log.info("OPPONENTS NEAR BY " + from + " : " + positionsNearBy);

        return positionsNearBy;
    }

    private List<Checker> findPositionsNearBy(Checker checkerFrom, List<Checker> checkers, String teamColor) {
        List<Checker> res = new ArrayList<>();
        int from = checkerFrom.getPosition();

        if (checkerFrom.getRow() % 2 == 0) {
            int leftDown = from - 3;
            int leftUp = from - 4;
            int rightDown = from + 5;
            int rightUp = from + 4;

            if (checkerFrom.getRow() != 0 && checkerFrom.getColumn() != 3) {
                Checker leftDownChecker = findCheckerByPosition(leftDown, checkers);
                if (leftDownChecker != null && !leftDownChecker.getColor().equals(teamColor)) {
                    res.add(leftDownChecker);
                }
            }
            if (checkerFrom.getRow() != 0 && checkerFrom.getColumn() != 0) {
                Checker leftUpChecker = findCheckerByPosition(leftUp, checkers);
                if (leftUpChecker != null && !leftUpChecker.getColor().equals(teamColor)) {
                    res.add(leftUpChecker);
                }
            }
            if (checkerFrom.getColumn() != 3) {
                Checker rightDownChecker = findCheckerByPosition(rightDown, checkers);
                if (rightDownChecker != null && !rightDownChecker.getColor().equals(teamColor)) {
                    res.add(rightDownChecker);
                }

            }
            if (checkerFrom.getColumn() != 0) {
                Checker rightUpChecker = findCheckerByPosition(rightUp, checkers);
                if (rightUpChecker != null && !rightUpChecker.getColor().equals(teamColor)) {
                    res.add(rightUpChecker);
                }
            }


        } else {
            int leftDown = from - 4;
            int leftUp = from - 5;
            int rightDown = from + 4;
            int rightUp = from + 3;

            if (checkerFrom.getColumn() != 3) {
                Checker leftDownChecker = findCheckerByPosition(leftDown, checkers);
                if (leftDownChecker != null && !leftDownChecker.getColor().equals(teamColor)) {
                    res.add(leftDownChecker);
                }
            }

            if (checkerFrom.getColumn() != 0 && checkerFrom.getRow() != 1) {
                Checker leftUpChecker = findCheckerByPosition(leftUp, checkers);
                if (leftUpChecker != null && !leftUpChecker.getColor().equals(teamColor)) {
                    res.add(leftUpChecker);
                }
            }
            if (checkerFrom.getRow() != 7 && checkerFrom.getColumn() != 3 && checkerFrom.getRow() != 6) {
                Checker rightDownChecker = findCheckerByPosition(rightDown, checkers);
                if (rightDownChecker != null && !rightDownChecker.getColor().equals(teamColor)) {
                    res.add(rightDownChecker);
                }
            }
            if (checkerFrom.getRow() != 7 && checkerFrom.getColumn() != 0) {
                Checker rightUpChecker = findCheckerByPosition(rightUp, checkers);
                if (rightUpChecker != null && !rightUpChecker.getColor().equals(teamColor)) {
                    res.add(rightUpChecker);
                }
            }
        }

        return res;
    }

    private void checkPosAndAddToList(List<Checker> checkers, String teamColor,
                                      List<Move> list, int from,
                                      int to, int secondTo) {

        int checkedPos = checkPos(from, to, teamColor, checkers);

        if (checkedPos != -1) {
            int priority = checkedPos == to ? 0 : 1;
            Move theSecondMove = new Move(from, secondTo, priority);
            list.add(theSecondMove);
        }
    }

    private int checkPos(int from, int to, String teamColor, List<Checker> checkers) {

        Checker checkerTo = findCheckerByPosition(to, checkers);

        if (checkerTo == null) {
            return to;
        } else if (checkerTo.getPosition() == to && checkerTo.getColor().equals(teamColor)) {
            return -1;
        } else if (checkerTo.getPosition() != to && !checkerTo.getColor().equals(teamColor)) {
            return checkAndGetPosAfterKill(from, to, teamColor, checkers);
        }


        return to;
    }

    private int checkAndGetPosAfterKill(int from, int to, String teamColor, List<Checker> checkers) {


        return -1;
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
