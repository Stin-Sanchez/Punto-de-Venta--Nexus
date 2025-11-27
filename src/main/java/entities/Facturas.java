package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Facturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "No_Factura",nullable = false,unique = true)
    private String numeroFactura;

    @NotBlank(message = "Debe especificar donde guardar la factura ")
    @Column(name = "Ruta_Archivo", nullable = false)
    private String rutaArchivoPDF;

    @NotBlank(message = "El RUC  es obligatorio")
    @Size( max = 15, message = "El RUC no debe sobrepasar el limite de 15 caracteres")
    @Column(name = "NO_RUC", nullable = false, length = 50)
    private String rucEmisor;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;


    @OneToOne(mappedBy = "factura" ,cascade = CascadeType.ALL )
    private Ventas venta;

    // Se ejecuta antes de INSERT
    @PrePersist
    protected void onCreate() {
        this.fechaEmision = LocalDateTime.now();
    }
}
