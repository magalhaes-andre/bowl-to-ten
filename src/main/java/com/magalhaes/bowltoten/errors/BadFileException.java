package com.magalhaes.bowltoten.errors;

public class BadFileException extends RuntimeException {

    private static final String BAD_FILE_MESSAGE = "The presented file has the following issues \n";
    public BadFileException(String message) {
        super(BAD_FILE_MESSAGE + message);
    }
}
