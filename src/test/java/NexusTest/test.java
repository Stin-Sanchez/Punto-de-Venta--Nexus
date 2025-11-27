/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NexusTest;


import Services.ProductoService;
import controllers.ClienteController;
import controllers.ProductsController;
import controllers.VentasController;
import dao.*;
import entities.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author sjosu
 */
public class test {

    ClientesDAO clienteRepo;
    ProductosDAO productosDAO;
    VentasDAO vdao;
    DashboardDAO ddao;
    UsuariosDAO udao;
    @BeforeEach
    void setUp() {
        clienteRepo = new ClientesDAOImpl();
        productosDAO= new ProductosDAOImpl();
        vdao= new VentasDAOImpl();
        ddao= new DashboardDAOIMP();
        udao= new UsuariosDAOImpl();
    }

    void createDirecciones(){

    }

    @Test
    void addClientes(){
        Clientes c1= new Clientes();
        c1.setNombre("Joss");
        c1.setApellido("Saenz");
        c1.setEstado(EstadosUsuariosClientes.ACTIVO);
        c1.setCedula("0803639484");
        c1.setEmail("joss12@gmail.com");
        c1.setTelefono("0969561832");
        c1.setFechaCreacion(LocalDate.now());

        clienteRepo.crearUsuario(c1);

        }

        @Test
        void addUser(){
            Usuarios u1= new Usuarios();
            u1.setUsername("Josue Sanchez");
            u1.setPassword("190904");
            u1.setRol(Roles.VENDEDOR);
            u1.setFechaCreacion(LocalDate.now());
            u1.setActivo(true);
            u1.setEstado(EstadosUsuariosClientes.ACTIVO);
            udao.create(u1);
        }

        @Test
        void findById(){
            Usuarios u1= udao.finById(3L);
            System.out.println(u1.getUsername()+u1.getRol()+u1.getEstado());

        }

