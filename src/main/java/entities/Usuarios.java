
package entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import lombok.*;
import javax.validation.constraints.*;


@Entity
@Table(name = "Usuarios")
@Getter
@Setter

public class Usuarios {
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles rol; // ADMIN, VENDEDOR, etc.

    @Column(nullable = false)
    private EstadosUsuariosClientes estado;

    @Column(nullable = false)
    private boolean activo = true;
    
     @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Ventas> ventas;

    public Usuarios() {
        this.fechaCreacion=LocalDate.now();
    }
     
     
}
