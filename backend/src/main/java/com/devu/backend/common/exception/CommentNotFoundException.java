package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class CommentNotFoundException extends BusinessException{
    public CommentNotFoundException() {
        super(Messages.COMMENT_NOT_FOUND);
    }
}