        @Test
        void addProduct(){

            // Producto 2
            Productos p2 = new Productos();
            p2.setNombreProducto("Crema Dental Colgate Total 12");
            p2.setDescripcion("Pasta dental con protección antibacterial");
            p2.setCode("CLG001");
            p2.setStock(200);
            p2.setStockMinimo(10);
            p2.setPrecio(new BigDecimal("2.00"));
            p2.setImagen("null");
            p2.setFechaVencimiento(LocalDate.of(2027,10,20));
            p2.setEstado(EstadosProductos.DISPONIBLE);
            p2.setMarca("Colgate");
            p2.setActivo(true);
            productosDAO.create(p2);

// Producto 3
            Productos p3 = new Productos();
            p3.setNombreProducto("Cuaderno Universitario Norma");
            p3.setDescripcion("Cuaderno de 100 hojas, cuadriculado");
            p3.setCode("NRM001");
            p3.setStock(120);
            p3.setStockMinimo(15);
            p3.setPrecio(new BigDecimal("3.00"));
            p3.setImagen("null");
            p3.setFechaVencimiento(LocalDate.of(2026,10,20));
            p3.setEstado(EstadosProductos.DISPONIBLE);
            p3.setMarca("Norma");
            p3.setActivo(true);
            productosDAO.create(p3);

// Producto 4
            Productos p4 = new Productos();
            p4.setNombreProducto("Bolígrafo BIC Azul");
            p4.setDescripcion("Bolígrafo color azul, escritura suave");
            p4.setCode("BIC001");
            p4.setStock(500);
            p4.setStockMinimo(50);
            p4.setPrecio(new BigDecimal("0.50"));
            p4.setImagen("null");
            p4.setFechaVencimiento(LocalDate.of(2025,10,20));
            p4.setEstado(EstadosProductos.DISPONIBLE);
            p4.setMarca("BIC");
            p4.setActivo(true);
            productosDAO.create(p4);

// Producto 5
            Productos p5 = new Productos();
            p5.setNombreProducto("Coca Cola 500ml");
            p5.setDescripcion("Bebida gaseosa clásica");
            p5.setCode("CCL001");
            p5.setStock(300);
            p5.setStockMinimo(30);
            p5.setPrecio(new BigDecimal("1.20"));
            p5.setImagen("null");
            p5.setFechaVencimiento(LocalDate.of(2026,3,20));
            p5.setEstado(EstadosProductos.DISPONIBLE);
            p5.setMarca("Coca Cola");
            p5.setActivo(true);
            productosDAO.create(p5);

// Producto 6
            Productos p6 = new Productos();
            p6.setNombreProducto("Agua Ciel 600ml");
            p6.setDescripcion("Agua purificada sin gas");
            p6.setCode("CIE001");
            p6.setStock(400);
            p6.setStockMinimo(20);
            p6.setPrecio(new BigDecimal("0.80"));
            p6.setImagen("null");
            p6.setFechaVencimiento(LocalDate.of(2027,6,20));
            p6.setEstado(EstadosProductos.DISPONIBLE);
            p6.setMarca("Ciel");
            p6.setActivo(true);
            productosDAO.create(p6);

// Producto 7
            Productos p7 = new Productos();
            p7.setNombreProducto("Papas Fritas Lays Clásicas");
            p7.setDescripcion("Papas fritas con sal, 150g");
            p7.setCode("LAY001");
            p7.setStock(250);
            p7.setStockMinimo(25);
            p7.setPrecio(new BigDecimal("1.50"));
            p7.setImagen("null");
            p7.setFechaVencimiento(LocalDate.of(2026,3,20));
            p7.setEstado(EstadosProductos.DISPONIBLE);
            p7.setMarca("Lays");
            p7.setActivo(true);
            productosDAO.create(p7);

// Producto 8
            Productos p8 = new Productos();
            p8.setNombreProducto("Chocolate Snickers");
            p8.setDescripcion("Barra de chocolate con maní y caramelo");
            p8.setCode("SNK001");
            p8.setStock(180);
            p8.setStockMinimo(20);
            p8.setPrecio(new BigDecimal("0.90"));
            p8.setImagen("null");
            p8.setFechaVencimiento(LocalDate.of(2026,6,20));
            p8.setEstado(EstadosProductos.DISPONIBLE);
            p8.setMarca("Snickers");
            p8.setActivo(true);
            productosDAO.create(p8);

// Producto 9
            Productos p9 = new Productos();
            p9.setNombreProducto("Arroz Súper Extra 1kg");
            p9.setDescripcion("Arroz de grano largo, calidad extra");
            p9.setCode("ARZ001");
            p9.setStock(150);
            p9.setStockMinimo(15);
            p9.setPrecio(new BigDecimal("1.80"));
            p9.setImagen("null");
            p2.setFechaVencimiento(LocalDate.of(2027,10,20));
            p9.setEstado(EstadosProductos.DISPONIBLE);
            p9.setMarca("La Favorita");
            p9.setActivo(true);
            productosDAO.create(p9);

// Producto 10
            Productos p10 = new Productos();
            p10.setNombreProducto("Aceite Girasol 1L");
            p10.setDescripcion("Aceite vegetal refinado");
            p10.setCode("ACE001");
            p10.setStock(100);
            p10.setStockMinimo(10);
            p10.setPrecio(new BigDecimal("3.50"));
            p10.setImagen("null");
            p2.setFechaVencimiento(LocalDate.of(2027,10,20));
            p10.setEstado(EstadosProductos.DISPONIBLE);
            p10.setMarca("Cocinero");
            p10.setActivo(true);
            productosDAO.create(p10);

            // Producto 11
            Productos p11 = new Productos();
            p11.setNombreProducto("Leche Entera Toni 1L");
            p11.setDescripcion("Leche entera pasteurizada en envase Tetra Pak");
            p11.setCode("LEC001");
            p11.setStock(220);
            p11.setStockMinimo(20);
            p11.setPrecio(new BigDecimal("1.10"));
            p11.setImagen("null");
            p11.setFechaVencimiento(LocalDate.of(2025, 11, 5));
            p11.setEstado(EstadosProductos.DISPONIBLE);
            p11.setMarca("Toni");
            p11.setActivo(true);
            productosDAO.create(p11);

// Producto 12
            Productos p12 = new Productos();
            p12.setNombreProducto("Yogurt Griego Kiosko 150g");
            p12.setDescripcion("Yogurt natural estilo griego");
            p12.setCode("YGT001");
            p12.setStock(300);
            p12.setStockMinimo(25);
            p12.setPrecio(new BigDecimal("0.95"));
            p12.setImagen("null");
            p12.setFechaVencimiento(LocalDate.of(2025, 10, 25));
            p12.setEstado(EstadosProductos.DISPONIBLE);
            p12.setMarca("Kiosko");
            p12.setActivo(true);
            productosDAO.create(p12);

// Producto 13
            Productos p13 = new Productos();
            p13.setNombreProducto("Mantequilla La Vaquita 200g");
            p13.setDescripcion("Mantequilla con sal en barra");
            p13.setCode("MTQ001");
            p13.setStock(180);
            p13.setStockMinimo(15);
            p13.setPrecio(new BigDecimal("1.40"));
            p13.setImagen("null");
            p13.setFechaVencimiento(LocalDate.of(2025, 12, 10));
            p13.setEstado(EstadosProductos.DISPONIBLE);
            p13.setMarca("La Vaquita");
            p13.setActivo(true);
            productosDAO.create(p13);

// Producto 14
            Productos p14 = new Productos();
            p14.setNombreProducto("Queso Fresco El Ordeño 400g");
            p14.setDescripcion("Queso fresco pasteurizado en bloque");
            p14.setCode("QSO001");
            p14.setStock(90);
            p14.setStockMinimo(10);
            p14.setPrecio(new BigDecimal("2.80"));
            p14.setImagen("null");
            p14.setFechaVencimiento(LocalDate.of(2025, 9, 30));
            p14.setEstado(EstadosProductos.DISPONIBLE);
            p14.setMarca("El Ordeño");
            p14.setActivo(true);
            productosDAO.create(p14);

// Producto 15
            Productos p15 = new Productos();
            p15.setNombreProducto("Pan Tajado Bimbo 600g");
            p15.setDescripcion("Pan de molde blanco rebanado");
            p15.setCode("PNB001");
            p15.setStock(160);
            p15.setStockMinimo(15);
            p15.setPrecio(new BigDecimal("2.20"));
            p15.setImagen("null");
            p15.setFechaVencimiento(LocalDate.of(2025, 9, 25));
            p15.setEstado(EstadosProductos.DISPONIBLE);
            p15.setMarca("Bimbo");
            p15.setActivo(true);
            productosDAO.create(p15);

// Producto 16
            Productos p16 = new Productos();
            p16.setNombreProducto("Atún Real Lata 170g");
            p16.setDescripcion("Atún en aceite vegetal en lata");
            p16.setCode("ATN001");
            p16.setStock(350);
            p16.setStockMinimo(30);
            p16.setPrecio(new BigDecimal("1.60"));
            p16.setImagen("null");
            p16.setFechaVencimiento(LocalDate.of(2026, 3, 15));
            p16.setEstado(EstadosProductos.DISPONIBLE);
            p16.setMarca("Real");
            p16.setActivo(true);
            productosDAO.create(p16);

// Producto 17
            Productos p17 = new Productos();
            p17.setNombreProducto("Salsa de Tomate La Huerta 200g");
            p17.setDescripcion("Salsa de tomate lista para servir");
            p17.setCode("SLT001");
            p17.setStock(270);
            p17.setStockMinimo(20);
            p17.setPrecio(new BigDecimal("0.75"));
            p17.setImagen("null");
            p17.setFechaVencimiento(LocalDate.of(2026, 1, 10));
            p17.setEstado(EstadosProductos.DISPONIBLE);
            p17.setMarca("La Huerta");
            p17.setActivo(true);
            productosDAO.create(p17);

// Producto 18
            Productos p18 = new Productos();
            p18.setNombreProducto("Galletas Oreo Clásicas 108g");
            p18.setDescripcion("Galletas rellenas de crema sabor vainilla");
            p18.setCode("ORE001");
            p18.setStock(400);
            p18.setStockMinimo(40);
            p18.setPrecio(new BigDecimal("1.30"));
            p18.setImagen("null");
            p18.setFechaVencimiento(LocalDate.of(2026, 5, 8));
            p18.setEstado(EstadosProductos.DISPONIBLE);
            p18.setMarca("Oreo");
            p18.setActivo(true);
            productosDAO.create(p18);

// Producto 19
            Productos p19 = new Productos();
            p19.setNombreProducto("Cereal Zucaritas 300g");
            p19.setDescripcion("Cereal de maíz azucarado en hojuelas");
            p19.setCode("ZUC001");
            p19.setStock(220);
            p19.setStockMinimo(25);
            p19.setPrecio(new BigDecimal("2.90"));
            p19.setImagen("null");
            p19.setFechaVencimiento(LocalDate.of(2026, 7, 20));
            p19.setEstado(EstadosProductos.DISPONIBLE);
            p19.setMarca("Kellogg's");
            p19.setActivo(true);
            productosDAO.create(p19);

// Producto 20
            Productos p20 = new Productos();
            p20.setNombreProducto("Café Soluble Nescafé 170g");
            p20.setDescripcion("Café instantáneo soluble");
            p20.setCode("CAF001");
            p20.setStock(140);
            p20.setStockMinimo(15);
            p20.setPrecio(new BigDecimal("4.80"));
            p20.setImagen("null");
            p20.setFechaVencimiento(LocalDate.of(2027, 8, 15));
            p20.setEstado(EstadosProductos.DISPONIBLE);
            p20.setMarca("Nescafé");
            p20.setActivo(true);
            productosDAO.create(p20);

        }

