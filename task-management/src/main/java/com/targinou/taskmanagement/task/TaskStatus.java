package com.targinou.taskmanagement.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    NOT_STARTED("Não iniciada"),
    IN_PROGRESS("Em progresso"),
    FINISHED("Finalizada"),
    ARCHIVED("Arquivada");

    private final String status;
}
