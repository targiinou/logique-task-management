package com.targinou.taskmanagement.commom.exception;


import com.targinou.taskmanagement.commom.enums.ResultType;
import com.targinou.taskmanagement.commom.view.GenericView;
import org.springframework.http.ResponseEntity;

public abstract class GenericException extends RuntimeException {

    protected ResultType resultType;

    protected GenericException(String message) {
        super(message);
    }

    protected GenericException(ResultType resultType) {
        super(resultType.getMessage());
        this.resultType = resultType;
    }

    protected GenericException(ResultType resultType, Exception e) {
        super(e.getMessage(), e);
        this.resultType = resultType;
    }

    public abstract ResponseEntity<GenericView<?>> asResponse();

}