      @Test
    void findFacturas(){

        Ventas v=   vdao.obtenerVentaConDetalles("00000001");
           List<DetalleVentas>detalleList=v.getDetalles();


        System.out.println(v);
        System.out.println("Detalles encontrados para la venta "+v.getId()+"---> "+detalleList.size());
        System.out.println();

        for(DetalleVentas list:detalleList){
            System.out.println(list);

        }
      
    }
    @Test
    void testProductosProximosVencer() {
        List<Map<String, Object>> proximos = ddao.getProductosProximosVencer();
        assertNotNull(proximos);
        assertEquals(2, proximos.size());

// Recorrer todos los productos próximos a vencer
        for (int i = 0; i < proximos.size(); i++) {
            Map<String, Object> producto = proximos.get(i);

            String nombre = (String) producto.get("nombre");
            LocalDate fechaVencimiento = (LocalDate) producto.get("fechaVencimiento");
            int stock = (int) producto.get("stock");
            int diasRestantes = (int) producto.get("diasRestantes");

            System.out.println("Producto #" + (i + 1));
            System.out.println("Nombre: " + nombre);
            System.out.println("Fecha de Vencimiento: " + fechaVencimiento);
            System.out.println("Días restantes: " + diasRestantes);
            System.out.println("Stock: " + stock);
            System.out.println("-----------------------------");
        }
    }

