
package dao;


import entities.Usuarios;
import javax.swing.JTable;
import javax.swing.JTextField;


public interface UsuariosDAO {
    
    /**
     * Insert a new Employee in the database
     * USING --> HIBERNATE

     * @return
     */
    Usuarios create(Usuarios user);


    /**
     * Filtered a employee for id
     * USING --> HIBERNATE
     * @param Id
     * @return
     */
    Usuarios finById(Long Id);

    Usuarios update(Usuarios user);

    /**
     *Delete a employee by ID
     * @param id
     * @return
     */
    boolean deleteById(Long id);
    
    
        public boolean login(Usuarios user) ;
      
    
}
