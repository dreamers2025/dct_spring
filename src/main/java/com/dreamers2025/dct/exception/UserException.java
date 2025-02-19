package com.dreamers2025.dct.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }

    public UserException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }
}

