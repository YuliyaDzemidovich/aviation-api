package com.github.yuliyadzemidovich.aviationapi.controller;

import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.service.AirportService;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService service;

    @GetMapping("/lookup")
    public ResponseDto<AirportInfo> lookup(@RequestParam String code) {
        return service.lookup(code);
    }
}
