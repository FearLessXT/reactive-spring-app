package com.demo.reactive.common;

import com.demo.reactive.constant.ErrorCode;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(String message, String code) {
        super(message, code, HttpStatus.NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        this(message, ErrorCode.NotFound.DEFAULT);
    }

}
