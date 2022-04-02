package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class EmailAuthKeyNotEqualException extends BusinessException {
    public EmailAuthKeyNotEqualException() {
        super(Messages.WRONG_AUTH_KEY);
    }
}
