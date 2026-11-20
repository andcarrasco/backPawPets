package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Cuerpo de POST /api/pedidos. No se guarda tal cual: el servicio arma el Pedido.
@Getter
@Setter
public class PedidoRequest {
    @Schema(description = "Id del usuario con sesión; null si compra como invitado", example = "3")
    private Integer usuarioId;

    @Valid
    @NotNull(message = "Faltan los datos del cliente.")
    private ClientePedido cliente;

    @Schema(example = "domicilio")
    @NotBlank(message = "Selecciona una opción de entrega.")
    @Pattern(regexp = "domicilio|retiro", message = "La entrega debe ser domicilio o retiro.")
    private String entrega;

    private DireccionPedido direccion;

    @Valid
    @NotEmpty(message = "El carrito está vacío.")
    private List<ItemPedido> items;
}
