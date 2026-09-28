package com.isapet.exception;

public class AfiliadoNotFoundException extends RuntimeException {

    public AfiliadoNotFoundException(Long afiliadoId) {
        super("No se encontró el afiliado con identificador " + afiliadoId + ".");
    }
}