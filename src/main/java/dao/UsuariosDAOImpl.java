
package dao;
import entities.EstadosUsuariosClientes;
import entities.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

/**
 *
 * @author sjosu
 */
public class UsuariosDAOImpl implements UsuariosDAO{

    @Override
    public Usuarios create(Usuarios user) {
        
          Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            //Insert DATA IN THE bd--> transactions
            user.setEstado(EstadosUsuariosClientes.ACTIVO);
            session.save(user);
            session.getTransaction().commit();
        }catch (HibernateException e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return user;
}
    
     

    @Override
    public Usuarios finById(Long Id) {
          Session session= HibernateUtil.getSessionFactory().openSession();

            Usuarios user = session.find(Usuarios.class,Id);
            session.close();

            return user;
    }

    @Override
    public Usuarios update(Usuarios user) {
      Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            //Insert DATA IN THE bd--> transactions
            session.update(user);
            session.getTransaction().commit();
        }catch (HibernateException e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
     Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            //Insert DATA IN THE bd--> transactions
            session.beginTransaction();
            Usuarios user=  this.finById(id);

            session.delete(user);
            session.getTransaction().commit();

        }catch (HibernateException e){
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }finally {
            session.close();
        }

        return true;
    }

  
    @Override
    public boolean login(Usuarios user) {
        
        boolean isAuthenticated = false;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "FROM Usuarios u WHERE u.usuario = :usuario AND u.contrasenia = :contrasenia";
            Query<Usuarios> query = session.createQuery(hql, Usuarios.class);
            query.setParameter("usuario", user.getUsername());
            query.setParameter("contrasenia", user.getPassword());
            Usuarios usuarioAutenticado = query.uniqueResult();

            if (usuarioAutenticado != null) {
                isAuthenticated = true;
                user.setId(usuarioAutenticado.getId());
                user.setUsername(usuarioAutenticado.getUsername());
                user.setPassword(null); // No guardar la contraseña en memoria
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return isAuthenticated;
    }
}
     
    

    
        


    

