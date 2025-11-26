package entities;

public enum EstadosVentas {
    //Estados de ventas
    FACTURADA("Facturada"),
    PENDIENTE("Pendiente"),
    DISPONIBLE("Disponible"),
    CANCELADA("Cancelada"),
    ENTREGADA("Entregada"),
    CONFIRMADA("Confirmada"),
    ELIMINADA("Eliminada");

    private final String descripcion;

    EstadosVentas(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
