/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;



import entities.Productos;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;


public interface ProductosDAO {

    Productos create(Productos producto);
    Productos update(Productos producto);
    boolean deleteById(Long id);
    public List<Productos> buscarPorNombre(String criterioDeBusqueda);
    Productos findByCode(String codigo);
    Productos findByName(String nombre);
    Productos finById(Long Id);
    public List<Productos> findAllProducts();
    public void DeleteLogico(Long id);
    public void reducirStock(Long id, int cantidadVendida);
    public List<Productos> findByStockBajo();
    public List<Productos> findActivos();
}
