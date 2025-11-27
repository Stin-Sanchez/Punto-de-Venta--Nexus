package dao;

import entities.Categorias;
import entities.Clientes;
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
        return null;
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

    }

    @Override
    public void eliminar(Integer id) {

    }
}
