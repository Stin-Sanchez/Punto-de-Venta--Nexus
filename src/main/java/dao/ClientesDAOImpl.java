package dao;

import entities.Clientes;
import entities.Estados;
import entities.EstadosUsuariosClientes;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import util.HibernateUtil;


public class ClientesDAOImpl implements ClientesDAO {


    @Override
    public Clientes crearUsuario(Clientes cliente) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(cliente);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return cliente;

    }

    @Override
    public Clientes finById(Long Id) {

        Clientes cliente;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            cliente = session.find(Clientes.class, Id);
        }

        return cliente;
    }

    @Override
    public Clientes update(Clientes cliente) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                //UPDATE DATA IN THE bd--> transactions
                session.update(cliente);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
        return cliente;

    }

    @Override
    public boolean deleteById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            session.beginTransaction();
            try {
                Clientes cliente = this.finById(id);

                session.delete(cliente);
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
    public List<Clientes> buscarPorNombreOCedula(String criterioDeBusqueda) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Clientes> clientes = new ArrayList<>();

        try {
            NativeQuery<Clientes> query = session.createNativeQuery(
                    "SELECT * FROM clientes WHERE nombres LIKE CONCAT('%', :nombres, '%') OR cedula LIKE CONCAT('%', :cedula, '%')",
                    Clientes.class);
            query.setParameter("nombres", criterioDeBusqueda);
            query.setParameter("cedula", criterioDeBusqueda);

            clientes = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return clientes;
    }

    @Override
    public List<Clientes> obtenerClientes() {
        List<Clientes> employedList1;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            employedList1 = session.createQuery("from Clientes", Clientes.class).list();
            employedList1.sort(Comparator.comparing(Clientes::getNombre));
        }

        return employedList1;

    }

    @Override
    public void DeleteLogico(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Clientes cliente = session.get(Clientes.class, id);
            if (cliente != null) {
                cliente.setActivo(false);
                cliente.setEstado(EstadosUsuariosClientes.INACTIVO);
                // marcamos como borrado
                session.merge(cliente);   // actualizamos
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error en borrado lógico", e);
        }
    }

    @Override
    public Clientes findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Clientes> query = session.createQuery(
                    "FROM Clientes WHERE email = :email", Clientes.class);
            query.setParameter("email", email);

            Clientes cliente = query.uniqueResult();
            return cliente;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cliente por email: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Clientes> findByEstado(Estados estado) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Clientes> query = session.createQuery(
                    "FROM Clientes WHERE estado = :estado ORDER BY nombre, apellido", Clientes.class);
            query.setParameter("estado", estado);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar clientes por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(c) FROM Clientes c WHERE c.email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia por email: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByEmailAndNotId(String email, Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(c) FROM Clientes c WHERE c.email = :email AND c.id != :id", Long.class);
            query.setParameter("email", email);
            query.setParameter("id", id);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar email único: " + e.getMessage(), e);
        }
    }

    @Override
    public Long countByEstado(Estados estado) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(c) FROM Clientes c WHERE c.estado = :estado", Long.class);
            query.setParameter("estado", estado);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar clientes por estado: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Clientes> findActivos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Clientes c WHERE c.activo = true", Clientes.class)
                    .list();
        }
    }
}

    
    

