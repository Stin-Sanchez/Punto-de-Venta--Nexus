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
@Table(name = "paises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del país es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @NotBlank(message = "El código ISO es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}$", message = "El código ISO debe tener 2 letras mayúsculas")
    @Column(nullable = false, unique = true, length = 2)
    private String codigoISO;

    @NotBlank(message = "El código de área es obligatorio")
    @Pattern(regexp = "^\\+\\d{1,4}$", message = "El código debe empezar con + seguido de 1-4 dígitos")
    @Column(name = "codigo_area", nullable = false, length = 10)
    private String codigoArea;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación OneToMany con Provincia
    @OneToMany(mappedBy = "pais", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provincia> provincias = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Métodos helper para mantener sincronización bidireccional
    public void agregarProvincia(Provincia provincia) {
        provincias.add(provincia);
        provincia.setPais(this);
    }

    public void removerProvincia(Provincia provincia) {
        provincias.remove(provincia);
        provincia.setPais(null);
    }
}
