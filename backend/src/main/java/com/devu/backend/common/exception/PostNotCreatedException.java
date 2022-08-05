package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class PostNotCreatedException extends BusinessException {
    public PostNotCreatedException() {
        super(Messages.POST_NOT_CREATED);
    }
}
