package com.projects.labyrinth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitAnswerDto {
    private Long playerId;
    private String answer;
}
