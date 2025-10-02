package com.github.yuliyadzemidovich.aviationapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic response object
 * @param <T> class to wrap with this response obj
 */
@AllArgsConstructor
@Data
public class ResponseDto<T> {

    private T data;
    private ErrorDto error;

    public ResponseDto(T data) {
        this.data = data;
    }
}
