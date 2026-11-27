package cl.duoc.pawpets.service;

import cl.duoc.pawpets.model.LineaPedido;
import cl.duoc.pawpets.model.Pedido;
import cl.duoc.pawpets.model.ProductoVendido;
import cl.duoc.pawpets.model.ReporteVentas;
import cl.duoc.pawpets.repository.RepositorioPedidos;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportesService {
    private final RepositorioPedidos pedidos;

    public ReportesService(RepositorioPedidos pedidos) {
        this.pedidos = pedidos;
    }

    // Suma los pedidos del mes y arma el ranking de productos por unidades vendidas.
    public ReporteVentas ventasDelMes(YearMonth mes) {
        List<Pedido> delMes = pedidos.findAll().stream()
                .filter(pedido -> pedido.getFecha() != null && YearMonth.from(pedido.getFecha()).equals(mes))
                .toList();

        int totalVendido = delMes.stream().mapToInt(Pedido::getTotal).sum();

        Map<Integer, ProductoVendido> porProducto = new LinkedHashMap<>();
        for (Pedido pedido : delMes) {
            for (LineaPedido linea : pedido.getProductos()) {
                ProductoVendido vendido = porProducto.computeIfAbsent(linea.getProductoId(),
                        id -> new ProductoVendido(id, linea.getNombre(), 0, 0));
                vendido.setCantidad(vendido.getCantidad() + linea.getCantidad());
                vendido.setTotal(vendido.getTotal() + linea.getSubtotal());
            }
        }
        List<ProductoVendido> ranking = new ArrayList<>(porProducto.values());
        ranking.sort(Comparator.comparingInt(ProductoVendido::getCantidad).reversed());

        return new ReporteVentas(mes.toString(), totalVendido, delMes.size(),
                ranking.subList(0, Math.min(5, ranking.size())));
    }
}
