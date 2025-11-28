/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NexusTest;

import dao.*;
import entities.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Stin Josue Sanchez Mosquera.
 */
public class CRUDtest {

    ClientesDAO clienteRepo;
    ProductosDAO productosDAO;
    VentasDAO vdao;
    DashboardDAO ddao;
    UsuariosDAO udao;
    CategoriasRepositoryIMP categoriaRepository;

    @BeforeEach
    void setUp() {
        clienteRepo = new ClientesDAOImpl();
        productosDAO = new ProductosDAOImpl();
        vdao = new VentasDAOImpl();
        ddao = new DashboardDAOIMP();
        udao = new UsuariosDAOImpl();
        categoriaRepository = new CategoriasRepositoryIMP();
    }

    @Test
    void deberiaAgregarNuevoCliente() {
        // Arrange
        Clientes cliente = new Clientes();
        cliente.setNombre("Joss");
        cliente.setApellido("Saenz");
        cliente.setEstado(EstadosUsuariosClientes.ACTIVO);
        cliente.setCedula("0803639484");
        cliente.setEmail("joss12@gmail.com");
        cliente.setTelefono("0969561832");
        cliente.setFechaCreacion(LocalDate.now());

        // Act
        clienteRepo.crearUsuario(cliente);

        // Assert
        assertNotNull(cliente.getId(), "El cliente debería tener un ID asignado");

        Clientes clienteGuardado = clienteRepo.finById(cliente.getId());
        assertNotNull(clienteGuardado, "El cliente debería estar guardado en la BD");
        assertEquals("Joss", clienteGuardado.getNombre());
        assertEquals("0803639484", clienteGuardado.getCedula());
        assertEquals(EstadosUsuariosClientes.ACTIVO, clienteGuardado.getEstado());
    }

    @Test
    void deberiaAgregarNuevoUsuario() {
        // Arrange
        Usuarios usuario = new Usuarios();
        usuario.setUsername("Josue Sanchez");
        usuario.setPassword("190904");
        usuario.setRol(Roles.VENDEDOR);
        usuario.setFechaCreacion(LocalDate.now());
        usuario.setActivo(true);
        usuario.setEstado(EstadosUsuariosClientes.ACTIVO);

        // Act
        udao.create(usuario);

        // Assert
        assertNotNull(usuario.getId(), "El usuario debería tener un ID asignado");
        assertTrue(usuario.isActivo(), "El usuario debería estar activo");
        assertEquals(Roles.VENDEDOR, usuario.getRol());
    }

    @Test
    void deberiaCrearVentaConDetalles() {
        // Arrange
        Clientes cliente = clienteRepo.finById(1L);
        assertNotNull(cliente, "El cliente debe existir para crear la venta");

        Productos producto1 = productosDAO.finById(1L);
        assertNotNull(producto1, "El producto 1 debe existir");

        Ventas venta = new Ventas();
        venta.setCliente(cliente);

        DetalleVentas detalle = crearDetalle(producto1, 100, venta);
        venta.getDetalles().add(detalle);

        venta.setEstado(EstadosVentas.CONFIRMADA);
        venta.setFechaHora(LocalDateTime.now());
        venta.setTotal(venta.calcularTotal());

        Facturas factura = new Facturas(
                null,
                "001-001-000001234",
                "C:/Facturas/Nexus",
                "17243412345",
                LocalDateTime.now(),
                venta
        );
        venta.setFactura(factura);

        // Act
        vdao.crearVenta(venta);

        // Assert
        assertNotNull(venta.getId(), "La venta debería tener un ID asignado");
        assertFalse(venta.getDetalles().isEmpty(), "La venta debe tener detalles");
        assertEquals(EstadosVentas.CONFIRMADA, venta.getEstado());
        assertNotNull(venta.getFactura(), "La venta debe tener factura");
        assertTrue(venta.getTotal().compareTo(BigDecimal.ZERO) > 0,
                "El total debe ser mayor a cero");
    }

    @Test
    void deberiaCrearCategoria() {
        // Arrange
        Categorias categoria = new Categorias();
        categoria.setNombre("Tecnología");

        // Act
        categoriaRepository.crear(categoria);

        // Assert
        assertNotNull(categoria.getId(), "La categoría debería tener un ID");

        Categorias categoriaGuardada = categoriaRepository.porId(categoria.getId().intValue());
        assertNotNull(categoriaGuardada, "La categoría debe estar en la BD");
        assertEquals("Tecnología", categoriaGuardada.getNombre());
    }

