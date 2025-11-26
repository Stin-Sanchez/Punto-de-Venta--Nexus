
package entities;

import jakarta.persistence.*;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Detalle_Ventas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleVentas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "venta_id") // Clave foránea en la tabla DetalleVenta
    private Ventas venta;
    
    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Productos productos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Método para calcular subtotal
    public BigDecimal calcularSubtotal() {
        return this.subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
    }

}
