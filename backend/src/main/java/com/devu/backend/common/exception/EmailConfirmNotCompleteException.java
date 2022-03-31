package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class EmailConfirmNotCompleteException extends BusinessException {
    public EmailConfirmNotCompleteException() {
        super(Messages.NO_EMAIL_CONFIRM);
    }
}
