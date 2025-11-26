/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package entities;

/**
 *
 * @author stin-josue
 */
public enum Roles {
    
     ADMIN("Administrador"),
    VENDEDOR("Vendedor/a");
    
      private final String descripcion;
      
        Roles(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
