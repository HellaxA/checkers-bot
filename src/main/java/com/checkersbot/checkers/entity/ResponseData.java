package com.checkersbot.checkers.entity;

import lombok.Data;

import java.util.Arrays;

@Data
public class ResponseData {
    private String status;
    private String whose_turn;
    private String winner;
    private Checker[] board;
    private double available_time;
    private MoveDto last_move;
    private Boolean is_started;
    private Boolean is_finished;

    @Override
    public String toString() {
        return "ResponseData{" +
                "\n\t\tstatus='" + status + '\'' +
                "\n\t\twhose_turn='" + whose_turn + '\'' +
                "\n\t\twinner='" + winner + '\'' +
                "\n\t\tboard=" + Arrays.toString(board) +
                "\n\t\tavailable_time='" + available_time + '\'' +
                "\n\t\tlast_move='" + last_move + '\'' +
                "\n\t\tis_started='" + is_started + '\'' +
                "\n\t\tis_finished='" + is_finished + '\'' +
                '}';
    }
}
