package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.model.ReporteVentas;
import cl.duoc.pawpets.service.ReportesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Reportes", description = "Ventas calculadas a partir de los pedidos")
public class ReportesController {
    private final ReportesService servicio;

    public ReportesController(ReportesService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Ventas de un mes", description = "Total vendido, cantidad de pedidos y productos más vendidos. Sin mes, usa el mes actual.")
    @ApiResponse(responseCode = "200", description = "Reporte calculado")
    @ApiResponse(responseCode = "400", description = "El mes no tiene el formato AAAA-MM")
    @GetMapping("/api/reportes/ventas")
    public ResponseEntity<ReporteVentas> ventas(
            @Parameter(description = "Mes en formato AAAA-MM", example = "2026-11")
            @RequestParam(required = false) String mes) {
        YearMonth periodo;
        try {
            periodo = mes == null ? YearMonth.now(ZoneId.of("America/Santiago")) : YearMonth.parse(mes);
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("El mes debe tener el formato AAAA-MM, por ejemplo 2026-11.");
        }
        return ResponseEntity.ok(servicio.ventasDelMes(periodo));
    }
}
