package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Where;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "Clientes",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Where(clause = "activo = true AND estado= 'ACTIVO'")
public class Clientes {

    public Clientes(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Clientes(String nombre,String apellido) {
        this.nombre = nombre;
        this.apellido=apellido;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    
    @NotBlank(message = "El numero de cedula es obligatorio")
    @Size(min = 2, max = 10, message = "La cedula debe tener 10 caracteres")
    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadosUsuariosClientes estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Ventas> ventas;

    // Relación OneToMany con Dirección
    // Un cliente puede tener múltiples direcciones (casa, trabajo, etc.)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();


    @Column(nullable = false)
    private boolean activo = true;

    // Se ejecuta antes de INSERT - establece ambas fechas por primera vez
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
        this.fechaActualizacion = LocalDate.now();
    }

    // Se ejecuta antes de UPDATE - solo actualiza la fecha de modificación
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDate.now();
    }

    public String getNombreCompleto(){
        return nombre+" "+apellido;
    }

}
