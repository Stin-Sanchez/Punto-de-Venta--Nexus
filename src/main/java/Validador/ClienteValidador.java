package Validador;

import entities.Clientes;
import Excepciones.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

public class ClienteValidador {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static void validar(Clientes cliente) {
        Set<ConstraintViolation<Clientes>> violations = validator.validate(cliente);
        if (!violations.isEmpty()) {
            List<String> errores = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            throw new ValidationException("Errores de validación", errores);
        }
    }

    public static List<String> obtenerErrores(Clientes cliente) {
        Set<ConstraintViolation<Clientes>> violations = validator.validate(cliente);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
