package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Cuerpo de POST /api/auth/login. No se guarda en la base de datos.
@Getter
@Setter
public class LoginRequest {
    @Schema(example = "cliente@gmail.com")
    @NotBlank(message = "El correo es obligatorio.")
    private String email;

    @Schema(example = "1234")
    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;
}
