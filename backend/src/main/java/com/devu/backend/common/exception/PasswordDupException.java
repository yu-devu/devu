package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class PasswordDupException extends BusinessException{
    public PasswordDupException() {
        super(Messages.DUP_PASSWORD);
    }
}
