package dao;

import entities.Categorias;
import entities.Clientes;
import entities.EstadosUsuariosClientes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.Comparator;
import java.util.List;

public class CategoriasRepositoryIMP implements Repository<Categorias> {

    @Override
    public List<Categorias> listar() {
        List<Categorias> categoriasList;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoriasList = session.createQuery("from Categorias", Categorias.class).list();
            categoriasList.sort(Comparator.comparing(Categorias::getNombre));
        }

        return categoriasList;
    }

    @Override
    public Categorias porId(Integer id) {
        Categorias categoria;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoria = session.find(Categorias.class, id);
        }
        return categoria;
    }

    @Override
    public void crear(Categorias categorias) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(categorias);
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

    }


    @Override
    public void editar(Categorias categorias) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                //UPDATE DATA IN THE bd--> transactions
                session.update(categorias);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try  {
            transaction = session.beginTransaction();
            Categorias categoria = session.get(Categorias.class, id);
            if (categoria != null) {
                categoria.setActivo(false);
                // marcamos como borrado
                session.merge(categoria);   // actualizamos
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error en borrado lógico", e);
        }
    }
}
