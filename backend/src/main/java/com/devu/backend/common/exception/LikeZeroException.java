package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class LikeZeroException extends BusinessException {
    public LikeZeroException() {
        super(Messages.LIKE_ZERO);
    }
}
