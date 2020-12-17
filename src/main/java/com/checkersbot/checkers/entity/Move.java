package com.checkersbot.checkers.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Move {
    @NonNull private int from;
    @NonNull private int to;
}
