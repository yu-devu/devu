package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class PasswordNotSameException extends BusinessException {
    public PasswordNotSameException() {
        super(Messages.PASSWORD_NOT_EQUAL);
    }
}
