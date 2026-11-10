package com.bin2dec.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import com.bin2dec.backend.service.ConverterService;

@WebMvcTest(value = ConverterController.class, properties = "CORS_ALLOWED_ORIGINS=https://frontend.example.com")
@Import(ConverterService.class)
public class ConfiguredCorsControllerTest {
    private final MockMvc mockMvc;

    @Autowired
    ConfiguredCorsControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void deberiaPerimitirConfigurarOrigen() throws Exception {
        String origin = "https://frontend.example.com";

        mockMvc.perform(get("/api/v1/convert")
                .param("binary", "0011")
                .header(HttpHeaders.ORIGIN, origin))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin));
    }

    @Test
    void deberiaRechazarOrigenNoConfigurado() throws Exception {
        String origin = "https://untrusted.example.com";

        mockMvc.perform(get("/api/v1/convert")
                .param("binary", "0011")
                .header(HttpHeaders.ORIGIN, origin))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}
