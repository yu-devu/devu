package com.devu.backend.common.exception;

import com.devu.backend.common.Messages;

public class OrderNotFoundException extends BusinessException{
    public OrderNotFoundException() {
        super(Messages.OREDER_NOT_FOUND);
    }
}
