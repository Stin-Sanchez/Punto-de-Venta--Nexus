
package entities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Productos")
@Getter
@Setter
@AllArgsConstructor
@Where(clause = "activo = true AND estado= 'DISPONIBLE'")
public class Productos {

 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombreProducto;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no debe superar los 200 caracteres")
    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no debe superar los 50 caracteres")
    @Column(name = "marca", length = 50, nullable = false)
    private String marca;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 30, message = "El código no debe superar los 30 caracteres")
    @Column(name = "codigo", unique = true, length = 30, nullable = false)
    private String code;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadosProductos estado;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer stockMinimo;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;


    private int VentasHoy;

    @Size(max = 255, message = "La ruta de la imagen no debe superar los 255 caracteres")
    private String imagen;

    @Column(nullable = false)
    private Boolean activo = true;


    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @Column(name = "fecha_vencimiento", updatable = false)
    private LocalDate fechaVencimiento;

    @OneToMany(mappedBy = "productos", cascade = CascadeType.ALL)
    private List<DetalleVentas> detallesVenta;

    // Constructor útil para actualizar stock sin traer todos los datos
    public Productos(Long id, Integer stock) {
        this.id = id;
        this.stock = stock;
    }

    //Constructor para hacer auditoria , fecha de creacion o actualizacion
    public Productos() {
        this.fechaCreacion=LocalDate.now();
        this.fechaActualizacion=LocalDate.now();
        this.VentasHoy=0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDate.now();
    }

    @Override
    public String toString() {
        return nombreProducto;

    }
}
