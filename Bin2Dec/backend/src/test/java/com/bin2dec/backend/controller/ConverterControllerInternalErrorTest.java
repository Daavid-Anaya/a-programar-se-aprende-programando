package com.bin2dec.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;

import com.bin2dec.backend.service.ConverterService;

@WebMvcTest(ConverterController.class)
@MockitoBean(types = ConverterService.class)
public class ConverterControllerInternalErrorTest {
    private final MockMvc mockMvc;
    private final ConverterService converterService;

    @Autowired
    ConverterControllerInternalErrorTest(MockMvc mockMvc, ConverterService converterService) {
        this.mockMvc = mockMvc;
        this.converterService = converterService;
    }

    @Test
    void shouldReturnGenericErrorWhenServiceFailsUnexpectedly() throws Exception {
        when(converterService.convert("11")).thenThrow(new IllegalStateException("Simulated internal failure"));

        mockMvc.perform(get("/api/v1/convert").param("binary", "11"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                          "errorCode": "ERR_INTERNAL",
                          "message": "Error interno del servidor."
                        }
                        """, JsonCompareMode.STRICT));
    }
}
