package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Administracion de categorias")
public class CategoriasController {

    private final CategoriaService servicio;

    public CategoriasController(CategoriaService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar categorias")
    @GetMapping
    public List<Categoria> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Consultar categoria por ID")
    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable int id) {
        return servicio.buscarPorId(id);
    }

    @Operation(summary = "Crear categoria")
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria datos) {
        Categoria creada = servicio.crear(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Actualizar categoria")
    @PutMapping("/{id}")
    public Categoria actualizar(
            @PathVariable int id,
            @RequestBody Categoria datos) {
        return servicio.actualizar(id, datos);
    }

    @Operation(summary = "Eliminar categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}