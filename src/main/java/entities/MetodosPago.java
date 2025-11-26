package entities;

public enum MetodosPago {
    EFECTIVO("Efectivo"),
    TRANSFERENCIA("Transferencia ");

    private String Descripcion;
    private final String descripcion;

    MetodosPago(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
