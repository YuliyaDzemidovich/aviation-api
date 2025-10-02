package com.github.yuliyadzemidovich.aviationapi.service;

import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;

/**
 * Interface to fetch airport info from any 3d party
 */
public interface AirportService {

    /**
     * Lookup airport info on third party site by airport code
     * @param code code to lookup
     * @return Airport info if airport found
     */
    ResponseDto<AirportInfo> lookup(String code);
}
