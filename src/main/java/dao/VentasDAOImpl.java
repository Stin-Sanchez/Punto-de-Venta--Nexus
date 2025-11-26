package dao;

import entities.Clientes;
import entities.Estados;
import entities.EstadosVentas;
import entities.Ventas;
import java.time.LocalDateTime;
import java.util.*;
import org.hibernate.*;
import org.hibernate.query.*;
import util.HibernateUtil;

public class VentasDAOImpl implements VentasDAO {

    @Override
    public Ventas crearVenta(Ventas ventas) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Ventas ventaGuardada = null;

        try {
            transaction = session.beginTransaction();

            // Guardar la venta con el número generado
            session.save(ventas); // Hibernate gestiona el ID automáticamente
            transaction.commit();
            ventaGuardada = ventas;

        } catch (NumberFormatException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ventaGuardada;
    }

    @Override
    public List<Ventas> obtenerTodasLasVentas() {
        List<Ventas> ventasList;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ventasList = session.createQuery("FROM Ventas v JOIN FETCH v.cliente where v.activa=:activa  ORDER BY v.numeroSerie", Ventas.class)
                    .setParameter("activa", true).list();

        }
        return ventasList;
    }

    @Override
    public Ventas buscarVentaPorId(Long Id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ventas v JOIN FETCH v.cliente LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.productos WHERE v.id = :id", Ventas.class)
                    .setParameter("id", Id)
                    .uniqueResult();
        }
    }

    @Override
    public Double TotalDEVentas() {
        return 0.0;
    }

    @Override
    public Ventas actualizarVenta(Ventas ventas) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                //Insert DATA IN THE bd--> transactions
                session.update(ventas);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
        return ventas;
    }

    @Override
    public boolean eliminarVentaPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            try{
            //Insert DATA IN THE bd--> transactions
            session.beginTransaction();
            Ventas ventas = this.buscarVentaPorId(id);

            session.delete(ventas);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        }

        return true;
    }

    @Override
    public void deleteLogico(Long id) {
               Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Ventas venta = session.get(Ventas.class, id);
            if (venta != null) {
                venta.setActiva(false); // marcamos como borrado
                venta.setEstado(EstadosVentas.ELIMINADA);
                session.merge(venta);   // actualizamos
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error en borrado lógico", e);
        }
    }
    
   
    @Override
    public int obtenerIdUltimaVenta() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        int idVenta = 0;
        try {
            transaction = session.beginTransaction();

            // Consulta para obtener el ID de la última venta registrada
            String hql = "SELECT MAX(v.id) FROM Ventas v";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            idVenta = query.uniqueResult() != null ? query.uniqueResult() : 0;

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return idVenta;
    }

    @Override
    public String obtenerUltimoNumeroDeFactura() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery("SELECT MAX(No_Factura) FROM Ventas");
            return (String) query.uniqueResult();
        }
    }

    @Override
    public List<Ventas> findByCliente(Long clienteId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ventas v JOIN FETCH v.cliente WHERE v.cliente.id = :clienteId", Ventas.class)
                    .setParameter("clienteId", clienteId)
                    .list();
        }
    }

    @Override
    public List<Ventas> findByEstado(EstadosVentas estado) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ventas v JOIN FETCH v.cliente WHERE v.estado = :estado", Ventas.class)
                    .setParameter("estado", estado)
                    .list();
        }
    }

    @Override
    public List<Ventas> findByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Ventas v JOIN FETCH v.cliente WHERE v.fechaHora BETWEEN :inicio AND :fin", Ventas.class)
                    .setParameter("inicio", fechaInicio)
                    .setParameter("fin", fechaFin)
                    .list();
        }
    }

    @Override
    public Map<Long, String> obtenerClientes() {
              Map<Long, String> clientes = new HashMap<>();
               Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Trae todos los clientes como entidades
            List<Clientes> lista = session.createQuery("FROM Clientes", Clientes.class).getResultList();

            for (Clientes c : lista) {
                clientes.put(c.getId(), c.getNombre() + " " + c.getApellido());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return clientes;
    }

    @Override
    public Ventas obtenerVentaConDetalles(String numeroFactura) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
                    SELECT f FROM Ventas f 
                    LEFT JOIN FETCH f.cliente c
                    LEFT JOIN FETCH f.detalles d
                    LEFT JOIN FETCH d.productos p
                    WHERE f.numeroSerie = :numeroSerie
                     and f.activa=true
                    """;
            return session.createQuery(hql, Ventas.class)
                    .setParameter("numeroSerie", numeroFactura)
                    .uniqueResult();
    } catch(Exception e){
        throw new RuntimeException("Error al obtener detalles de venta: " + e.getMessage(), e);
    }
    }
    }
    
    
    
    
