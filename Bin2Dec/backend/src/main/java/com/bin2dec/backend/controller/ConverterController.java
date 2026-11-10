package com.bin2dec.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bin2dec.backend.dto.ConversionResponse;
import com.bin2dec.backend.service.ConverterService;

@RestController 
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ConverterController {
    private final ConverterService converterService;

    // Inyección de dependencias a través del constructor
    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @GetMapping("/convert")
    public ResponseEntity<ConversionResponse> convert(@RequestParam String binary) {
        return ResponseEntity.ok(converterService.convert(binary));
    }
}
