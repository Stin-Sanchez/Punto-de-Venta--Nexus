
package controllers;

import Services.DetalleVentasService;
import entities.DetalleVentas;

import java.util.List;

/**
 *
 * @author ING. Stin Sánchez
 */
public class DetalleVentasController {

    private DetalleVentasService detallesVentasService;

    public DetalleVentasController(DetalleVentasService detallesVentasService) {
        this.detallesVentasService = detallesVentasService;
    }

    public DetalleVentas InsertarNuevaVenta(DetalleVentas detalle) {

        return detallesVentasService.crearVenta(detalle);

    }

    public List<DetalleVentas> obtenerDetallesDeFactura(String numeroFactura) {
        return detallesVentasService.obtenerDetallePorNumeroFactura(numeroFactura);
    }

    public void buscarDetalleVentaPorId(Long id) {
        DetalleVentas detalle = detallesVentasService.buscarDetallePorId(id);
        System.out.println("Detalle encontrado: " + detalle);
    }

    public void actualizarDetalleVenta(DetalleVentas detalle) {
        DetalleVentas actualizado = detallesVentasService.actualizarDetalle(detalle);
        System.out.println("Detalle actualizado: " + actualizado);
    }

    public void eliminarDetalleVenta(Long id) {
        if (detallesVentasService.eliminarDetallePorId(id)) {
            System.out.println("Detalle eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar detalle.");
        }
    }

}
