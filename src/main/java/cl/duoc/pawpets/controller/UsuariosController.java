package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.Usuario;
import cl.duoc.pawpets.service.UsuariosService;
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
@Tag(name = "Usuarios", description = "CRUD de usuarios. Las respuestas nunca incluyen la contraseña.")
public class UsuariosController {
    private final UsuariosService servicio;

    public UsuariosController(UsuariosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar usuarios")
    @GetMapping("/api/usuarios")
    public List<Usuario> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Ver un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id")
    @GetMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable int id) {
        return ResponseEntity.ok(servicio.buscar(id));
    }

    @Operation(summary = "Crear un usuario (registro)", description = "Si no se indica el rol, queda como cliente.")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "409", description = "El correo ya está registrado")
    @PostMapping("/api/usuarios")
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(usuario));
    }

    @Operation(summary = "Actualizar un usuario", description = "Si la contraseña viene vacía se mantiene la actual.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id")
    @ApiResponse(responseCode = "409", description = "El correo pertenece a otro usuario")
    @PutMapping("/api/usuarios/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable int id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(servicio.actualizar(id, usuario));
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id")
    @DeleteMapping("/api/usuarios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
