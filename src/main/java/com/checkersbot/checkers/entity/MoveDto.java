package com.checkersbot.checkers.entity;

import com.checkersbot.checkers.utils.IOUtils;
import lombok.Data;

@Data
public class MoveDto {
    private String player;
    private int[][] last_moves;

    @Override
    public String toString() {
        return "\n\t\t\t\tMove{" +
                "\n\t\t\t\t\tplayer='" + player + '\'' +
                "\n\t\t\t\t\tlast_moves=" + IOUtils.getTwoDimensionalArrayInString(last_moves) +
                "\n\t\t\t\t}";
    }
}
