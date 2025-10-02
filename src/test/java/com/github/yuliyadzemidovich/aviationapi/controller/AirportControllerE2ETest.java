package com.github.yuliyadzemidovich.aviationapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.yuliyadzemidovich.aviationapi.controller.dto.ResponseDto;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Tag("e2e")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportControllerE2ETest {

    @Autowired
    private AirportController controller;

    @Test
    void lookupOk() {
        // given 3d party endpoint is available

        // when
        ResponseDto<AirportInfo> response = controller.lookup("KATL");

        // then
        assertNotNull(response);
        assertNotNull(response.getData());

        JsonNode actual = response.getData().getInfo();

        String expectedName = "HARTSFIELD - JACKSON ATLANTA INTL";
        assertEquals(expectedName, actual.get("KATL").get(0).get("facility_name").asText());
    }
}
