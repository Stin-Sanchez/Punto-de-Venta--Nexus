package Excepciones;

import java.util.List;

public class ValidationException extends RuntimeException{
    private List<String> errores;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<String> errores) {
        super(message);
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
