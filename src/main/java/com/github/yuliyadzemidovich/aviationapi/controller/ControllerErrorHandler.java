package com.github.yuliyadzemidovich.aviationapi.controller;

import com.github.yuliyadzemidovich.aviationapi.controller.dto.ErrorDto;
import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.exception.UpstreamUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(UpstreamUnavailableException.class)
    public ResponseEntity<ResponseDto<Void>> handleUnavailable(UpstreamUnavailableException e) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(e.getMessage())
                .errCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE))
                .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ResponseDto<>(errorDto));
    }

}
