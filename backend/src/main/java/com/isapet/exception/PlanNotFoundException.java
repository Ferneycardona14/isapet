package com.isapet.exception;

public class PlanNotFoundException extends RuntimeException {

    public PlanNotFoundException(Long planId) {
        super("No se encontró el plan con id " + planId + ".");
    }
}
