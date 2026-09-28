package com.isapet.exception;

public class MascotaNotFoundException extends RuntimeException {

    public MascotaNotFoundException(Long mascotaId) {
        super("No se encontró la mascota con identificador " + mascotaId + ".");
    }
}