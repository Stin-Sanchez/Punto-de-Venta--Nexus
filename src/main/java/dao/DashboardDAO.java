package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DashboardDAO {

    public BigDecimal getVentasDelDia();
    public BigDecimal getPromedioVentasMensual();
    public Long getTotalClientes();
    public Long getProductosVendidosHoy();
    public Long getProductosStockBajo();
    public Long getClientesAtendidosHoy();
    public List<Map<String, Object>> getProductosPopulares();
    public List<Map<String, Object>> getVentasPorHora();
    public List<Map<String, Object>> getMetodosPagoHoy();
    public List<Map<String, Object>> getProductosProximosVencer();
    public List<Map<String, Object>> getUltimasActividades();
}