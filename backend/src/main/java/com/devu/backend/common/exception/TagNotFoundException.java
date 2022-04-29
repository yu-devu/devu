package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class TagNotFoundException extends BusinessException{
    public TagNotFoundException() {
        super(Messages.TAG_NOT_FOUND);
    }
}
