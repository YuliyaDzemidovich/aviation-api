package com.github.yuliyadzemidovich.aviationapi.service;

import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;

/**
 * Fetches airports info from any implemented provider
 */
public interface AirportInfoProvider {

    /**
     * Lookup airport info by code
     * @param code airport code to search by
     * @return airport info if found
     */
    AirportInfo lookup(String code);
}
