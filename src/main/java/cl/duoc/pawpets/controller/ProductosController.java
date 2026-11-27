package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.service.ProductosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catalogo de productos")
public class ProductosController {

    private final ProductosService servicio;

    public ProductosController(ProductosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<Producto> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Consultar productos en oferta")
    @GetMapping("/ofertas")
    public List<Producto> listarOfertas() {
        return servicio.listarOfertas();
    }

    @Operation(summary = "Consultar productos por categoria")
    @GetMapping("/categoria/{id}")
    public List<Producto> listarPorCategoria(@PathVariable int id) {
        return servicio.listarPorCategoria(id);
    }

    @Operation(summary = "Consultar producto por ID")
    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable int id) {
        return servicio.buscarPorId(id);
    }

    @Operation(summary = "Crear producto")
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto datos) {
        Producto creado = servicio.crear(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public Producto actualizar(
            @PathVariable int id,
            @RequestBody Producto datos) {
        return servicio.actualizar(id, datos);
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}