package com.devu.backend.common.exception;

public class BusinessException extends RuntimeException{
    protected BusinessException(String message) {
        super(message);
    }
}
