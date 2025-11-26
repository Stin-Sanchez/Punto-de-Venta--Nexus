package Services;

import Excepciones.*;
import Validador.*;
import dao.ClientesDAO;
import dao.ClientesDAOImpl;
import entities.Clientes;
import entities.EstadosUsuariosClientes;
import lombok.Getter;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;



public class ClienteService {

    private final ClientesDAO clienteDAO;
    
    //Inyección de dependencias por constructor
    public ClienteService() {
        this.clienteDAO = new ClientesDAOImpl();
    }
    
    /**Usamos este mapa para almacenar todas las direcciones de cada cliente 
     * Su funcion principal consiste en guardar cada direccion y mostrarla en el módulo de
     * ventas en el apartado de resumen de seleccion de la venta..........
     */
    @Getter
    private HashMap<Long, String> mapaDirecciones = new HashMap<>();

    /**
     *-crea un nuevo cliente
     * @param cliente tipo de parametro que recibe por constructor
     * @return el nuevo cliente creado
     */
    public Clientes crearCliente(Clientes cliente) {
        // 1. Validaciones básicas (Bean Validation)
        ClienteValidador.validar(cliente);

        // 2. Validaciones de negocio específicas
        validarReglaDeNegocio(cliente);

        // 3. Verificar que el email no exista
        if (clienteDAO.existsByEmail(cliente.getEmail())) {
            throw new BusinessException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        // 4. Establecer valores por defecto
        if (cliente.getEstado() == null) {
            cliente.setEstado(EstadosUsuariosClientes.ACTIVO);
        }

        try {
            return clienteDAO.crearUsuario(cliente);
        } catch (Exception e) {
            throw new BusinessException("Error al crear el cliente: " + e.getMessage(), e);
        }
    }

    public void actualizarCliente(Clientes cliente) throws BusinessException {
        if (cliente.getId() == null) {
            throw new BusinessException("El ID del cliente es requerido para actualizar");
        }
        validarReglaDeNegocio(cliente);
        clienteDAO.update(cliente);
    }
    
    
     public List<Clientes> obtenerTodosLosClientes()throws BusinessException,NotFoundExceptions {
            List<Clientes> listaClientes = clienteDAO.obtenerClientes();

         // Validación: si es null o vacía → lanzamos excepción
         if (listaClientes == null || listaClientes.isEmpty()) {
             throw new NotFoundExceptions("No se encontraron clientes en la base de datos");
         }
            // Llenamos los mapas con la información de imágenes y estados
            for (Clientes cliente : listaClientes) {
                mapaDirecciones.put(cliente.getId(), cliente.getDireccion());

            }

        return listaClientes;
    }

    public List<Clientes> listarActivos() throws BusinessException,NotFoundExceptions{

        List<Clientes> clientesActivos = clienteDAO.findActivos();
        if (clientesActivos == null || clientesActivos.isEmpty()) {
            throw new NotFoundExceptions("No hay clientes activos registrados");
        }
        return clienteDAO.findActivos();
    }


      public Clientes findById(Long id) throws NotFoundExceptions{
        Clientes cliente=clienteDAO.finById(id);
        if (cliente.getId()==null ){
            throw new NotFoundExceptions("No hay clientes registrados con ese ID");
        }
        return cliente;
    }
    public List<Clientes> obtenerClientesPorNombreOcedula(String criterioBusqueda) throws NotFoundExceptions{
        List<Clientes> clientesFiltrados = clienteDAO.findActivos();
        if(clientesFiltrados==null || clientesFiltrados.isEmpty()){
            throw new NotFoundExceptions("No hay clientes registrados con el criterio de busqueda asignado");
        }
        return clientesFiltrados;
    }


    Clientes buscarPorEmail(String email)throws NotFoundExceptions{
        Clientes cliente = clienteDAO.findByEmail(email);
        if (cliente.getEmail()==null){
            throw new NotFoundExceptions("No hay clientes registrados con el correo: "+email);
        }
        return clienteDAO.findByEmail(email);
    }

    public void eliminarCliente(Long id) throws NotFoundExceptions {
        Clientes cliente = clienteDAO.finById(id);
        if (cliente == null) {
            throw new NotFoundExceptions("Error al eliminar : Cliente no encontrado con ID: " + id);
        }
        clienteDAO.DeleteLogico(id);
    }

    //=====VALIDACIONES DE REGLAS DE NEGOCIO====
      private void validarReglaDeNegocio(Clientes cliente) throws BusinessException {
        // 1. Validar que el cliente sea mayor de edad
        if (cliente.getFechaNacimiento() != null) {
            int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
            if (edad < 18) {
                throw new BusinessException("El cliente debe ser mayor de edad (18 años). Edad actual: " + edad);
            }
            if (edad > 120) {
                throw new BusinessException("La edad no puede ser mayor a 120 años");
            }
        }

        // 2. Validar formato específico del teléfono para Ecuador
        if (cliente.getTelefono() != null && !validarTelefonoEcuador(cliente.getTelefono())) {
            throw new BusinessException("El teléfono debe ser válido para Ecuador (+593XXXXXXXXX o 09XXXXXXXX)");
        }

        // 3. Validar dominio de email permitido (ejemplo de regla de negocio)
        if (cliente.getEmail() != null) {
            validarDominioEmail(cliente.getEmail());
        }

        // 4. Validar nombres (no números)
        if (cliente.getNombre() != null && cliente.getNombre().matches(".*\\d.*")) {
            throw new BusinessException("El nombre no puede contener números");
        }

        if (cliente.getApellido() != null && cliente.getApellido().matches(".*\\d.*")) {
            throw new BusinessException("El apellido no puede contener números");
        }
    }

    private boolean validarTelefonoEcuador(String telefono) {
        // Lógica específica para validar teléfonos de Ecuador
        // Formatos válidos: +593XXXXXXXXX, 593XXXXXXXXX, 09XXXXXXXX
        return telefono.matches("^(\\+?593[0-9]{9}|09[0-9]{8})$");
    }

    private void validarDominioEmail(String email) throws  BusinessException {
        // Ejemplo: solo permitir ciertos dominios corporativos
        String[] dominiosPermitidos = {"gmail.com", "hotmail.com", "yahoo.com", "empresa.com"};
        String dominio = email.substring(email.indexOf("@") + 1).toLowerCase();

        boolean dominioValido = false;
        for (String dominioPermitido : dominiosPermitidos) {
            if (dominio.equals(dominioPermitido)) {
                dominioValido = true;
                break;
            }
        }

        if (!dominioValido) {
            throw new BusinessException("El dominio del email no está permitido: " + dominio);
        }
    }

    private void validarTransicionEstado(EstadosUsuariosClientes estadoActual, EstadosUsuariosClientes estadoNuevo) throws BusinessException {
        if (estadoActual == estadoNuevo) {
            return; // No hay cambio
        }

        // Reglas de transición de estados
        switch (estadoActual) {
            case ACTIVO:
                if (estadoNuevo != EstadosUsuariosClientes.INACTIVO && estadoNuevo != EstadosUsuariosClientes.SUSPENDIDO) {
                    throw new BusinessException(
                            String.format("Un cliente activo solo puede pasar a inactivo o suspendido. Transición inválida: %s -> %s",
                                    estadoActual, estadoNuevo));
                }
                break;
            case SUSPENDIDO:
                if (estadoNuevo != EstadosUsuariosClientes.ACTIVO && estadoNuevo != EstadosUsuariosClientes.INACTIVO) {
                    throw new BusinessException(
                            String.format("Un cliente suspendido solo puede pasar a activo o inactivo. Transición inválida: %s -> %s",
                                    estadoActual, estadoNuevo));
                }
                break;
            case INACTIVO:
                if (estadoNuevo != EstadosUsuariosClientes.ACTIVO) {
                    throw new BusinessException(
                            String.format("Un cliente inactivo solo puede reactivarse. Transición inválida: %s -> %s",
                                    estadoActual, estadoNuevo));
                }
                break;
        }
    }
    private void validarEliminacion(Clientes cliente) throws BusinessException {
        // Aquí se pueden agregar más validaciones de negocio para la eliminación
        // Por ejemplo: verificar que no tenga facturas pendientes, pedidos activos, etc.

        // Ejemplo: no se pueden eliminar clientes creados en los últimos 30 días
        LocalDate hace30Dias = LocalDate.now().minusDays(30);
        if (cliente.getFechaCreacion().isAfter(hace30Dias)) {
            throw new BusinessException("No se puede eliminar un cliente creado en los últimos 30 días");
        }
    }


}
