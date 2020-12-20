package com.checkersbot.checkers.entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class MoveServerResponse {
      @NonNull private String status;
      @NonNull private String data;
}
