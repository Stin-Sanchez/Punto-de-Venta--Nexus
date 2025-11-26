package controllers;

import Excepciones.*;
import Services.ClienteService;
import Validador.ClienteValidador;
import dto.*;
import entities.Clientes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.validation.Valid;

public class ClienteController {

    private ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController() {
        this.clienteService = new ClienteService();
        this.clienteMapper = new ClienteMapper();
    }

    public ClienteController(ClienteMapper clienteMapper) {
        this.clienteMapper = clienteMapper;
    }

    public ClientesDTO crearCliente(@Valid ClientesDTO clienteDTO) {
        try {
            Clientes cliente = clienteMapper.dtoToEntity(clienteDTO);
            ClienteValidador.validar(cliente);
            Clientes clienteCreado = clienteService.crearCliente(cliente);
            return clienteMapper.entityToDto(clienteCreado);

        }   catch (ValidationException ex) {
        // Manejo de errores de validación del servicio
        String erroresFormateados = String.join("\n", ex.getErrores());
        JOptionPane.showMessageDialog(null,
                "Se encontraron los siguientes errores:\n" + erroresFormateados,
                "Errores de validación",
                JOptionPane.ERROR_MESSAGE);

        } catch (BusinessException | NotFoundExceptions e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado , complete los campos, son obligatorios para su registro",
                    "Error grave", JOptionPane.ERROR_MESSAGE);
        }

        return null; // si hubo error
    }


    public List<Clientes> obtenerClientes(){
        return ControllerHelper.executeExceptions(()->clienteService.obtenerTodosLosClientes()
                ,new ArrayList<>());
    }
    public List<Clientes> obtenerActivos()throws NotFoundExceptions,BusinessException{
        return ControllerHelper.executeExceptions(()->clienteService.listarActivos()
                ,new ArrayList<>());
    }

    public String obtenerDireccionCliente(Long id) {
        return ControllerHelper.executeExceptions(()->{
            String direccion=clienteService.getMapaDirecciones().get(id);
            if(direccion==null){
                throw new NotFoundExceptions("El cliente con ID " + id + " no tiene dirección registrada.");
            }
            return direccion;
        },
         null
    );
}
    
    public Clientes obtenerClientePorId(Long id) {
        return ControllerHelper.executeExceptions(
                () -> {
                    Clientes cliente = clienteService.findById(id);
                    if (cliente == null) {
                        throw new NotFoundExceptions("No existe cliente con ID " + id);
                    }
                    return cliente;
                },
                null
        );
    }
    public List<Clientes> buscarClientesNombreCedula(String criterioBusqueda) {
        return ControllerHelper.executeExceptions(
                () -> {
                    List<Clientes> clientes = clienteService.obtenerClientesPorNombreOcedula(criterioBusqueda);
                    if (clientes == null || clientes.isEmpty()) {
                        throw new NotFoundExceptions("No se encontraron clientes con el criterio: " + criterioBusqueda);
                    }
                    return clientes;
                },
                new ArrayList<>()
        );
    }
    
     // Método para manejo de errores desde la GUI
    public String manejarExcepcion(Exception e) {
        if (e instanceof BusinessException) {
            return "Error de negocio: " + e.getMessage();
        } else if (e instanceof NotFoundExceptions) {
            return "Cliente no encontrado: " + e.getMessage();
        } else {
            return "Error inesperado: " + e.getMessage();
        }
    }
    
      // Método para validar campos antes de enviar al service
    public boolean validarCamposBasicos(ClientesDTO cliente) {
        return cliente.getNombre() != null && !cliente.getNombre().trim().isEmpty() &&
                cliente.getApellido() != null && !cliente.getApellido().trim().isEmpty() &&
                cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty() &&
                cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty();
    }
    
    

}
