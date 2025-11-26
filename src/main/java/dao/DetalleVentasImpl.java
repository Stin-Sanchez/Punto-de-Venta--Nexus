
package dao;

import entities.DetalleVentas;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;


public class DetalleVentasImpl implements DetalleVentasDAO {

    @Override
    public DetalleVentas create(DetalleVentas detalleVenta) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {

            transaction = session.beginTransaction();
            session.save(detalleVenta);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return detalleVenta;
    }

    @Override
    public List<DetalleVentas> obtenerDetalleVentas(String numeroFactura) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            List<DetalleVentas> ventasList = session.createQuery(
                    "FROM DetalleVentas dv WHERE  dv.venta.numeroSerie = :numeroFactura",
                    DetalleVentas.class
            ).setParameter("numeroFactura", numeroFactura).list();

            transaction.commit();
            return ventasList;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return new ArrayList<>(); // Devolver lista vacía en caso de error
        } finally {
            session.close();
        }
    }


    @Override
    public DetalleVentas finByIdCriteria(Long Id) {
        DetalleVentas detalle;
        //1.Criteria
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //1.Criteria
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<DetalleVentas> criteria = builder.createQuery(DetalleVentas.class);
            Root<DetalleVentas> root = criteria.from(DetalleVentas.class);
            Predicate filter = builder.equal(root.get("Id"), Id);
            criteria.select(root).where(filter);
            //2.Query
            detalle = session.createQuery(criteria).getSingleResult();
        }

        return detalle;
    }

    @Override
    public DetalleVentas update(DetalleVentas Detalle) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                //Insert DATA IN THE bd--> transactions
                session.update(Detalle);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }

        return Detalle;

    }
    }

    @Override
    public boolean deleteById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            //Insert DATA IN THE bd--> transactions
            DetalleVentas detalle = this.finByIdCriteria(id);

            session.delete(detalle);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }

        return true;
    }

    @Override
    public void DeleteLogico(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            DetalleVentas detalle = session.get(DetalleVentas.class, id);
            if (detalle != null) {
                detalle.setActivo(false); // marcamos como borrado
                session.merge(detalle);   // actualizamos
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error en borrado lógico", e);
        }
    }
    }
    
    



     
     
    

   
      
    

