package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;
import org.aspectj.bridge.Message;

public class AlreadyExistsEmailException extends BusinessException {
    public AlreadyExistsEmailException() {
        super(Messages.ALREADY_EXIST_EMAIL);
    }
}
