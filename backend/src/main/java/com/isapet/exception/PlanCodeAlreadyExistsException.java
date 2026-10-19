package com.isapet.exception;

public class PlanCodeAlreadyExistsException extends RuntimeException {

    public PlanCodeAlreadyExistsException(String codigo) {
        super("Ya existe un plan con el código " + codigo + ".");
    }
}
