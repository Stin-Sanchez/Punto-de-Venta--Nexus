
package Services;

import Excepciones.BusinessException;
import dao.*;
import entities.DetalleVentas;
import entities.EstadosVentas;
import entities.Productos;
import entities.Ventas;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class VentasService {
    
        //Clase interna para manejar excepciones personalizadas
        public   class FacturaNotFoundException extends Exception {
            public FacturaNotFoundException(String message) {
            super(message);
    }
}

    private final VentasDAO ventasDAO;
    private final ProductosDAO productosDAO;
    private final ClientesDAO clientesDAO;

    public VentasService() {
        this.ventasDAO = new VentasDAOImpl();
        this.productosDAO = new ProductosDAOImpl();
        this.clientesDAO=new ClientesDAOImpl();

    }

    public void crearVenta(Ventas venta) throws Exception {

        if (venta != null && venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            venta.setEstado(EstadosVentas.FACTURADA);
            ventasDAO.crearVenta(venta);
            String numeroFactura = generarNumeroFacturaYActualizarEstado(venta.getId());
            venta.setNumFactura(numeroFactura);
            ventasDAO.actualizarVenta(venta);
            // Reducir stock por cada producto vendido
            for (DetalleVentas detalle : venta.getDetalles()) {
                productosDAO.reducirStock(detalle.getProductos().getId(), detalle.getCantidad());
            }
        } else {
            throw new IllegalArgumentException("La venta debe contener al menos un detalle.");
        }

    }
   public  void confirmarVenta(Long ventaId) throws BusinessException{
        Ventas venta = ventasDAO.buscarVentaPorId(ventaId);
        if (venta == null) {
            throw new BusinessException("Venta no encontrada");
        }

        if (venta.getEstado() != EstadosVentas.PENDIENTE) {
            throw new BusinessException("Solo se pueden confirmar ventas pendientes");
        }

        venta.setEstado(EstadosVentas.CONFIRMADA);
        ventasDAO.actualizarVenta(venta);
    }
    public void cancelarVenta(Long ventaId) throws BusinessException{
        Ventas venta = ventasDAO.buscarVentaPorId(ventaId);
        if (venta == null) {
            throw new BusinessException("Venta no encontrada");
        }

        if (venta.getEstado() == EstadosVentas.ENTREGADA) {
            throw new BusinessException("No se puede cancelar una venta ya entregada");
        }

        // Devolver stock si no estaba cancelada
        if (venta.getEstado() != EstadosVentas.CANCELADA) {
            for (DetalleVentas detalle : venta.getDetalles()) {
                Productos producto = detalle.getProductos();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productosDAO.update(producto);
            }
        }

        venta.setEstado(EstadosVentas.CANCELADA);
        ventasDAO.actualizarVenta(venta);
    }
    public void entregarVenta(Long ventaId) throws BusinessException{
        Ventas venta = ventasDAO.buscarVentaPorId(ventaId);
        if (venta == null) {
            throw new BusinessException("Venta no encontrada");
        }

        if (venta.getEstado() != EstadosVentas.CONFIRMADA) {
            throw new BusinessException("Solo se pueden entregar ventas confirmadas");
        }

        venta.setEstado(EstadosVentas.ENTREGADA);
        ventasDAO.actualizarVenta(venta);
    }

    public List<Ventas> obtenerVentas() throws Exception {
        List<Ventas> venta = ventasDAO.obtenerTodasLasVentas();
        
        if (venta.isEmpty()) {
            throw new NullPointerException("No hay ventas registradas.");
        }
        return venta;
    }

    public List<Ventas> listarPorCliente(Long clienteId){
        return ventasDAO.findByCliente(clienteId);
    }
    public  List<Ventas> listarPorEstado(EstadosVentas estado){
        return ventasDAO.findByEstado(estado);
    }
    public List<Ventas> listarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin){
        return ventasDAO.findByFecha(fechaInicio, fechaFin);
    }

    public Ventas buscarPorId(Long id) {
        return ventasDAO.buscarVentaPorId(id);
    }

    private void validarDetalle(DetalleVentas detalle) throws BusinessException {
        if (detalle.getProductos() == null) {
            throw new BusinessException("El producto es obligatorio en el detalle");
        }

        Productos producto = productosDAO.finById(detalle.getProductos().getId());
        if (producto == null || !producto.getActivo()) {
            throw new BusinessException("Producto no válido o inactivo");
        }

        if (detalle.getCantidad() <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }

        if (detalle.getPrecio() == null || detalle.getPrecio().compareTo(producto.getPrecio()) != 0) {
            detalle.setPrecio(producto.getPrecio());
        }

        detalle.setProductos(producto);
        detalle.calcularSubtotal();
    }

    public Ventas actualizarVenta(Ventas ventas) throws Exception {
        
        if (ventas == null || ventas.getId() == 0) {
            throw new Exception("La venta a actualizar es inválida o no tiene ID.");
        }
        return ventasDAO.actualizarVenta(ventas);
    }

    public boolean eliminarVenta(Long id) throws Exception {
        
         if (id == null || id <= 0) {
            throw new Exception("El ID de la venta es inválido para eliminar.");
        }
        return ventasDAO.eliminarVentaPorId(id);
    }

    public String ObtenerUltimaFactura() {
        return (String) ventasDAO.obtenerUltimoNumeroDeFactura();
    }

    public Ventas buscarVentaPorId(Long id) throws Exception {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la venta es inválido.");
        }

        Ventas venta = ventasDAO.buscarVentaPorId(id);
        if (venta == null) {
            throw new Exception("No se encontró ninguna venta con ID: " + id);
        }
        return venta;

    }

  /**
 * Calcula el total de la venta sumando todos los subtotales
 * 
 * @param subtotales Lista de subtotales de los productos
 * @return BigDecimal con el total calculado
 * @throws BusinessException si hay errores de validación
 */
