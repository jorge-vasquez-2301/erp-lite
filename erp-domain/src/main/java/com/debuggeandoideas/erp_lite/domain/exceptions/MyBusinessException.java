package com.debuggeandoideas.erp_lite.domain.exceptions;

public class MyBusinessException extends RuntimeException {

    public MyBusinessException(String message) {
        super(message);
    }
}