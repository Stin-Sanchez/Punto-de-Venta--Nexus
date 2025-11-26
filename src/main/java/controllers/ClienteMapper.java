package controllers;

import  dto.*;
import entities.*;


public class ClienteMapper {
    // DTO → Entity
    public Clientes dtoToEntity(ClientesDTO dto) {
        if (dto == null) {
            return null;
        }

        Clientes cliente = new Clientes();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCedula(dto.getCedula());
        cliente.setActivo(dto.isActivo());
        cliente.setDireccion(dto.getDireccion());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadosUsuariosClientes.ACTIVO);
        cliente.setFechaCreacion(dto.getFechaCreacion());
        cliente.setFechaActualizacion(dto.getFechaActualizacion());

        return cliente;
    }

    // Entity → DTO
    public ClientesDTO entityToDto(Clientes entity) {
        if (entity == null) {
            return null;
        }

        ClientesDTO dto = new ClientesDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        return dto;
    }
}
