package com.github.yuliyadzemidovich.aviationapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto for airport info fetched from 3d party
 */
@AllArgsConstructor
@Data
public class AirportInfo {

    private final String info;
}
