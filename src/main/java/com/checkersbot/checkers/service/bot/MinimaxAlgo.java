package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.Checker;
import com.checkersbot.checkers.entity.Move;

public interface MinimaxAlgo {

    Checker[] makePseudoMoves(Move theMove, Checker[] checkers);
}
