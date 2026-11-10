package com.bin2dec.backend.exception;

public class InvalidBinaryException extends RuntimeException {
    private final String errorCode;

    public InvalidBinaryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
