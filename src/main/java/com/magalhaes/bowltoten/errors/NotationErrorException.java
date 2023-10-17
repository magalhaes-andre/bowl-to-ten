package com.magalhaes.bowltoten.errors;

import com.magalhaes.bowltoten.service.NotationReadService;

public class NotationErrorException extends RuntimeException{

    private static final String NOTATION_ERROR_MESSAGE = "The selected file presentes error in its notation system, as in:\n";
    public NotationErrorException(String message) {
        super(NOTATION_ERROR_MESSAGE + message);
    }
}
