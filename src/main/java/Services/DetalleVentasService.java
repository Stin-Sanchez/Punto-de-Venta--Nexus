/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import dao.DetalleVentasDAO;
import entities.DetalleVentas;
import java.util.List;

/**
 *
 * @author ING. Stin Sánchez
 */
public class DetalleVentasService {
     private  final DetalleVentasDAO detalleDAO;
   


    public DetalleVentasService(DetalleVentasDAO detalleDAO) {
        this.detalleDAO = detalleDAO;
    }
    
    
// 
  
    public DetalleVentas crearVenta(DetalleVentas detalle){
        
        return detalleDAO.create(detalle);
    }
     
     
   public List<DetalleVentas> obtenerDetallePorNumeroFactura(String numeroFactura) {
        return detalleDAO.obtenerDetalleVentas(numeroFactura);
    }
   
  

    public DetalleVentas buscarDetallePorId(Long id) {
        return detalleDAO.finByIdCriteria(id);
    }

    public DetalleVentas actualizarDetalle(DetalleVentas detalle) {
        return detalleDAO.update(detalle);
    }

    public boolean eliminarDetallePorId(Long id) {
        return detalleDAO.deleteById(id);
    }
   
}
