/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import entities.DetalleVentas;
import java.util.List;


public interface DetalleVentasDAO {
  
    DetalleVentas create(DetalleVentas detalleVenta);

    public List<DetalleVentas> obtenerDetalleVentas(String numeroFactura);

    DetalleVentas update(DetalleVentas Detalle);

    boolean deleteById(Long id);
      public void DeleteLogico(Long id) ;
    
    DetalleVentas finByIdCriteria(Long Id);
    
  
    
    
    
}
