package com.bin2dec.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.bin2dec.backend.dto.ConversionResponse;
import com.bin2dec.backend.exception.InvalidBinaryException;

public class ConverterServiceTest {
    private final ConverterService service = new ConverterService();

    // Casos de exito
    @Test
    void deberiaConvertirBinarioEstándar() {
        // Actuar
        ConversionResponse response = service.convert("1010");

        // Verificar
        assertEquals(10, response.decimal());
        assertEquals("1010", response.binary());
    }

    @Test
    void deberiaConvertirBinarioConLimiteSuperior() {
        ConversionResponse response = service.convert("11111111");

        assertEquals(255, response.decimal());
        assertEquals("11111111", response.binary());
    }

    @Test 
    void deberiaConvertirBinarioConLimiteInferior() {
        ConversionResponse response = service.convert("0");

        assertEquals(0, response.decimal());
        assertEquals("0", response.binary());
    }

    @Test
    void deberiaConvertirBinarioConCerosALaIzquierda() {
        ConversionResponse response = service.convert("0011");

        assertEquals(3, response.decimal());
        assertEquals("0011", response.binary());
    }

    // Casos de error
    @Test
    void deberiaLanzarExcepciónCuandoEstáVacio() {
        InvalidBinaryException ex = assertThrows(InvalidBinaryException.class, () -> {
            service.convert("");
        });

        assertEquals("ERR_EMPTY", ex.getErrorCode());
    }

    @Test 
    void deberiaLanzarExceptionCuandoEsNulo() {
        InvalidBinaryException ex = assertThrows(InvalidBinaryException.class, () -> {
            service.convert(null);
        });

        assertEquals("ERR_EMPTY", ex.getErrorCode());
    }

    @Test 
    void deberiaLanzarExceptionConCaracteresInvalidos() {
        InvalidBinaryException ex = assertThrows(InvalidBinaryException.class, () -> {
            service.convert("102");
        });

        assertEquals("ERR_CHARS", ex.getErrorCode());
    }

    @Test 
    void deberiaLanzarExceptionConLongitudExcesiva() {
        InvalidBinaryException ex = assertThrows(InvalidBinaryException.class, () -> {
            service.convert("100000000");
        });

        assertEquals("ERR_LENGTH", ex.getErrorCode());
    }

    @Test 
    void deberiaLanzarExceptionCuandoHayPredencia() {
        InvalidBinaryException ex = assertThrows(InvalidBinaryException.class, () -> {
            service.convert("1021111111");
        });

        assertEquals("ERR_CHARS", ex.getErrorCode());
    }
}
