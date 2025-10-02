package com.github.yuliyadzemidovich.aviationapi.service.impl;

import com.github.yuliyadzemidovich.aviationapi.service.AirportInfoProvider;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AirportInfoProviderAviationApiImpl implements AirportInfoProvider {

    @Override
    public AirportInfo lookup(String code) {
        log.info("Calling aviationapi.com - lookup for code {}", code);
        // todo impl real 3d party call to https://aviationapi.com
        return new AirportInfo("stub response for code=" + code);
    }
}
