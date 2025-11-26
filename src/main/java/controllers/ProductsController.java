
package controllers;

import Services.ProductoService;
import entities.Estados;
import entities.EstadosProductos;
import entities.Productos;
import java.util.List;

public class ProductsController {

    private final  ProductoService productoService;

    public ProductsController(ProductoService productoService) {
        this.productoService = productoService;
    }

    public List<Productos> obtenerProductos() {
        return productoService.obtenerTodosLosProductos();
    }

    public List<Productos> buscarProductosPorNombre(String criterioBusqueda) {
        return productoService.obtenerProductosPorNombre(criterioBusqueda);
    }
    
     public Productos buscarProductoId(Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    public String obtenerImagenProducto(Long id) {
        return productoService.getMapaImagenes().get(id);
    }

    public EstadosProductos obtenerEstadoProducto(Long id) {
        return productoService.getMapaEstados().get(id);
    }
    
     public void agregarProductoAVenta(Long id, int cantidad, int stockDisponible,int stockMinimo) {
        productoService.validarStock(cantidad, stockDisponible,stockMinimo);
    }

}
