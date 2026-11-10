package com.bin2dec.backend.service;

import org.springframework.stereotype.Service;

import com.bin2dec.backend.dto.ConversionResponse;
import com.bin2dec.backend.exception.InvalidBinaryException;

@Service 
public class ConverterService {
    
    public ConversionResponse convert(String binary) {
        // Fase 1: Validación estricta con precedencia
        if (binary == null || binary.isEmpty()) {
            throw new InvalidBinaryException("ERR_EMPTY", "La entrada no puede estar vacía.");
        } else if (!binary.matches("^[01]+$")) {
            throw new InvalidBinaryException("ERR_CHARS", "La cadena contiene caracteres inválidos. Solo se permiten 0 y 1.");
        } else if (binary.length() > 8) {
            throw new InvalidBinaryException("ERR_LENGTH", "La longitud máxima es de 8 bits.");
        }

        // Fase 2: Algoritmo de conversión
        int decimal = 0;
        
        for (int i = 0; i < binary.length(); i++) {
            decimal = (decimal << 1) + (binary.charAt(i) - '0');
        }

        return new ConversionResponse(decimal, binary);
    }
}
