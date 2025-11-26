package controllers;

import Excepciones.BusinessException;
import Excepciones.NotFoundExceptions;

import javax.swing.*;
import java.util.function.Supplier;

public class ControllerHelper {

    public static <T> T executeExceptions(Supplier<T> operacion, T valorPorDefecto) {
        try {
            return operacion.get();
        } catch (NotFoundExceptions e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de negocio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + e.getMessage(), "Error grave", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // o usa logger
        }
        return valorPorDefecto;
    }
}
