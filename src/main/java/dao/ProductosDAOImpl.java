package dao;

import entities.Productos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import util.HibernateUtil;


public class ProductosDAOImpl implements ProductosDAO {

    @Override
    public Productos create(Productos producto) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {

            transaction = session.beginTransaction();
            session.save(producto);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return producto;
    }

    @Override
    public List<Productos> findAllProducts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Productos> prodcutList = session.createQuery("from Productos", Productos.class).list();
        prodcutList.sort(Comparator.comparing(Productos::getNombreProducto));
        session.close();
        return prodcutList;

    }

    @Override
    public Productos finById(Long Id) {

        Productos employee2;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            employee2 = session.find(Productos.class, Id);
        }

        return employee2;

    }

    @Override
    public Productos update(Productos producto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                //Insert DATA IN THE bd--> transactions
                session.update(producto);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }

        return producto;
    }

    @Override
    public boolean deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //Insert DATA IN THE bd--> transactions
            session.beginTransaction();
       try{
            Productos employee = this.finById(id);

            session.delete(employee);
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
    public void DeleteLogico(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Productos productos = session.get(Productos.class, id);
            if (productos != null) {
                productos.setActivo(false); // marcamos como borrado
                session.merge(productos);   // actualizamos
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error en borrado lógico", e);
        }
    }

    @Override
    public List<Productos> buscarPorNombre(String criterioDeBusqueda) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Productos> productos = new ArrayList<>();

        try {
          NativeQuery<Productos> query = session.createNativeQuery(
            "SELECT * FROM Productos WHERE nombre  LIKE CONCAT('%', :nombre, '%')",
            Productos.class
        );
            query.setParameter("nombre", criterioDeBusqueda);
            productos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return productos;
    }

    @Override
    public void reducirStock(Long id, int cantidadVendida) {
        
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.createQuery("UPDATE Productos SET stock = stock - :cantidad WHERE id = :id")
               .setParameter("cantidad", cantidadVendida)
               .setParameter("id", id)
               .executeUpdate();
        tx.commit();
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }
        
    }

    @Override
    public List<Productos> findByStockBajo() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Productos p WHERE p.stock <= p.stockMinimo AND p.activo = true", Productos.class)
                    .list();
        }
    }

    @Override
    public List<Productos> findActivos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Productos p WHERE p.activo = true ORDER BY p.nombreProducto", Productos.class)
                    .list();
        }
    }

    @Override
    public Productos findByCode(String codigo) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        return session.createQuery("FROM Productos p WHERE p.code= :code", Productos.class)
                .setParameter("code", codigo)
                .uniqueResult();
    }
}

    @Override
    public Productos findByName(String nombre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Productos p WHERE p.nombreProducto = :nombreProducto", Productos.class)
                    .setParameter("nombreProducto", nombre)
                    .uniqueResult();
        }
    }
}
