package com.checkersbot.checkers.entity;

import lombok.Data;

@Data
public class GameDto {
    private String status;
    private ResponseData data;

    @Override
    public String toString() {
        return "\nGameResponse{ \n" +
                "   status='" + status + "\n" +
                "   data=" + data +
                '}';
    }
}
