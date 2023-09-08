package com.targinou.taskmanagement.commom.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultType {

    OK("OK"),
    ERROR("ERROR"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    AUTHENTICATION_ERROR("AUTHENTICATION_ERROR"),
    AUTHORIZATION_ERROR("AUTHORIZATION_ERROR");

    private final String message;

}
