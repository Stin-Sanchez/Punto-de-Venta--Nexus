
package Services;
import Excepciones.BusinessException;
import dao.ProductosDAO;
import dao.ProductosDAOImpl;
import entities.EstadosProductos;
import entities.Productos;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
@Getter


public class ProductoService {
    
    private final ProductosDAO productosDAO;

    public ProductoService() {
        this.productosDAO=new ProductosDAOImpl();
    }

    // HashMap para almacenar la ruta de la imagen asociada a cada ID de producto
    private final HashMap<Long, String> mapaImagenes = new HashMap<>();
    private final HashMap<Long, EstadosProductos> mapaEstados = new HashMap<>();

    void crearProducto(Productos producto) throws BusinessException {

    }

    void actualizarProducto(Productos producto) throws BusinessException{

    }

    void eliminarProducto(Long id) throws BusinessException{

    }
     
     public List<Productos> obtenerTodosLosProductos() {
        List<Productos> listaProductos = productosDAO.findAllProducts();

        // Llenamos los mapas con la información de imágenes y estados
        for (Productos producto : listaProductos) {
            mapaImagenes.put(producto.getId(), producto.getImagen());
            mapaEstados.put(producto.getId(), producto.getEstado());
        }

        return listaProductos;
    }

    List<Productos> listarActivos(){
        return productosDAO.findActivos();
    }
    List<Productos> listarStockBajo(){
        return productosDAO.findByStockBajo();
    }
     
      public Productos obtenerProductoPorId(Long id) {
        return productosDAO.finById(id);
    }
     
     public List<Productos> obtenerProductosPorNombre(String criterioBusqueda) {
        return productosDAO.buscarPorNombre(criterioBusqueda);
    }
     
      public void validarStock(int cantidad, int stockDisponible,int stockMinimo) {
          if (cantidad <= 0) {
              throw new IllegalArgumentException("La cantidad debe ser mayor que 0. Cantidad ingresada: " + cantidad);
          }

          if (cantidad > stockDisponible) {
              throw new IllegalArgumentException("No hay suficiente stock para el producto seleccionado. Stock disponible: " + stockDisponible + ", cantidad solicitada: " + cantidad);
          }

          if ((stockDisponible - cantidad) < stockMinimo) {
              throw new IllegalArgumentException("No se puede realizar la venta. Esta operación dejaría el stock por debajo del mínimo requerido (" + stockMinimo + "). Stock actual: " + stockDisponible + ", cantidad solicitada: " + cantidad);
          }

    }

    void actualizarStock(Long productoId, Integer cantidad) throws BusinessException{
        Productos producto = productosDAO.finById(productoId);
        if (producto == null) {
            throw new BusinessException("Producto no encontrado");
        }

        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }

        producto.setStock(nuevoStock);
        productosDAO.update(producto);
    }
    //====validaciones de reglas de negocio========

    private void validarProducto(Productos producto, boolean esNuevo) throws BusinessException {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new BusinessException("El nombre del producto es obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio debe ser mayor a cero");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }

        if (producto.getStockMinimo() == null || producto.getStockMinimo() < 0) {
            throw new BusinessException("El stock mínimo no puede ser negativo");
        }

        // Validar unicidad del nombre
        if (esNuevo) {
            Productos productoExistente = productosDAO.findByCode(producto.getCode());
            if (productoExistente != null) {
                throw new BusinessException("Ya existe un producto con ese código ");
            }else if(productoExistente!=null){
                productoExistente=productosDAO.findByName(producto.getNombreProducto());
                throw new BusinessException("Ya existe un producto con ese nombre ");
            }
    }
}

    
}
    
    
    

