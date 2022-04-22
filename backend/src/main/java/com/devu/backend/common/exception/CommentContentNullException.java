package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class CommentContentNullException extends BusinessException{
    public CommentContentNullException() {
        super(Messages.COMMENT_CONTENT_EMPTY);
    }
}