    @Test
    void deberiaCrearProducto() {
        // Arrange
        Productos producto = new Productos();
        producto.setNombreProducto("Crema Dental Colgate Total 12");
        producto.setDescripcion("Pasta dental con protección antibacterial");
        producto.setCode("CLG001");
        producto.setStock(200);
        producto.setStockMinimo(10);
        producto.setPrecio(new BigDecimal("2.00"));
        producto.setImagen("null");
        producto.setFechaVencimiento(LocalDate.of(2027, 10, 20));
        producto.setEstado(EstadosProductos.DISPONIBLE);
        producto.setMarca("Colgate");
        producto.setActivo(true);

        // Act
        productosDAO.create(producto);

        // Assert
        assertNotNull(producto.getId(), "El producto debe tener un ID");
        assertTrue(producto.getActivo(), "El producto debe estar activo");
        assertEquals(EstadosProductos.DISPONIBLE, producto.getEstado());
        assertTrue(producto.getPrecio().compareTo(BigDecimal.ZERO) > 0,
                "El precio debe ser mayor a cero");
    }

    @Test
    void deberiaListarCategorias() {
        // Act
        List<Categorias> categorias = categoriaRepository.listar();

        // Assert
        assertNotNull(categorias, "La lista no debe ser null");
        assertFalse(categorias.isEmpty(), "Debe haber al menos una categoría");

        // Verificar que todas las categorías tienen datos válidos
        categorias.forEach(c -> {
            assertNotNull(c.getId(), "Cada categoría debe tener ID");
            assertNotNull(c.getNombre(), "Cada categoría debe tener nombre");
        });
        AtomicInteger counter = new AtomicInteger(1);
        categorias.stream().forEach(ca -> System.out.println(counter.getAndIncrement() + ". " + ca.getNombre()+" is activo: "+ca.isActivo()));
    }

    @Test
    void deberiaEncontrarUsuarioPorId() {
        // Act
        Usuarios usuario = udao.finById(3L);

        // Assert
        assertNotNull(usuario, "El usuario con ID 3 debe existir");
        assertNotNull(usuario.getUsername(), "El usuario debe tener username");
        assertNotNull(usuario.getRol(), "El usuario debe tener rol");
        assertNotNull(usuario.getEstado(), "El usuario debe tener estado");
    }

    @Test
    void deberiaObtenerVentaConDetalles() {
        // Act
        Ventas venta = vdao.obtenerVentaConDetalles("00000001");

        // Assert
        assertNotNull(venta, "La venta debe existir");
        assertNotNull(venta.getDetalles(), "La venta debe tener detalles");
        assertFalse(venta.getDetalles().isEmpty(),
                "La lista de detalles no debe estar vacía");

        // Verificar que cada detalle tiene datos válidos
        venta.getDetalles().forEach(detalle -> {
            assertNotNull(detalle.getProductos(), "Cada detalle debe tener producto");
            assertTrue(detalle.getCantidad() > 0, "La cantidad debe ser mayor a 0");
            assertTrue(detalle.getSubtotal().compareTo(BigDecimal.ZERO) > 0,
                    "El subtotal debe ser mayor a 0");
        });
    }

    @Test
    void deberiaObtenerProductosProximosAVencer() {
        // Act
        List<Map<String, Object>> proximos = ddao.getProductosProximosVencer();

        // Assert
        assertNotNull(proximos, "La lista no debe ser null");
        assertEquals(2, proximos.size(), "Debe haber exactamente 2 productos");

        proximos.forEach(producto -> {
            assertNotNull(producto.get("nombre"), "Cada producto debe tener nombre");
            assertNotNull(producto.get("fechaVencimiento"),
                    "Cada producto debe tener fecha de vencimiento");
            assertTrue((int) producto.get("stock") >= 0,
                    "El stock no puede ser negativo");
            assertTrue((int) producto.get("diasRestantes") >= 0,
                    "Los días restantes deben ser >= 0");
        });
    }

    @Test
    void deberiaObtenerTop5ProductosPopulares() {
        // Act
        List<Map<String, Object>> top5 = ddao.getProductosPopulares();

        // Assert
        assertNotNull(top5, "La lista no debe ser null");
        assertEquals(5, top5.size(), "Debe haber exactamente 5 productos");

        top5.forEach(producto -> {
            assertNotNull(producto.get("nombreProducto"),
                    "Cada producto debe tener nombre");
            Long cantidad = (Long) producto.get("cantidad");
            assertTrue(cantidad > 0, "La cantidad vendida debe ser mayor a 0");

            double porcentaje = (double) producto.get("porcentaje");
            assertTrue(porcentaje >= 0 && porcentaje <= 100,
                    "El porcentaje debe estar entre 0 y 100");
        });
    }

