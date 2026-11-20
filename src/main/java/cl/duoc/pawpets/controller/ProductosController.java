package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.service.ProductosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint de referencia del proyecto base. María Pía lo completa con el CRUD.
@RestController
@Tag(name = "Productos", description = "Catálogo de productos")
public class ProductosController {
    private final ProductosService servicio;

    public ProductosController(ProductosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar los productos", description = "Devuelve el catálogo completo ordenado por id.")
    @GetMapping("/api/productos")
    public List<Producto> listar() {
        return servicio.listar();
    }
}
