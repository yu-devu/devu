package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class AlreadyLikedException extends BusinessException{
    public AlreadyLikedException() {
        super(Messages.ALREADY_LIKED);
    }
}
