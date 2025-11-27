
package entities;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "Ventas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false, length = 20)
    private EstadosVentas estado; // Enum recomendado


    @NotNull(message = "El metodo de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "MetodoPago", nullable = false, length = 20)
    private MetodosPago metodosPago;

    @Positive(message = "El monto de la venta debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @NotNull(message = "La fecha y hora de la venta son obligatorias")
    private LocalDateTime fechaHora;

    @NotNull(message = "La venta debe estar asociada a un cliente")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes cliente;

    @NotNull(message = "La venta debe estar asociada a un vendedor")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuarios vendedor;


    @NotEmpty(message = "La venta debe tener al menos un detalle")
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<DetalleVentas> detalles = new ArrayList<>();

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id", referencedColumnName = "id", unique = true)
    private Facturas factura;

    @Column(nullable = false)
    private boolean activa = true;


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

    // Método para calcular el total
    public BigDecimal calcularTotal() {
        if (detalles != null) {
            total = detalles.stream()
                    .map(DetalleVentas::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return total;
    }
}
