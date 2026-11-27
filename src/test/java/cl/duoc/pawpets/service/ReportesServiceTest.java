package cl.duoc.pawpets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import cl.duoc.pawpets.model.LineaPedido;
import cl.duoc.pawpets.model.Pedido;
import cl.duoc.pawpets.model.ReporteVentas;
import cl.duoc.pawpets.repository.RepositorioPedidos;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportesServiceTest {

    @Mock
    private RepositorioPedidos pedidos;

    @InjectMocks
    private ReportesService servicio;

    private Pedido pedido(LocalDateTime fecha, LineaPedido... lineas) {
        Pedido pedido = new Pedido();
        pedido.setFecha(fecha);
        pedido.setProductos(List.of(lineas));
        pedido.setTotal(List.of(lineas).stream().mapToInt(LineaPedido::getSubtotal).sum());
        return pedido;
    }

    @Test
    void sumaSoloLosPedidosDelMesYOrdenaElRanking() {
        LineaPedido hueso = new LineaPedido(3, "Hueso de goma", 3, 3500, 10500);
        LineaPedido masterDog = new LineaPedido(1, "Master Dog", 1, 25000, 25000);
        LineaPedido otroHueso = new LineaPedido(3, "Hueso de goma", 1, 3500, 3500);
        // El último pedido es de otro mes y no debe contarse.
        when(pedidos.findAll()).thenReturn(List.of(
                pedido(LocalDateTime.of(2026, 11, 3, 10, 0), hueso, masterDog),
                pedido(LocalDateTime.of(2026, 11, 20, 18, 30), otroHueso),
                pedido(LocalDateTime.of(2026, 10, 31, 23, 59), masterDog)));

        ReporteVentas reporte = servicio.ventasDelMes(YearMonth.of(2026, 11));

        assertEquals("2026-11", reporte.getMes());
        assertEquals(2, reporte.getCantidadPedidos());
        assertEquals(39000, reporte.getTotalVendido());
        assertEquals("Hueso de goma", reporte.getProductosMasVendidos().get(0).getNombre());
        assertEquals(4, reporte.getProductosMasVendidos().get(0).getCantidad());
    }

    @Test
    void unMesSinPedidosDevuelveCeros() {
        when(pedidos.findAll()).thenReturn(List.of());

        ReporteVentas reporte = servicio.ventasDelMes(YearMonth.of(2026, 1));

        assertEquals(0, reporte.getTotalVendido());
        assertEquals(0, reporte.getCantidadPedidos());
        assertEquals(0, reporte.getProductosMasVendidos().size());
    }
}
