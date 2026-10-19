package com.isapet.exception;

import java.time.LocalDate;

public class VersionPlanConflictException extends RuntimeException {

    public VersionPlanConflictException(LocalDate vigenteDesde, LocalDate ultimaVigencia) {
        super("La fecha " + vigenteDesde
                + " debe ser posterior a la última fecha de vigencia: " + ultimaVigencia + ".");
    }

    public VersionPlanConflictException(String message) {
        super(message);
    }
}
