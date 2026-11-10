package com.bin2dec.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.bin2dec.backend.service.ConverterService;

@WebMvcTest(ConverterController.class)
@Import(ConverterService.class)
public class DefaultCorsControllerTest {
    private final MockMvc mockMvc;

    @Autowired
    DefaultCorsControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void deberiaPermitirOrigenesDeDesarrolloPorDefecto() throws Exception {
        String[] origins = {"http://localhost:5173", "http://localhost:3000"};

        for (String origin : origins) {
            mockMvc.perform(get("/api/v1/convert")
                    .param("binary", "0011")
                    .header(HttpHeaders.ORIGIN, origin))
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin));
        }
    }
}
