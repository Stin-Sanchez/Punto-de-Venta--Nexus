package entities;

public enum EstadosProductos {
    AGOTADO("Agotado"),
    DISPONIBLE("Disponible"),
    SIN_STOCK("Sin Stock"),
    CASI_AGOTADO("Casi Agotado"),
    CON_STOCK_MINIMO("Con Stock Minimo");
    private final String descripcion;

    EstadosProductos(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }

}
