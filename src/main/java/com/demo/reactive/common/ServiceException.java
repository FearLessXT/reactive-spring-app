package com.demo.reactive.common;

import com.demo.reactive.constant.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private String code;
    private HttpStatus httpStatus;

    public ServiceException(String message, String code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ServiceException(String message, HttpStatus httpStatus) {
        this(message, ErrorCode.INTERNAL_SERVER_ERROR, httpStatus);
    }

    public ServiceException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