public BigDecimal calcularTotalVenta(List<BigDecimal> subtotales) throws BusinessException {
    
    // Validaciones
    if (subtotales == null) {
        throw new BusinessException("La lista de subtotales no puede ser nula");
    }
    
    if (subtotales.isEmpty()) {
        throw new BusinessException("La lista de subtotales no puede estar vacía");
    }
    
    BigDecimal total = BigDecimal.ZERO;
    
    try {
        for (int i = 0; i < subtotales.size(); i++) {
            BigDecimal subtotal = subtotales.get(i);
            
            if (subtotal == null) {
                throw new BusinessException("El subtotal en la posición " + (i + 1) + " no puede ser nulo");
            }
            
            if (subtotal.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("El subtotal en la posición " + (i + 1) + " no puede ser negativo");
            }
            
            total = total.add(subtotal);
        }
        
        return total.setScale(2, RoundingMode.HALF_UP);
        
    } catch (BusinessException e) {
        throw e; // Re-lanzar excepciones de negocio
    } catch (Exception e) {
        throw new BusinessException("Error al calcular el total de la venta: " + e.getMessage());
    }
}

/**
 * Versión sobrecargada que acepta List<Double> para compatibilidad
 * 
 * @param subtotalesDouble Lista de subtotales como Double
 * @return BigDecimal con el total calculado
 * @throws BusinessException si hay errores de validación
 */
