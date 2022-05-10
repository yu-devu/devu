package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class ReCommentNotFoundException extends BusinessException{
    public ReCommentNotFoundException() { super(Messages.RECOMMENT_NOT_FOUND);}
}
