package dao;
import entities.Estados;
import entities.EstadosVentas;
import entities.Ventas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface VentasDAO {
    
     /**
     * Crea un nuevo registro de venta en el sistema.
     *
     * @param venta El objeto {@code Ventas} que contiene los datos de la nueva venta.
     * @return El objeto {@code Ventas} recién creado, posiblemente con campos actualizados como el ID generado.
     */
    public Ventas crearVenta(Ventas venta);

    /**
     * Busca un registro de venta por su identificador único.
     *
     * @param id El identificador único (Long) del registro de venta que se desea obtener.
     * @return El objeto {@code Ventas} con el ID coincidente, o {@code null} si no se encuentra.
     */
    Ventas buscarVentaPorId(Long id);
    /**
     * Obtiene todos los registros de ventas almacenados en el sistema.
     *
     * @return Una lista de todos los objetos {@code Ventas} presentes en el sistema.
     */
    public List<Ventas> obtenerTodasLasVentas();

    /**
     * Obtiene el ID de la última venta registrada en el sistema.
     * Esto puede ser útil para determinar la venta más reciente.
     *
     * @return El ID (int) del último registro de venta.
     */
    public int obtenerIdUltimaVenta();

    /**
     * Obtiene el último número de factura generado por el sistema.
     *
     * @return El último número de factura (String) generado.
     */
    public String obtenerUltimoNumeroDeFactura();

    /**
     * Actualiza un registro de venta existente en el sistema.
     *
     * @param venta El objeto {@code Ventas} que contiene los datos actualizados. Este objeto debe tener un ID válido de un registro existente.
     * @return El objeto {@code Ventas} actualizado.
     */
    Ventas actualizarVenta(Ventas venta);
    
    /**
     * Obtiene todos los clientes de la base de datos  usando un map
     * Es util para la actualizacion de una venta
     * 
     * @return El objeto {@code Ventas} actualizado con su nuevo cliente.
     */
    public Map<Long, String> obtenerClientes();

    /**
     * Elimina un registro de venta basado en su identificador único.
     *
     * @param id El identificador único (Long) del registro de venta que se desea eliminar.
     * @return {@code true} si el registro de venta fue eliminado exitosamente, {@code false} en caso contrario.
     */
    boolean eliminarVentaPorId(Long id);
    public void deleteLogico(Long id);
    
    public Ventas obtenerVentaConDetalles(String numeroFactura);
    public Double TotalDEVentas();
    List<Ventas> findByCliente(Long clienteId);
    List<Ventas> findByEstado(EstadosVentas estado);
    List<Ventas> findByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
         

}


