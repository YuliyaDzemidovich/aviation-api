package com.github.yuliyadzemidovich.aviationapi.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto for airport info fetched from 3d party
 */
@AllArgsConstructor
@Data
public class AirportInfo {

    private final JsonNode info;
}
