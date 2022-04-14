package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class UnlikedPostException extends BusinessException {
    public UnlikedPostException() {
        super(Messages.UNLIKE);
    }
}
