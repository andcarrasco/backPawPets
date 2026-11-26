package cl.duoc.pawpets.controller;

import cl.duoc.pawpets.model.LoginRequest;
import cl.duoc.pawpets.model.Usuario;
import cl.duoc.pawpets.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Autenticación", description = "Inicio de sesión simple con correo y contraseña")
public class AuthController {
    private final UsuariosService servicio;

    public AuthController(UsuariosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Iniciar sesión", description = "Devuelve los datos del usuario (sin contraseña).")
    @ApiResponse(responseCode = "200", description = "Credenciales correctas")
    @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos")
    @PostMapping("/api/auth/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginRequest datos) {
        return ResponseEntity.ok(servicio.login(datos.getEmail(), datos.getPassword()));
    }
}
