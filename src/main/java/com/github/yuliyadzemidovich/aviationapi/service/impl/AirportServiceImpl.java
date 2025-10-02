package com.github.yuliyadzemidovich.aviationapi.service.impl;

import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.service.AirportInfoProvider;
import com.github.yuliyadzemidovich.aviationapi.service.AirportService;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AirportService}. Uses provider to search for airport info
 */
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportInfoProvider provider;

    @Override
    public ResponseDto<AirportInfo> lookup(String code) {
        AirportInfo infoFetched = provider.lookup(code);
        return new ResponseDto<>(infoFetched);
    }
}
