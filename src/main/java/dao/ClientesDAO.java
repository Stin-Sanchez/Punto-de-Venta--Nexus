
package dao;
import entities.Clientes;
import entities.Estados;
import java.util.List;

public interface ClientesDAO {

    /**
     * Insert a new CLIENTE in the database USING --> HIBERNATE
     *
     * @return
     */
    Clientes crearUsuario(Clientes cliente);

    /**
     * Filtered a employee for id USING --> HIBERNATE
     *
     * @param Id
     * @return
     */
    Clientes finById(Long Id);

    Clientes update(Clientes cliente);

    /**
     * Delete a employee by ID
     *
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    public List<Clientes> buscarPorNombreOCedula(String criterioDeBusqueda);

    public List<Clientes> obtenerClientes();
    
     public void DeleteLogico(Long id);

    // Métodos personalizados
    Clientes findByEmail(String email);
    List<Clientes> findByEstado(Estados estado);
    boolean existsByEmail(String email);
    boolean existsByEmailAndNotId(String email, Long id);
    Long countByEstado(Estados estado);
    public List<Clientes> findActivos();

}
    

