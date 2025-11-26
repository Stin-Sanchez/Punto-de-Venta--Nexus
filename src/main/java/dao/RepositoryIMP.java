package dao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public abstract class RepositoryIMP <T> implements Repository <T> {

    private final Class<T> entityClass;

    // Constructor que recibe la clase concreta
    public RepositoryIMP(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T porId(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = session.get(entityClass, id);
        session.close();
        return entity;
    }

    @Override
    public void crear(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(t);

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Error al crear: "+e.getMessage());
        } finally {
            session.close();
        }

    }

    @Override
    public void editar(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            //UPDATE DATA IN THE bd--> transactions
            session.update(t);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println("Se produjo un error al editar: "+e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();

    }

    @Override
    public void eliminar(Integer id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
            }
            session.getTransaction().commit();

        }catch (NullPointerException n){
            System.out.println("Se produjo un error al eliminar el dato"+n.getMessage());
        }catch (Exception e){
            System.out.println("Error : "+e.getMessage());
        }

    }

    @Override
    public List<T> listar() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> list = session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
        session.close();
        return list;

    }
}
