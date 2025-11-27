package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "direcciones")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

    public class Direccion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La calle principal es obligatoria")
    @Size(min = 5, max = 200, message = "La calle debe tener entre 5 y 200 caracteres")
    @Column(name = "calle_principal", nullable = false, length = 200)
    private String callePrincipal;

    @Size(max = 200, message = "La calle secundaria no puede exceder 200 caracteres")
    @Column(name = "calle_secundaria", length = 200)
    private String calleSecundaria;

    @NotBlank(message = "El número de casa es obligatorio")
    @Size(max = 20, message = "El número de casa no puede exceder 20 caracteres")
    @Column(name = "numero_casa", nullable = false, length = 20)
    private String numeroCasa;

    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    @Column(name = "referencia", length = 100)
    private String referencia;

    @Pattern(regexp = "^\\d{5,10}$", message = "El código postal debe tener entre 5 y 10 dígitos")
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;


    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación ManyToOne con Ciudad
    @NotNull(message = "La dirección debe pertenecer a una ciudad")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private Ciudad ciudad;

    // Relación ManyToOne con Cliente (NUEVA RELACIÓN)
    @NotNull(message = "La dirección debe pertenecer a un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes cliente;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}