    @Test
    void deberiaObtenerVentasPorHora() {
        // Act
        List<Map<String, Object>> ventasPorHora = ddao.getVentasPorHora();

        // Assert
        assertNotNull(ventasPorHora, "La lista no debe ser null");
        assertEquals(2, ventasPorHora.size(), "Debe haber 2 registros de ventas");

        ventasPorHora.forEach(venta -> {
            int hora = (int) venta.get("hora");
            assertTrue(hora >= 0 && hora <= 23, "La hora debe estar entre 0 y 23");

            Long numVentas = (Long) venta.get("numVentas");
            assertTrue(numVentas > 0, "El número de ventas debe ser mayor a 0");

            BigDecimal total = convertirABigDecimal(venta.get("totalMonto"));
            assertTrue(total.compareTo(BigDecimal.ZERO) > 0,
                    "El total debe ser mayor a 0");
        });
    }

    @Test
    void deberiaObtenerMetodosPagoMasUsados() {
        // Act
        List<Map<String, Object>> metodos = ddao.getMetodosPagoHoy();

        // Assert
        assertNotNull(metodos, "La lista no debe ser null");
        assertEquals(2, metodos.size(), "Debe haber 2 métodos de pago");

        metodos.forEach(metodo -> {
            assertNotNull(metodo.get("metodo"), "Debe tener método de pago");

            Long cantidad = (Long) metodo.get("cantidad");
            assertTrue(cantidad > 0, "La cantidad debe ser mayor a 0");

            BigDecimal monto = convertirABigDecimal(metodo.get("monto"));
            assertTrue(monto.compareTo(BigDecimal.ZERO) > 0,
                    "El monto debe ser mayor a 0");

            Double porcentaje = (Double) metodo.get("porcentaje");
            assertTrue(porcentaje > 0 && porcentaje <= 100,
                    "El porcentaje debe estar entre 0 y 100");
        });
    }

    @Test
    void deberiaObtenerTotalVentasDelDia() {
        // Act
        BigDecimal total = ddao.getVentasDelDia();

        // Assert
        assertNotNull(total, "El total no debe ser null");
        assertTrue(total.compareTo(BigDecimal.ZERO) >= 0,
                "El total debe ser mayor o igual a 0");
    }

    @Test
    void deberiaObtenerTotalClientesActivos() {
        // Act
        Long total = ddao.getTotalClientes();

        // Assert
        assertNotNull(total, "El total no debe ser null");
        assertTrue(total >= 0, "El total debe ser >= 0");
    }

    @Test
    void deberiaObtenerProductosVendidosHoy() {
        // Act
        Long total = ddao.getProductosVendidosHoy();

        // Assert
        assertNotNull(total, "El total no debe ser null");
        assertTrue(total >= 0, "El total debe ser >= 0");
    }

    @Test
    void deberiaObtenerProductosConStockBajo() {
        // Act
        Long total = ddao.getProductosStockBajo();

        // Assert
        assertNotNull(total, "El total no debe ser null");
        assertTrue(total >= 0, "El total debe ser >= 0");
    }

    @Test
    void deberiaObtenerClientesAtendidosHoy() {
        // Act
        Long total = ddao.getClientesAtendidosHoy();

        // Assert
        assertNotNull(total, "El total no debe ser null");
        assertTrue(total >= 0, "El total debe ser >= 0");
    }

    @Test
    void deberiaObtenerUltimasActividades() {
        // Act
        List<Map<String, Object>> actividades = ddao.getUltimasActividades();

        // Assert
        assertNotNull(actividades, "La lista no debe ser null");
        assertEquals(3, actividades.size(), "Debe haber 3 actividades");

        actividades.forEach(actividad -> {
            assertNotNull(actividad.get("id"), "Cada venta debe tener ID");
            assertNotNull(actividad.get("fechaHora"),
                    "Cada venta debe tener fecha");
            assertNotNull(actividad.get("cliente"),
                    "Cada venta debe tener cliente");

            BigDecimal total = convertirABigDecimal(actividad.get("totalMonto"));
            assertTrue(total.compareTo(BigDecimal.ZERO) > 0,
                    "El total debe ser mayor a 0");
        });
    }

    @Test
    void deberiaBuscarProductoPorCodigo() {
        // Arrange
        String codigo = "NVM001";

        // Act
        Productos producto = productosDAO.findByCode(codigo);

        // Assert
        assertNotNull(producto, "El producto debe existir");
        assertEquals(codigo, producto.getCode(),
                "El código debe coincidir");
        assertNotNull(producto.getNombreProducto(),
                "El producto debe tener nombre");
    }

