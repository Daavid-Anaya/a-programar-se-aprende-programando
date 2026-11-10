package com.bin2dec.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bin2dec.backend.service.ConverterService;

@WebMvcTest(ConverterController.class)
@Import(ConverterService.class)
class ConverterControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    ConverterControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void deberiaRetornarDecimalYPreservarCerosInicialesEnJson() throws Exception {
        mockMvc.perform(get("/api/v1/convert").param("binary", "0011"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.decimal").value(3))
                .andExpect(jsonPath("$.binary").value("0011"));
    }

    @Test
    void deberiaRetornarBadRequestCuandoBinaryEstaFaltando() throws Exception {
        mockMvc.perform(get("/api/v1/convert"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("ERR_EMPTY"))
                .andExpect(jsonPath("$.message").value("La entrada no puede estar vacía."));
    }

    @Test
    void deberiaRetornarBadRequestCuandoBinaryEstaVacio() throws Exception {
        mockMvc.perform(get("/api/v1/convert").param("binary", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("ERR_EMPTY"))
                .andExpect(jsonPath("$.message").value("La entrada no puede estar vacía."));
    }

    @Test 
    void deberiaRechazarCaracteresInvalidosEnBinary() throws Exception {
        String[] invalidInputs = {"101a", "102"};
        for (String binary : invalidInputs) {
            mockMvc.perform(get("/api/v1/convert").param("binary", binary))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errorCode").value("ERR_CHARS"))
                    .andExpect(jsonPath("$.message").value("La cadena contiene caracteres inválidos. Solo se permiten 0 y 1."));
        }
    }

    @Test 
    void deberiaRechazarBinaryMasLargoQueOchoBits() throws Exception {
        mockMvc.perform(get("/api/v1/convert").param("binary", "100000000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("ERR_LENGTH"))
                .andExpect(jsonPath("$.message").value("La longitud máxima es de 8 bits."));
    }

    @Test 
    void deberiaPriorizarCaracteresInvalidosSobreLongitud() throws Exception {
        mockMvc.perform(get("/api/v1/convert").param("binary", "1021111111"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("ERR_CHARS"))
                .andExpect(jsonPath("$.message").value("La cadena contiene caracteres inválidos. Solo se permiten 0 y 1."));
    }

    @Test 
    void deberiaRechazarSolicitudesPost() throws Exception {
        mockMvc.perform(post("/api/v1/convert").param("binary", "11"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value("ERR_METHOD"))
                .andExpect(jsonPath("$.message").value("Método no permitido. Use GET."));
    }
}
