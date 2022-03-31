package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(Messages.NO_USER_MESSAGE);
    }
}
