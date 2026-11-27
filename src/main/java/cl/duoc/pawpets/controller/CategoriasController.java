package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.service.CategoriasService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Categorías", description = "CRUD de las categorías del catálogo")
public class CategoriasController {
    private final CategoriasService servicio;

    public CategoriasController(CategoriasService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar categorías")
    @GetMapping("/api/categorias")
    public List<Categoria> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Ver una categoría")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @ApiResponse(responseCode = "404", description = "No existe una categoría con ese id")
    @GetMapping("/api/categorias/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable int id) {
        return ResponseEntity.ok(servicio.buscar(id));
    }

    @Operation(summary = "Crear una categoría")
    @ApiResponse(responseCode = "201", description = "Categoría creada")
    @ApiResponse(responseCode = "400", description = "Falta el nombre")
    @PostMapping("/api/categorias")
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(categoria));
    }

    @Operation(summary = "Actualizar una categoría")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada")
    @ApiResponse(responseCode = "404", description = "No existe una categoría con ese id")
    @PutMapping("/api/categorias/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable int id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(servicio.actualizar(id, categoria));
    }

    @Operation(summary = "Eliminar una categoría", description = "No se puede si tiene productos asociados.")
    @ApiResponse(responseCode = "204", description = "Categoría eliminada")
    @ApiResponse(responseCode = "404", description = "No existe una categoría con ese id")
    @ApiResponse(responseCode = "409", description = "La categoría tiene productos asociados")
    @DeleteMapping("/api/categorias/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
