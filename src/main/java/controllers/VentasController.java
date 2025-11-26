
package controllers;
import Excepciones.BusinessException;
import Services.VentasService;
import Services.VentasService.FacturaNotFoundException;
import entities.DetalleVentas;
import entities.Estados;
import entities.EstadosVentas;
import entities.Ventas;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VentasController {

    private static final Logger logger = Logger.getLogger(VentasController.class.getName());
    private final VentasService ventasService;

    // Constructor con inyección de dependencia manual
    public VentasController() {
        this.ventasService = new VentasService();
    }

    // Constructor para testing (permite inyectar mock)
    public VentasController(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    /**
     * Generar una nueva factura/venta
     *
     * @param venta La venta a generar
     * @return OperationResult con el resultado de la operación
     */
    public OperationResult<Ventas> generarVenta(Ventas venta) {
        try {
            // Validaciones previas
            if (venta == null) {
                return OperationResult.failure("La venta no puede ser nula");
            }

            if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
                return OperationResult.failure("La venta debe contener al menos un detalle");
            }

            // Validar detalles de venta
            String validationError = validarDetallesVenta(venta.getDetalles());
            if (validationError != null) {
                return OperationResult.failure(validationError);
            }
          
            ventasService.crearVenta(venta);
             // generarNumeroFactura(venta.getId());
            logger.info("Venta generada exitosamente con ID: " + venta.getId());

            return OperationResult.success(venta, "Venta generada exitosamente");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Error de validación al generar factura", e);
            return OperationResult.failure("Datos inválidos: " + e.getMessage());
        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Error de negocio al generar factura", e);
            return OperationResult.failure("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al generar factura", e);
            return OperationResult.failure("Error interno: " + e.getMessage());
        }
    }

    /**
     * Generar número de factura y actualizar estado de la venta
     *
     * @param ventaId ID de la venta a facturar
     * @return OperationResult con el número de factura generado
     */
    public OperationResult<String> generarNumeroFactura(Long ventaId) {
        try {
            if (ventaId == null || ventaId <= 0) {
                return OperationResult.failure("El ID de la venta debe ser un número positivo válido");
            }

            String numeroFactura = ventasService.generarNumeroFacturaYActualizarEstado(ventaId);
            logger.info("Número de factura generado: " + numeroFactura + " para venta ID: " + ventaId);

            return OperationResult.success(numeroFactura, "Número de factura generado exitosamente: " + numeroFactura);

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Parámetro inválido al generar factura para venta ID: " + ventaId, e);
            return OperationResult.failure("Parámetro inválido: " + e.getMessage());
        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Error de negocio al generar factura para venta ID: " + ventaId, e);
            return OperationResult.failure(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al generar factura para venta ID: " + ventaId, e);
            return OperationResult.failure("Error interno al generar el número de factura");
        }
    }

    /**
     * Obtener todas las ventas registradas
     *
     * @return OperationResult con la lista de ventas
     */
    public OperationResult<List<Ventas>> obtenerTodasLasVentas() {
        try {
            List<Ventas> ventas = ventasService.obtenerVentas();
            logger.info("Se obtuvieron " + ventas.size() + " ventas");

            return OperationResult.success(ventas, "Ventas obtenidas exitosamente");

        } catch (NullPointerException e) {
            logger.info("No hay ventas registradas");
            return OperationResult.success(List.of(), "No hay ventas registradas");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener ventas", e);
            return OperationResult.failure("Error al obtener las ventas: " + e.getMessage());
        }
    }

    /**
     * Buscar una venta por su ID
     *
     * @param id ID de la venta a buscar
     * @return OperationResult con la venta encontrada
     */
    public OperationResult<Ventas> buscarVentaPorId(Long id) {
        try {
            if (id == null || id <= 0) {
                return OperationResult.failure("El ID debe ser un número positivo válido");
            }

            Ventas venta = ventasService.buscarVentaPorId(id);
            logger.info("Venta encontrada con ID: " + id);

            return OperationResult.success(venta, "Venta encontrada exitosamente");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "ID inválido: " + id, e);
            return OperationResult.failure("ID inválido: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Venta no encontrada con ID: " + id, e);
            return OperationResult.failure("No se encontró la venta: " + e.getMessage());
        }
    }

    /**
     * Confirmar una venta pendiente
     *
     * @param ventaId ID de la venta a confirmar
     * @return OperationResult del resultado de la operación
     */
    public OperationResult<String> confirmarVenta(Long ventaId) {
        try {
            if (ventaId == null || ventaId <= 0) {
                return OperationResult.failure("El ID debe ser un número positivo válido");
            }

            ventasService.confirmarVenta(ventaId);
            logger.info("Venta confirmada exitosamente. ID: " + ventaId);

            return OperationResult.success("OK", "Venta confirmada exitosamente");

        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Error de negocio al confirmar venta ID: " + ventaId, e);
            return OperationResult.failure(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al confirmar venta ID: " + ventaId, e);
            return OperationResult.failure("Error interno al confirmar la venta");
        }
    }

    /**
     * Cancelar una venta
     *
     * @param ventaId ID de la venta a cancelar
     * @return OperationResult del resultado de la operación
     */
    public OperationResult<String> cancelarVenta(Long ventaId) {
        try {
            if (ventaId == null || ventaId <= 0) {
                return OperationResult.failure("El ID debe ser un número positivo válido");
            }

            ventasService.cancelarVenta(ventaId);
            logger.info(() -> "Venta cancelada exitosamente. ID: " + ventaId);

            return OperationResult.success("OK", "Venta cancelada exitosamente");

        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Error de negocio al cancelar venta ID: " + ventaId, e);
            return OperationResult.failure(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al cancelar venta ID: " + ventaId, e);
            return OperationResult.failure("Error interno al cancelar la venta");
        }
    }

    /**
     * Entregar una venta confirmada
     *
     * @param ventaId ID de la venta a entregar
     * @return OperationResult del resultado de la operación
     */
    public OperationResult<String> entregarVenta(Long ventaId) {
        try {
            if (ventaId == null || ventaId <= 0) {
                return OperationResult.failure("El ID debe ser un número positivo válido");
            }

            ventasService.entregarVenta(ventaId);
            logger.info(() -> "Venta entregada exitosamente. ID: " + ventaId);

            return OperationResult.success("OK", "Venta entregada exitosamente");

        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Error de negocio al entregar venta ID: " + ventaId, e);
            return OperationResult.failure(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al entregar venta ID: " + ventaId, e);
            return OperationResult.failure("Error interno al entregar la venta");
        }
    }

    /**
     * Actualizar una venta existente
     *
     * @param venta La venta con los datos actualizados
     * @return OperationResult con la venta actualizada
     */
    public OperationResult<Ventas> actualizarVenta(Ventas venta) {
        try {
            if (venta == null) {
                return OperationResult.failure("La venta no puede ser nula");
            }

            if (venta.getId() == null || venta.getId() <= 0) {
                return OperationResult.failure("La venta debe tener un ID válido para actualizar");
            }

            Ventas ventaActualizada = ventasService.actualizarVenta(venta);
            logger.info(() -> "Venta actualizada exitosamente. ID: " + venta.getId());

            return OperationResult.success(ventaActualizada, "Venta actualizada exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar venta", e);
            return OperationResult.failure("Error al actualizar la venta: " + e.getMessage());
        }
    }

    /**
     * Eliminar una venta
     *
     * @param id ID de la venta a eliminar
     * @return OperationResult del resultado de la operación
     */
    public OperationResult<Boolean> eliminarVenta(Long id) {
        try {
            if (id == null || id <= 0) {
                return OperationResult.failure("El ID debe ser un número positivo válido");
            }

            boolean eliminado = ventasService.eliminarVenta(id);

            if (eliminado) {
                logger.info(() -> "Venta eliminada exitosamente. ID: " + id);
                return OperationResult.success(true, "Venta eliminada exitosamente");
            } else {
                logger.warning(() -> "No se pudo eliminar la venta con ID: " + id);
                return OperationResult.failure("No se pudo eliminar la venta");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al eliminar venta ID: " + id, e);
            return OperationResult.failure("Error al eliminar la venta: " + e.getMessage());
        }
    }

    /**
     * Listar ventas por cliente
     *
     * @param clienteId ID del cliente
     * @return OperationResult con la lista de ventas del cliente
     */
    public OperationResult<List<Ventas>> listarVentasPorCliente(Long clienteId) {
        try {
            if (clienteId == null || clienteId <= 0) {
                return OperationResult.failure("El ID del cliente debe ser un número positivo válido");
            }

            List<Ventas> ventas = ventasService.listarPorCliente(clienteId);
            logger.info(() -> "Se encontraron " + ventas.size() + " ventas para el cliente ID: " + clienteId);

            return OperationResult.success(ventas, "Ventas del cliente obtenidas exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener ventas por cliente ID: " + clienteId, e);
            return OperationResult.failure("Error al obtener ventas del cliente: " + e.getMessage());
        }
    }

    /**
     * Listar ventas por estado
     *
     * @param estado Estado de las ventas a buscar
     * @return OperationResult con la lista de ventas por estado
     */
    public OperationResult<List<Ventas>> listarVentasPorEstado(EstadosVentas estado) {
        try {
            if (estado == null) {
                return OperationResult.failure("El estado no puede ser nulo");
            }

            List<Ventas> ventas = ventasService.listarPorEstado(estado);
            logger.info(() -> "Se encontraron " + ventas.size() + " ventas con estado: " + estado);

            return OperationResult.success(ventas, "Ventas por estado obtenidas exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener ventas por estado: " + estado, e);
            return OperationResult.failure("Error al obtener ventas por estado: " + e.getMessage());
        }
    }

    /**
     * Listar ventas por rango de fechas
     *
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return OperationResult con la lista de ventas en el rango de fechas
     */
    public OperationResult<List<Ventas>> listarVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            if (fechaInicio == null || fechaFin == null) {
                return OperationResult.failure("Las fechas de inicio y fin son obligatorias");
            }

            if (fechaInicio.isAfter(fechaFin)) {
                return OperationResult.failure("La fecha de inicio no puede ser posterior a la fecha de fin");
            }

            List<Ventas> ventas = ventasService.listarPorFecha(fechaInicio, fechaFin);
            logger.info("Se encontraron " + ventas.size() + " ventas entre " + fechaInicio + " y " + fechaFin);

            return OperationResult.success(ventas, "Ventas por fecha obtenidas exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener ventas por fecha", e);
            return OperationResult.failure("Error al obtener ventas por fecha: " + e.getMessage());
        }
    }

    /**
     * Obtener detalles de una factura por su número
     *
     * @param numeroFactura Número de la factura
     * @return OperationResult con los detalles de la factura
     */
    public OperationResult<Ventas> obtenerDetallesFactura(String numeroFactura) {
        try {
            if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
                return OperationResult.failure("El número de factura es obligatorio");
            }

            Ventas venta = ventasService.obtenerDetallesVenta(numeroFactura.trim());
            logger.info("Detalles de factura obtenidos para: " + numeroFactura);

            return OperationResult.success(venta, "Detalles de factura obtenidos exitosamente");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Número de factura inválido: " + numeroFactura, e);
            return OperationResult.failure("Número de factura inválido: " + e.getMessage());
        } catch (FacturaNotFoundException e) {
            logger.log(Level.WARNING, "Factura no encontrada: " + numeroFactura, e);
            return OperationResult.failure(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener detalles de factura: " + numeroFactura, e);
            return OperationResult.failure("Error interno al buscar la factura");
        }
    }

    /**
     * Obtener el último número de factura generado
     *
     * @return String con el último número de factura
     */
    public String obtenerUltimaFactura() {
        String fac=null;
        try {
            fac  = ventasService.ObtenerUltimaFactura();
            logger.info("Última factura obtenida: " + fac);
               
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener última factura", e);
        }
        return "sexo "+fac;
    }

    /**
     * Obtener el ID de la última venta registrada
     *
     * @return OperationResult con el ID de la última venta
     */
    public OperationResult<Integer> obtenerUltimaVenta() {
        try {
            int ultimaVenta = ventasService.obtenerUltimaVenta();
            logger.info("Última venta obtenida. ID: " + ultimaVenta);

            return OperationResult.success(ultimaVenta, "ID de última venta obtenido exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener última venta", e);
            return OperationResult.failure("Error al obtener la última venta: " + e.getMessage());
        }
    }
    
    /**
 * Calcular el total de una venta basado en los subtotales
 * 
 * @param subtotales Lista de subtotales (acepta tanto Double como BigDecimal)
 * @return BigDecimal con el total calculado o null si hay error
 */
public BigDecimal calcularTotalVenta(List<? extends Number> subtotales) {
    try {
        if (subtotales == null || subtotales.isEmpty()) {
            logger.warning("Lista de subtotales vacía o nula");
            return BigDecimal.ZERO;
        }
        
        // Convertir a lista de BigDecimal para mayor precisión
        List<BigDecimal> subtotalesBD = new ArrayList<>();
        for (Number subtotal : subtotales) {
            if (subtotal == null) {
                logger.warning("Subtotal nulo encontrado en la lista");
                continue; // Omitir valores nulos
            }
            
            BigDecimal bd;
            if (subtotal instanceof BigDecimal) {
                bd = (BigDecimal) subtotal;
            } else if (subtotal instanceof Double) {
                bd = BigDecimal.valueOf((Double) subtotal);
            } else {
                bd = new BigDecimal(subtotal.toString());
            }
            
            subtotalesBD.add(bd);
        }
        
        BigDecimal total = ventasService.calcularTotalVenta(subtotalesBD);
        logger.info("Total calculado exitosamente: " + total);
        
        return total;
        
    } catch (BusinessException e) {
        logger.log(Level.WARNING, "Error de negocio al calcular total", e);
        return BigDecimal.ZERO; // Retornar 0 en caso de error para evitar NPE en UI
    } catch (Exception e) {
        logger.log(Level.SEVERE, "Error inesperado al calcular total de venta", e);
        return BigDecimal.ZERO; // Retornar 0 en caso de error para evitar NPE en UI
    }
}




/**
 * Obtener el total formateado como String para mostrar en la UI
 * 
 * @param subtotales Lista de subtotales
 * @return String con el total formateado (ej: "25.50")
 */
public String obtenerTotalFormateado(List<Double> subtotales) {
    try {
        BigDecimal total = calcularTotalVenta(subtotales);
        return total.setScale(2, RoundingMode.HALF_UP).toString();
    } catch (Exception e) {
        logger.log(Level.WARNING, "Error al formatear total", e);
        return "0.00";
    }
}

   
    /**
     * Formatear un valor total para mostrar
     *
     * @param total Valor total a formatear
     * @return OperationResult con el total formateado
     */
    public OperationResult<String> formatearsubTotal(List<Double> total) {
        try {
            if (total.size() < 0) {
                return OperationResult.failure("El total no puede ser negativo");
            }

            String totalFormateado = ventasService.formatearTotal(total);
            logger.fine("Total formateado: " + totalFormateado);

            return OperationResult.success(totalFormateado, "Total formateado exitosamente");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al formatear total", e);
            return OperationResult.failure("Error al formatear el total: " + e.getMessage());
        }
    }

    // Métodos utilitarios privados

    /**
     * Validar los detalles de una venta
     *
     * @param detalles Lista de detalles a validar
     * @return String con mensaje de error o null si es válido
     */
    private String validarDetallesVenta(List<DetalleVentas> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return "La venta debe tener al menos un detalle";
        }

        for (int i = 0; i < detalles.size(); i++) {
            DetalleVentas detalle = detalles.get(i);

            if (detalle == null) {
                return "El detalle " + (i + 1) + " no puede ser nulo";
            }

            if (detalle.getProductos() == null) {
                return "El producto del detalle " + (i + 1) + " es obligatorio";
            }

            if (detalle.getCantidad() <= 0) {
                return "La cantidad del detalle " + (i + 1) + " debe ser mayor a cero";
            }

            if (detalle.getPrecio() == null || detalle.getPrecio().doubleValue() < 0) {
                return "El precio del detalle " + (i + 1) + " no puede ser negativo";
            }
        }

        return null; // Todo válido
    }

    /**
     * Clase interna para encapsular el resultado de las operaciones
     *
     * @param <T> Tipo de dato del resultado
     */
    public static class OperationResult<T> {
        private final boolean success;
        private final String message;
        private final T data;
        private final String error;

        private OperationResult(boolean success, String message, T data, String error) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.error = error;
        }

        public static <T> OperationResult<T> success(T data, String message) {
            return new OperationResult<>(true, message, data, null);
        }

        public static <T> OperationResult<T> failure(String error) {
            return new OperationResult<>(false, null, null, error);
        }

        // Getters
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        public String getError() {
            return error;
        }

        public boolean hasData() {
            return data != null;
        }
    }


}


    
