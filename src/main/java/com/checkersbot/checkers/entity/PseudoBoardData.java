package com.checkersbot.checkers.entity;

import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;

@Data
public class PseudoBoardData {
    @NonNull private Move move;
    @NonNull private Checker[] pseudoCheckers;
   private int score;

    @Override
    public String toString() {
        return "\nPseudoMovesData{" +
                "\ninitial move=" + move +
                "\nscore=" + score +
                "\n, pseudoCheckers=" + Arrays.asList(pseudoCheckers) +
                "\n}";
    }
}
