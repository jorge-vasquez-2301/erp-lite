package com.debuggeandoideas.erp_lite.dtos;

import java.time.LocalDateTime;

public record BaseResponseWrapper<T>(
        T data,
        LocalDateTime date
) {
    public static <T> BaseResponseWrapper<T> of(T data) {
        return new BaseResponseWrapper<>(data, LocalDateTime.now());
    }
}