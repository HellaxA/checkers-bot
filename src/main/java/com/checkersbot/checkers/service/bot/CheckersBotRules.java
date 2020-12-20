package com.checkersbot.checkers.service.bot;

import com.checkersbot.checkers.entity.Checker;
import com.checkersbot.checkers.entity.Move;

import java.util.List;

public interface CheckersBotRules {

    List<Move> getPseudoAvailableMoves(String teamColor, Checker[] checkers);

    List<Move> getAllAvailableMoves(Checker[] checkers, String teamColor);
}
