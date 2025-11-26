package Validador;

import entities.*;
import Excepciones.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VentasValidador {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static void validar(Ventas ventas) {
        Set<ConstraintViolation<Ventas>> violations = validator.validate(ventas);
        if (!violations.isEmpty()) {
            List<String> errores = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            throw new ValidationException("Errores de validación", errores);
        }
    }

    public static List<String> obtenerErrores(Ventas venta) {
        Set<ConstraintViolation<Ventas>> violations = validator.validate(venta);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