    @Test
    void top5(){
        List<Map<String, Object>> top5 = ddao.getProductosPopulares();
        assertNotNull(top5);
        assertEquals(5, top5.size());
// Recorrer todos los productos próximos a vencer
        System.out.println("Top 5 productos mas vendidos");
        for (int i = 0; i < top5.size(); i++) {
            Map<String, Object> producto = top5.get(i);
            String nombre = (String) producto.get("nombreProducto");
            Long cantidad = (Long) producto.get("cantidad");
            double porcentaje= (double) producto.get("porcentaje");

            System.out.println("Producto #" + (i + 1));
            System.out.println("Nombre: " + nombre);
            System.out.println("Cantidad " + cantidad);
            System.out.println("Porcentaje: " + porcentaje);
            System.out.println("-----------------------------");
        }

    }

    @Test
    void ventasHora(){
        List<Map<String, Object>> ventasPorHora = ddao.getVentasPorHora();
        assertNotNull(ventasPorHora);
        assertEquals(2, ventasPorHora.size());
        // Recorrer todos los productos próximos a vencer
        System.out.println("Ventas por hora");
        for (int i = 0; i < ventasPorHora.size(); i++) {
            Map<String, Object> venta = ventasPorHora.get(i);
            int  hora = (int) venta.get("hora");
            Long cantidad = (Long) venta.get("numVentas");
            Object obj = venta.get("totalMonto");
            BigDecimal total;
            if (obj instanceof BigDecimal) {
                total = (BigDecimal) obj;
            } else if (obj instanceof Number) {
                total = BigDecimal.valueOf(((Number) obj).doubleValue());
            } else {
                total = BigDecimal.ZERO; // o lanza excepción
            }

            System.out.println("Venta #" + (i + 1));
            System.out.println("HOra: " + hora);
            System.out.println("Numero de Ventas " + cantidad);
            System.out.println("Total: " + total);
            System.out.println("-----------------------------");
        }
    }
    @Test
    void metodosDePagoMasUsadosHoy(){
        List<Map<String, Object>> metodos = ddao.getMetodosPagoHoy();
        assertNotNull(metodos);
        assertEquals(2, metodos.size());

        System.out.println("Estadisticas de metodos de pago mas usados");
        for (int i = 0; i < metodos.size(); i++) {
            Map<String, Object> venta = metodos.get(i);
            MetodosPago  metodo = (MetodosPago) venta.get("metodo");
            Long cantidad = (Long) venta.get("cantidad");
            Object obj = venta.get("monto");
            BigDecimal total;
            if (obj instanceof BigDecimal) {
                total = (BigDecimal) obj;
            } else if (obj instanceof Number) {
                total = BigDecimal.valueOf(((Number) obj).doubleValue());
            } else {
                total = BigDecimal.ZERO; // o lanza excepción
            }
            Double porcentaje= (Double) venta.get("porcentaje");

            System.out.println("Metodo de pago #" + (i + 1));
            System.out.println("Metodo: " + metodo);
            System.out.println("Cantidad de transacciones " + cantidad);
            System.out.println("Total: " + total);
            System.out.println("Porcentaje: "+porcentaje);
            System.out.println("-----------------------------");
        }
    }

