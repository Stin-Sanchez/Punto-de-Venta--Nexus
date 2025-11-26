package dto;

import entities.Estados;
import entities.EstadosUsuariosClientes;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
public class ClientesDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String email;
    
      
    @NotBlank(message = "El numero de cedula es obligatorio")
    @Size(min = 2, max = 10, message = "La cedula debe tener 10 caracteres")
    private String cedula;
    
     @NotBlank(message = "La dirección es obligatoria")
    @Pattern(regexp = "^[\\p{L}0-9\\s.,#-]{5,100}$",
             message = "La dirección debe tener entre 5 y 100 caracteres y solo puede contener letras, números, espacios y ., - #")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    private EstadosUsuariosClientes estado;
    private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;

    private boolean isActivo;

    public ClientesDTO(){
        this.fechaCreacion = LocalDate.now();
        this.fechaActualizacion = LocalDate.now();
        this.isActivo=true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDate.now();
    }

}
