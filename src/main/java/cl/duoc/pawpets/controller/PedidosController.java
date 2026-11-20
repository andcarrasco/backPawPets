package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Pedido;
import cl.duoc.pawpets.model.PedidoRequest;
import cl.duoc.pawpets.service.PedidosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pedidos", description = "Compras: crea el pedido, descuenta stock y entrega el historial")
public class PedidosController {
    private final PedidosService servicio;

    public PedidosController(PedidosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar pedidos", description = "Del más reciente al más antiguo. Con usuarioId entrega el historial de esa persona.")
    @GetMapping("/api/pedidos")
    public List<Pedido> listar(
            @Parameter(description = "Id del usuario") @RequestParam(required = false) Integer usuarioId) {
        return servicio.listar(usuarioId);
    }

    @Operation(summary = "Ver un pedido (comprobante)")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un pedido con ese número")
    @GetMapping("/api/pedidos/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable int id) {
        return ResponseEntity.ok(servicio.buscar(id));
    }

    @Operation(summary = "Confirmar una compra",
            description = "Valida el stock de cada producto, lo descuenta y guarda el pedido con el total calculado por la API.")
    @ApiResponse(responseCode = "201", description = "Pedido creado")
    @ApiResponse(responseCode = "400", description = "Datos del cliente o de entrega inválidos, carrito vacío o producto inexistente")
    @ApiResponse(responseCode = "409", description = "No hay stock suficiente")
    @PostMapping("/api/pedidos")
    public ResponseEntity<Pedido> crear(@Valid @RequestBody PedidoRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(solicitud));
    }
}