    @Test
     void totalVentasHoy(){
        BigDecimal total= ddao.getVentasDelDia();
        System.out.println("Total hoy: $"+total);
    }

    @Test
    void clientesActivos(){
        Long totalClientesActivos= ddao.getTotalClientes();
        System.out.println("Total de clientes activos: "+totalClientesActivos);
    }

    @Test
    void totalProductosVendidosHoy(){
        Long totalProductos=ddao.getProductosVendidosHoy();
        System.out.println("Total de productos vendidos hoy: "+totalProductos);
    }

    @Test
    void totalProductosStockBajo(){
        Long totalProductosStockBajo=ddao.getProductosStockBajo();
        System.out.println("Total de prdductos con stock bajo: "+totalProductosStockBajo);
    }

    @Test
    void totalClientesAtendidosHoy(){
        Long totalAtendidos=ddao.getClientesAtendidosHoy();
        System.out.println("Total de clientes atendidos hoy: "+totalAtendidos);
    }


    @Test
    void UltimasActividades(){
        List<Map<String, Object>> ultimasActividades = ddao.getUltimasActividades();
        assertNotNull(ultimasActividades);
        assertEquals(3, ultimasActividades.size());

        System.out.println("Ultimas Ventas");
        for (int i = 0; i < ultimasActividades.size(); i++) {
            Map<String, Object> venta = ultimasActividades.get(i);
            Long  idVenta = (Long) venta.get("id");
            Object obj = venta.get("totalMonto");
            BigDecimal total;
            if (obj instanceof BigDecimal) {
                total = (BigDecimal) obj;
            } else if (obj instanceof Number) {
                total = BigDecimal.valueOf(((Number) obj).doubleValue());
            } else {
                total = BigDecimal.ZERO; // o lanza excepción
            }
            Double porcentaje= (Double) venta.get("porcentaje");
            LocalDateTime fecha= (LocalDateTime) venta.get("fechaHora");
            String cliente=(String) venta.get("cliente");

            System.out.println("==================Ultimas Actividades de ventas===========");
            System.out.println("Id: " + idVenta);
            System.out.println("Fecha de venta: " + fecha);
            System.out.println("Total: " + total);
            System.out.println("Cliente: "+cliente);
            System.out.println("-----------------------------");
        }
    }



    @Test
    void deleteLogicoCliente(){
        Clientes cliente=clienteRepo.finById(2L);
        clienteRepo.DeleteLogico(cliente.getId());
        boolean borrado= cliente.isActivo();
        System.out.println("Result, fue borrado logico? :"+borrado+" nuevo estado de cliente, activo="+cliente.isActivo());
    }

    @Test
    void deleteLogicoVentas(){
        Ventas venta=vdao.buscarVentaPorId(1L);
        vdao.deleteLogico(venta.getId());
        boolean borrado = venta.isActiva();
        System.out.println("Result, fue borrado logico? :"+borrado+" nuevo estado de venta:"+venta.getEstado()+"isActivs?= "+venta.isActiva());

    }

