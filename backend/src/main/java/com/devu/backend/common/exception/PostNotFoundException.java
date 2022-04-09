package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class PostNotFoundException extends BusinessException {
    public PostNotFoundException() {
        super(Messages.NO_POST);
    }
}
