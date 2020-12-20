package com.checkersbot.checkers.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class MoveRequest {
    @NonNull private int[] move;
}