    @Test
    void addVenta(){
        ProductosDAO productosDAO= new ProductosDAOImpl();

        VentasDAO ventasController= new VentasDAOImpl();
        Clientes cliente=clienteRepo.finById(1L);
        Productos producto1=productosDAO.finById(1L);
        Productos producto2=productosDAO.finById(2L);
        Productos producto3=productosDAO.finById(3L);
        Productos producto4=productosDAO.finById(4L);
        Productos producto5=productosDAO.finById(5L);

        Ventas venta= new Ventas();
        venta.setCliente(cliente);
        DetalleVentas detalleVentas= new DetalleVentas();
        detalleVentas.setProductos(producto1);
        detalleVentas.setPrecio(producto1.getPrecio());
        detalleVentas.setCantidad(100);
        detalleVentas.calcularSubtotal();
        detalleVentas.setSubtotal(detalleVentas.getSubtotal());
        detalleVentas.setVenta(venta);

        DetalleVentas detalleVentas2= new DetalleVentas();
        detalleVentas2.setProductos(producto2);
        detalleVentas2.setPrecio(producto2.getPrecio());
        detalleVentas2.setCantidad(10);
        detalleVentas2.calcularSubtotal();
        detalleVentas2.setSubtotal(detalleVentas2.getSubtotal());
        detalleVentas2.setVenta(venta);

        DetalleVentas detalleVentas3= new DetalleVentas();
        detalleVentas3.setProductos(producto3);
        detalleVentas3.setPrecio(producto3.getPrecio());
        detalleVentas3.setCantidad(10);
        detalleVentas3.calcularSubtotal();
        detalleVentas3.setSubtotal(detalleVentas3.getSubtotal());
        detalleVentas3.setVenta(venta);

        DetalleVentas detalleVentas4= new DetalleVentas();
        detalleVentas4.setProductos(producto4);
        detalleVentas4.setPrecio(producto4.getPrecio());
        detalleVentas4.setCantidad(10);
        detalleVentas4.calcularSubtotal();
        detalleVentas4.setSubtotal(detalleVentas4.getSubtotal());
        detalleVentas4.setVenta(venta);

        DetalleVentas detalleVentas5= new DetalleVentas();
        detalleVentas5.setProductos(producto5);
        detalleVentas5.setPrecio(producto5.getPrecio());
        detalleVentas5.setCantidad(10);
        detalleVentas5.calcularSubtotal();
        detalleVentas5.setSubtotal(detalleVentas5.getSubtotal());
        detalleVentas5.setVenta(venta);



        List<DetalleVentas>detalles=new ArrayList<>();
        detalles.add(detalleVentas);
        detalles.add(detalleVentas2);
        detalles.add(detalleVentas3);
        detalles.add(detalleVentas4);
        detalles.add(detalleVentas5);

        venta.getDetalles().add(detalleVentas);
        venta.getDetalles().add(detalleVentas2);
        venta.getDetalles().add(detalleVentas3);
        venta.getDetalles().add(detalleVentas4);
        venta.getDetalles().add(detalleVentas5);

        venta.setEstado(EstadosVentas.CONFIRMADA);

        // Crear una factura
        Facturas factura = new Facturas(
                null,
                "001-001-000001234",
                "C:/Facturas/Nexus",
                "17243412345",
                LocalDateTime.now(),
                venta
        );
        venta.setFactura(factura);
        venta.setFechaHora(LocalDateTime.now());
        venta.setTotal(venta.calcularTotal());
        ventasController.crearVenta(venta);

    }

     
        @Test
        void findProductByCode(){
          ProductosDAO pdao = new  ProductosDAOImpl();
          String criterio="NVM001";
          Productos Listproducto= pdao.findByCode(criterio);
        System.out.println("Producto encontrado  "+ "---> "+Listproducto.getNombreProducto()+" estado activo? "+Listproducto.getActivo());
        System.out.println();

        }

        @Test
    void findVentasByNumFac(){
            VentasDAO vdao= new VentasDAOImpl();
            Ventas venta=vdao.obtenerVentaConDetalles("000000003");
            if (venta==null){
                System.out.println("Venta no encontrada");
            }else{
                System.out.println("Venta con id: "+venta.getId());
                System.out.println("Estado: "+venta.getEstado());
                System.out.println("Clente: "+venta.getCliente().getNombre().concat(venta.getCliente().getApellido()));
                System.out.println("Detalles");
                venta.getDetalles().forEach(System.out::println);
            }


        }
}


        
        
    
    

