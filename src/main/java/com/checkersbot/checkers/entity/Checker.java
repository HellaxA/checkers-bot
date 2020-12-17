package com.checkersbot.checkers.entity;

import lombok.Data;

@Data
public class Checker {
    private String color;
    private int row;
    private int column;
    private Boolean king;
    private int position;

    @Override
    public String toString() {
        return "\n\t\t\tChecker{\n" +
                "\t\t\t\tcolor='" + color + "\n" +
                "\t\t\t\trow=" + row + "\n" +
                "\t\t\t\tcolumn=" + column + "\n" +
                "\t\t\t\tking=" + king + "\n" +
                "\t\t\t\tposition=" + position + "\n" +
                "\t\t\t}";
    }
}
