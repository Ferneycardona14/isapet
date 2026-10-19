package com.isapet.exception;

public class PlanInactiveException extends RuntimeException {

    public PlanInactiveException(Long planId) {
        super("No se pueden publicar versiones para el plan inactivo con id " + planId + ".");
    }
}
