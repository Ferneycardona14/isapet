package com.isapet.exception;

public class DuplicateNumeroIdentificacionException extends RuntimeException {

    public DuplicateNumeroIdentificacionException(String numeroIdentificacion) {
        super("El número de identificación " + numeroIdentificacion + " ya está registrado.");
    }
}