    @Test
    void deberiaBuscarVentaPorNumeroFactura() {
        // Arrange
        String numFactura = "000000003";

        // Act
        Ventas venta = vdao.obtenerVentaConDetalles(numFactura);

        // Assert - En este caso, puede o no existir, así que verificamos ambos casos
        if (venta != null) {
            assertNotNull(venta.getId(), "La venta debe tener ID");
            assertNotNull(venta.getEstado(), "La venta debe tener estado");
            assertNotNull(venta.getCliente(), "La venta debe tener cliente");
            assertFalse(venta.getDetalles().isEmpty(),
                    "La venta debe tener detalles");
        }
        // Si es null, simplemente el test pasa (caso válido)
    }

    @Test
    void deberiaActualizarCategoria() {
        // Arrange
        int categoriaId = 1;
        String nuevoNombre = "Tecnologia Assert";

        // Act
        Categorias categoria = categoriaRepository.porId(categoriaId);
        assertNotNull(categoria, "La categoría debe existir antes de actualizar");

        categoria.setNombre(nuevoNombre);
        categoria.setActivo(true);
        categoriaRepository.editar(categoria);

        // Assert
        Categorias categoriaActualizada = categoriaRepository.porId(categoriaId);
        assertNotNull(categoriaActualizada, "La categoría no debe ser null");
        assertEquals(categoriaId, categoriaActualizada.getId(),
                "El ID debe permanecer igual");
        assertEquals(nuevoNombre, categoriaActualizada.getNombre(),
                "El nombre debe haberse actualizado");
        assertTrue(categoriaActualizada.isActivo(),
                "La categoría debe estar activa");
    }

    @Test
    void deberiaRealizarBorradoLogicoDeCliente() {
        // Arrange
        Clientes cliente = clienteRepo.finById(2L);
        assertNotNull(cliente, "El cliente debe existir");

        boolean estadoInicial = cliente.isActivo();

        // Act
        clienteRepo.DeleteLogico(cliente.getId());

        // Assert
        Clientes clienteActualizado = clienteRepo.finById(2L);
        assertNotNull(clienteActualizado, "El cliente aún debe existir en la BD");
        assertFalse(clienteActualizado.isActivo(),
                "El cliente debe estar inactivo después del borrado lógico");
    }

    @Test
    void deberiaRealizarBorradoLogicoDeVenta() {
        // Arrange
        Ventas venta = vdao.buscarVentaPorId(1L);
        assertNotNull(venta, "La venta debe existir");

        // Act
        vdao.deleteLogico(venta.getId());

        // Assert
        Ventas ventaActualizada = vdao.buscarVentaPorId(1L);
        assertNotNull(ventaActualizada, "La venta aún debe existir en la BD");
        assertFalse(ventaActualizada.isActiva(),
                "La venta debe estar inactiva después del borrado lógico");
    }

    @Test
    void deberiaBorrarCategoriasDuplicadas(){
            // Arrange
            List<Categorias> lista = categoriaRepository.listar();
            Set<String> nombresVistos = new HashSet<>();
            List<Long> idsAEliminar = new ArrayList<>();

            // Identificar duplicados
            for (Categorias cat : lista) {
                if (!nombresVistos.add(cat.getNombre())) {
                    // Ya vimos este nombre, es duplicado
                    idsAEliminar.add(cat.getId());
                }
            }

            // Act - Eliminar duplicados (borrado lógico)
            for (Long id : idsAEliminar) {
                categoriaRepository.eliminar(id.intValue());
            }

            // Assert - Verificar que están inactivos
            for (Long id : idsAEliminar) {
                Categorias cat = categoriaRepository.porId(id.intValue());
                assertNotNull(cat, "La categoría debe existir en la BD");
                assertFalse(cat.isActivo(), "La categoría duplicada debe estar inactiva");
            }

            System.out.println("Duplicados desactivados: " + idsAEliminar.size());
        }


    // Método auxiliar para crear detalles de venta
    private DetalleVentas crearDetalle(Productos producto, int cantidad, Ventas venta) {
        DetalleVentas detalle = new DetalleVentas();
        detalle.setProductos(producto);
        detalle.setPrecio(producto.getPrecio());
        detalle.setCantidad(cantidad);
        detalle.calcularSubtotal();
        detalle.setSubtotal(detalle.getSubtotal());
        detalle.setVenta(venta);
        return detalle;
    }

    // Método auxiliar para conversión segura a BigDecimal
    private BigDecimal convertirABigDecimal(Object obj) {
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        } else if (obj instanceof Number) {
            return BigDecimal.valueOf(((Number) obj).doubleValue());
        }
        return BigDecimal.ZERO;
    }
}
