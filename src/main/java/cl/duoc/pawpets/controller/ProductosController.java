package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.service.ProductosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Productos", description = "CRUD del catálogo de productos")
public class ProductosController {
    private final ProductosService servicio;

    public ProductosController(ProductosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar productos", description = "Filtros opcionales por categoría y por oferta.")
    @GetMapping("/api/productos")
    public List<Producto> listar(
            @Parameter(description = "Id de la categoría") @RequestParam(required = false) Integer categoriaId,
            @Parameter(description = "true para ver solo ofertas") @RequestParam(required = false) Boolean oferta) {
        return servicio.listar(categoriaId, oferta);
    }

    @Operation(summary = "Ver un producto")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    @GetMapping("/api/productos/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable int id) {
        return ResponseEntity.ok(servicio.buscar(id));
    }

    @Operation(summary = "Crear un producto", description = "La API asigna el id.")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o categoría inexistente")
    @PostMapping("/api/productos")
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(producto));
    }

    @Operation(summary = "Actualizar un producto")
    @ApiResponse(responseCode = "200", description = "Producto actualizado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    @PutMapping("/api/productos/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(servicio.actualizar(id, producto));
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    @DeleteMapping("/api/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
