package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

// Lo que envía el frontend por cada producto del carrito: solo id y cantidad.
// Nombre, precio y total los pone la API desde la base de datos.
@Getter
@Setter
public class ItemPedido {
    @Schema(example = "1")
    private int productoId;

    @Schema(example = "2")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private int cantidad;
}