public BigDecimal calcularTotalVentas(List<Double> subtotalesDouble) throws BusinessException {
    
    if (subtotalesDouble == null) {
        throw new BusinessException("La lista de subtotales no puede ser nula");
    }
    
    // Convertir Double a BigDecimal para mayor precisión
    List<BigDecimal> subtotales = new ArrayList<>();
    for (int i = 0; i < subtotalesDouble.size(); i++) {
        Double subtotalDouble = subtotalesDouble.get(i);
        if (subtotalDouble == null) {
            throw new BusinessException("El subtotal en la posición " + (i + 1) + " no puede ser nulo");
        }
        subtotales.add(BigDecimal.valueOf(subtotalDouble));
    }
    
    return calcularTotalVenta(subtotales);
}

      
    
    public String formatearTotal(List<Double> total) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat formato = new DecimalFormat("#.##", symbols);
        return formato.format(total);
    }

    /**
     * Genera un número de factura único y actualiza el estado de la venta a "Facturada"
     *
     * @param ventaId ID de la venta a procesar
     * @return String con el número de factura generado
     * @throws BusinessException si hay errores de validación o negocio
     * @throws Exception si hay errores técnicos
     */
    public String generarNumeroFacturaYActualizarEstado(Long ventaId) throws BusinessException, Exception {

        // Validaciones de entrada
        if (ventaId == null || ventaId <= 0) {
            throw new IllegalArgumentException("El ID de la venta debe ser válido");
        }

        // Buscar la venta
        Ventas venta = ventasDAO.buscarVentaPorId(ventaId);
        if (venta == null) {
            throw new BusinessException("No se encontró la venta con ID: " + ventaId);
        }

        // Validar que la venta no tenga ya un número de factura
        if (venta.getNumFactura()!= null && !venta.getNumFactura().trim().isEmpty()) {
            throw new BusinessException("La venta ya tiene un número de factura asignado: " + venta.getNumFactura());
        }

        // Validar que la venta esté en estado válido para facturar
        if (venta.getEstado() == EstadosVentas.CANCELADA) {
            throw new BusinessException("No se puede facturar una venta cancelada");
        }

        try {
            // Generar el nuevo número de factura
            String ultimaFactura = ventasDAO.obtenerUltimoNumeroDeFactura();
            String nuevoNumeroFactura = generarSiguienteNumeroFactura(ultimaFactura);

            // Actualizar la venta con el número de factura y estado
            venta.setNumFactura(nuevoNumeroFactura);
            venta.setEstado(EstadosVentas.FACTURADA); // Asumiendo que tienes este estado en tu enum

            // Guardar los cambios
            ventasDAO.actualizarVenta(venta);

            return nuevoNumeroFactura;

        } catch (Exception e) {
            // Log del error y re-lanzar con contexto
            System.err.println("Error al generar número de factura para venta ID " + ventaId + ": " + e.getMessage());
            throw new Exception("Error al generar el número de factura: " + e.getMessage(), e);
        }
    }

    /**
     * Método auxiliar privado para generar el siguiente número de factura
     *
     * @param ultimaFactura Último número de factura en base de datos
     * @return String con el siguiente número de factura formateado
     */
    private String generarSiguienteNumeroFactura(String ultimaFactura) {
        try {
            if (ultimaFactura == null || ultimaFactura.trim().isEmpty()) {
                return "00000001";
            }

            // Convertir a número, incrementar y formatear
            long numeroFactura = Long.parseLong(ultimaFactura.trim()) + 1;
            return String.format("%08d", numeroFactura);

        } catch (NumberFormatException e) {
            // Si hay error en el formato, empezar desde 1
            System.err.println("Error al parsear último número de factura: " + ultimaFactura + ". Iniciando desde 00000001");
            return "00000001";
        }
    }
    
    public Ventas obtenerDetallesVenta(String numeroFactura)throws  FacturaNotFoundException{
             // Validación de entrada
    if (numeroFactura == null || numeroFactura.isBlank()) {
        throw new IllegalArgumentException("El número de factura no puede estar vacío.");
    }
    
      try {
        Ventas venta = ventasDAO.obtenerVentaConDetalles(numeroFactura);
        if (venta == null) {
            throw new FacturaNotFoundException("No se encontró la factura con número: " + numeroFactura);
        }
        return venta;
    } catch (RuntimeException e) {
        System.err.println("Error al obtener detalles de factura: " + e.getMessage());
        throw new FacturaNotFoundException("Error al buscar la factura: " + numeroFactura);
    }
            }

    /*public Ventas obtenerDatosFactura(String numeroFactura) throws FacturaNotFoundException {
     // Validación de entrada
    if (numeroFactura == null || numeroFactura.isBlank()) {
        throw new IllegalArgumentException("El número de factura no puede estar vacío.");
    }
    
    try {
        Ventas venta = ventasDAO.obtenerDatosClientePorFactura(numeroFactura);
        if (venta == null) {
            throw new FacturaNotFoundException("No se encontró la factura con número: " + numeroFactura);
        }
        return venta;
    } catch (RuntimeException e) {
        System.err.println("Error al obtener datos de factura: " + e.getMessage());
        throw new FacturaNotFoundException("Error al buscar la factura: " + numeroFactura);
    }
    }*/
    
    public int obtenerUltimaVenta() {
        return ventasDAO.obtenerIdUltimaVenta();
    }


}
