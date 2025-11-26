package dao;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAOIMP implements DashboardDAO {
    @Override
    public BigDecimal getVentasDelDia() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BigDecimal> query = session.createQuery(
                    "SELECT COALESCE(SUM(v.total), 0) " +
                            "FROM Ventas v " +
                            "WHERE DATE(v.fechaHora) = CURRENT_DATE", BigDecimal.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener Ventas del dia: " + e.getMessage(), e);
        }
    }


    @Override
    public Long getTotalClientes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(c) FROM Clientes c WHERE c.activo = true", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar clientes activos: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getProductosVendidosHoy() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COALESCE(SUM(dv.cantidad), 0) " +
                            "FROM DetalleVentas dv " +
                            "JOIN dv.venta v " +
                            "WHERE function('date', v.fechaHora) = current_date", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos vendidos hoy: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getPromedioVentasMensual() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String sql = """
            SELECT AVG(daily_sales.total) as promedio
            FROM (
                SELECT DATE(fechaHora) as fecha, SUM(total) as total
                FROM Ventas
                WHERE fechaHora >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                GROUP BY DATE(fechaHora)
            ) AS daily_sales
        """;
            // Ejecutar la native query y obtener el resultado
            BigDecimal promedio = (BigDecimal) session.createNativeQuery(sql)
                    .getSingleResult();

            return promedio != null ? promedio : BigDecimal.ZERO;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener promedio de ventas mensuales", e);
        }
    }

    @Override
    public Long getProductosStockBajo() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM Productos p " +
                            "WHERE p.stock <= p.stockMinimo AND p.activo = true", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos con stock bajo: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getClientesAtendidosHoy() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(DISTINCT v.cliente) " +
                            "FROM Ventas v " +
                            "WHERE function('date', v.fechaHora) = current_date", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener clientes atendidos hoy: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getProductosPopulares() {
        List<Map<String, Object>> productos = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT p.nombreProducto, SUM(dv.cantidad), " +
                            "(SUM(dv.cantidad) * 100.0 / (SELECT SUM(dv2.cantidad) " +
                            "   FROM DetalleVentas dv2 " +
                            "   JOIN dv2.venta v2 " +
                            "   WHERE function('date', v2.fechaHora) = current_date)) " +
                            "FROM Productos p " +
                            "JOIN p.detallesVenta dv " +
                            "JOIN dv.venta v " +
                            "WHERE function('date', v.fechaHora) = current_date " +
                            "GROUP BY p.id, p.nombreProducto " +
                            "ORDER BY SUM(dv.cantidad) DESC", Object[].class
            ).setMaxResults(5);

            for (Object[] row : query.list()) {
                Map<String, Object> producto = new HashMap<>();
                producto.put("nombreProducto", row[0]);
                producto.put("cantidad", row[1]);
                producto.put("porcentaje", row[2]);
                productos.add(producto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos populares: " + e.getMessage(), e);
        }

        return productos;
    }

    @Override
    public List<Map<String, Object>> getVentasPorHora() {
        List<Map<String, Object>> ventasHora = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT function('hour', v.fechaHora), COUNT(v), SUM(v.total) " +
                            "FROM Ventas v " +
                            "WHERE function('date', v.fechaHora) = current_date " +
                            "GROUP BY function('hour', v.fechaHora) " +
                            "ORDER BY function('hour', v.fechaHora)", Object[].class
            );

            for (Object[] row : query.list()) {
                Map<String, Object> venta = new HashMap<>();
                venta.put("hora", row[0]);
                venta.put("numVentas", row[1]);
                venta.put("totalMonto", row[2]);
                ventasHora.add(venta);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener ventas por hora: " + e.getMessage(), e);
        }

        return ventasHora;
    }

    @Override
    public List<Map<String, Object>> getMetodosPagoHoy() {
        List<Map<String, Object>> metodos = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT v.metodosPago, COUNT(v), SUM(v.total), " +
                            "(SUM(v.total) * 100.0 / (SELECT SUM(v2.total) " +
                            " FROM Ventas v2 " +
                            " WHERE function('date', v2.fechaHora) = current_date)) " +
                            "FROM Ventas v " +
                            "WHERE function('date', v.fechaHora) = current_date " +
                            "GROUP BY v.metodosPago " +
                            "ORDER BY SUM(v.total) DESC", Object[].class);

            List<Object[]> resultados = query.list();

            for (Object[] row : resultados) {
                Map<String, Object> metodo = new HashMap<>();
                metodo.put("metodo", row[0]);      // v.metodoPago
                metodo.put("cantidad", row[1]);    // COUNT(v)
                metodo.put("monto", row[2]);       // SUM(v.monto)
                metodo.put("porcentaje", row[3]);  // %
                metodos.add(metodo);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener métodos de pago: " + e.getMessage(), e);
        }

        return metodos;
    }

    @Override
    public List<Map<String, Object>> getProductosProximosVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(30);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT p.nombreProducto, p.fechaVencimiento, p.stock " +
                            "FROM Productos p " +
                            "WHERE p.fechaVencimiento IS NOT NULL " +
                            "AND p.fechaVencimiento > :hoy " +
                            "AND p.fechaVencimiento <= :limite " +
                            "AND p.activo = true " +
                            "ORDER BY p.fechaVencimiento ASC",
                    Object[].class
            );

            query.setParameter("hoy", hoy);
            query.setParameter("limite", limite);
            query.setMaxResults(5);

            List<Object[]> results = query.list();
            List<Map<String, Object>> proximos = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> map = new HashMap<>();
                map.put("nombre", row[0]);
                map.put("fechaVencimiento", row[1]);
                map.put("stock", row[2]);
                // Calcula días restantes en Java
                map.put("diasRestantes", hoy.until((LocalDate) row[1]).getDays());
                proximos.add(map);
            }

            return proximos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos próximos a vencer: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getUltimasActividades() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                            "SELECT v.id, v.total,CONCAT( c.nombre,' ',c.apellido), v.fechaHora " +
                                    "FROM Ventas v " +
                                    "LEFT JOIN v.cliente c " +
                                    "WHERE function('date', v.fechaHora) = current_date " +
                                    "ORDER BY v.fechaHora DESC", Object[].class)
                    .setMaxResults(3);

            List<Map<String, Object>> ventas = new ArrayList<>();

            for (Object[] row : query.list()) {
                Map<String, Object> venta = new HashMap<>();
                venta.put("id", row[0]);
                venta.put("totalMonto", row[1]);
                venta.put("cliente", row[2]);
                venta.put("fechaHora", row[3]);
                ventas.add(venta);
            }

            return ventas;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener últimas ventas: " + e.getMessage(), e);
        }
    }
}
