package entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ciudades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre", "provincia_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Pattern(regexp = "^\\d{5,10}$", message = "El código postal debe tener entre 5 y 10 dígitos")
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Min(value = 0, message = "La población no puede ser negativa")
    @Column(name = "poblacion")
    private Integer poblacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación ManyToOne con Provincia
    @NotNull(message = "La ciudad debe pertenecer a una provincia")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;

    // Relación OneToMany con Dirección
    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos helper
    public void agregarDireccion(Direccion direccion) {
        direcciones.add(direccion);
        direccion.setCiudad(this);
    }

    public void removerDireccion(Direccion direccion) {
        direcciones.remove(direccion);
        direccion.setCiudad(null);
    }


}
