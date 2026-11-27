package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Respuesta de GET /api/reportes/ventas. Se calcula a partir de los pedidos; no se guarda en Mongo.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentas {
    @Schema(description = "Mes del reporte (AAAA-MM)", example = "2026-11")
    private String mes;

    @Schema(description = "Suma de los totales de los pedidos del mes", example = "53500")
    private int totalVendido;

    @Schema(description = "Cantidad de pedidos del mes", example = "2")
    private int cantidadPedidos;

    @Schema(description = "Hasta 5 productos con más unidades vendidas en el mes")
    private List<ProductoVendido> productosMasVendidos;
}
