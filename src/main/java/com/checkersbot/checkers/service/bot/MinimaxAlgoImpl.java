package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.Checker;
import com.checkersbot.checkers.entity.Move;
import org.springframework.stereotype.Component;

@Component
public class MinimaxAlgoImpl implements MinimaxAlgo {

    @Override
    public Checker[] makePseudoMoves(Move theMove, Checker[] checkers) {

        Checker[] checkersWithPseudoMove = new Checker[checkers.length];

        for (int i = 0; i < checkers.length; i++) {
            if (theMove.getFrom() == checkers[i].getPosition()) {
                Checker oldChecker = checkers[i];
                Checker newChecker = new Checker();

                newChecker.setColor(oldChecker.getColor());
                newChecker.setPosition(theMove.getTo());
                newChecker.setColumn(findColByPos(theMove.getTo()));
                newChecker.setRow(findRowByPos(theMove.getTo()));
                newChecker.setKing(oldChecker.getKing());

                checkersWithPseudoMove[i] = newChecker;

            } else {
                checkersWithPseudoMove[i] = checkers[i];
            }

        }

        return checkersWithPseudoMove;
    }

    public int findRowByPos(int pos) {

        int row = 0;

        while (pos > 4) {
            pos -= 4;
            row++;
        }

        return row;
    }

    public int findColByPos(int pos) {

        int col = 0;

        if (pos % 4 == 1) {
            col = 0;
        } else if (pos % 4 == 2) {
            col = 1;
        } else if (pos % 4 == 3) {
            col = 2;
        } else if (pos % 4 == 0) {
            col = 3;
        }

        return col;
    }
}
