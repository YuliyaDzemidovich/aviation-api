package com.github.yuliyadzemidovich.aviationapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDto {

    private String message;
    private String errCode;
}
