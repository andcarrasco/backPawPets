package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Datos de contacto de quien compra (se guardan dentro del pedido).
@Getter
@Setter
public class ClientePedido {
    @Schema(example = "Cliente Demo")
    @NotBlank(message = "Ingresa el nombre del cliente.")
    private String nombre;

    @Schema(example = "cliente@gmail.com")
    @NotBlank(message = "Ingresa el correo del cliente.")
    @Email(message = "Ingresa un correo válido.")
    private String email;
}
