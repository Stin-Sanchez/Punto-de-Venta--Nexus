package entities;

public enum EstadosUsuariosClientes {

    //Estados de Usuarios
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    SUSPENDIDO("Suspendido");

    private final String descripcion;

    EstadosUsuariosClientes(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
