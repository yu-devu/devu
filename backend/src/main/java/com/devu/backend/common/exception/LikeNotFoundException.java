package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class LikeNotFoundException extends BusinessException{
    public LikeNotFoundException() {
        super(Messages.NO_LIKE);
    }
}
