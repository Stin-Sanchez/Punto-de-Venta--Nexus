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
@Table(name = "provincias", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre", "pais_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Provincia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la provincia es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El código de la provincia es obligatorio")
    @Size(min = 2, max = 10, message = "El código debe tener entre 2 y 10 caracteres")
    @Column(nullable = false, length = 10)
    private String codigo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación ManyToOne con País
    @NotNull(message = "La provincia debe pertenecer a un país")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id", nullable = false)
    private Pais pais;

    // Relación OneToMany con Ciudad
    @OneToMany(mappedBy = "provincia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ciudad> ciudades = new ArrayList<>();


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
    public void agregarCiudad(Ciudad ciudad) {
        ciudades.add(ciudad);
        ciudad.setProvincia(this);
    }

    public void removerCiudad(Ciudad ciudad) {
        ciudades.remove(ciudad);
        ciudad.setProvincia(null);
    }

}