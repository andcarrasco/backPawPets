package cl.duoc.pawpets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// Dirección de despacho (solo cuando la entrega es a domicilio).
@Getter
@Setter
public class DireccionPedido {
    @Schema(example = "Av. España 890")
    private String calle;

    @Schema(example = "Valparaíso")
    private String region;

    @Schema(example = "Viña del Mar")
    private String comuna;
}
