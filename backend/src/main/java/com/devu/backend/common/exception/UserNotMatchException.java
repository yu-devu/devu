package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class UserNotMatchException extends BusinessException{
    public UserNotMatchException() {
        super(Messages.USER_NOT_MATCH);
    }
}
