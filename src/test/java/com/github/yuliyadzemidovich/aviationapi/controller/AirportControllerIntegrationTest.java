package com.github.yuliyadzemidovich.aviationapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.service.AirportInfoProvider;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportControllerIntegrationTest {

    @Autowired
    private AirportController controller;
    @MockitoBean
    private AirportInfoProvider provider;

    @Test
    void lookupOk() {
        // given
        ObjectNode mockedResponseData = JsonNodeFactory.instance.objectNode()
                .put("icao", "KATL")
                .put("name", "ATLANTA INTL");
        when(provider.lookup("KATL")).thenReturn(new AirportInfo(mockedResponseData));

        // when
        ResponseDto<AirportInfo> response = controller.lookup("KATL");

        // then
        assertNotNull(response);
        assertNotNull(response.getData());

        JsonNode actual = response.getData().getInfo();
        assertEquals(mockedResponseData, actual);
    }
}
