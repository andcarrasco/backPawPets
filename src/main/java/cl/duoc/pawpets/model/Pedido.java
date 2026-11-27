package cl.duoc.pawpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Documento de la colección "pedidos". Cliente, dirección y líneas van embebidos
// en el mismo documento (en MongoDB no hacen falta tablas aparte).
@Getter
@Setter
@Document(collection = "pedidos")
public class Pedido {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Número del pedido", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Id del usuario que compró; null si compró como invitado", example = "3")
    private Integer usuarioId;

    private ClientePedido cliente;

    @Schema(description = "domicilio o retiro", example = "domicilio")
    private String entrega;

    @Schema(description = "Dirección de despacho; null si es retiro en tienda")
    private DireccionPedido direccion;

    @Schema(description = "Fecha y hora de Chile en que se confirmó la compra", example = "2026-11-19T18:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Copia de nombre y precio de cada producto al momento de comprar")
    private List<LineaPedido> productos;

    @Schema(description = "Total calculado por la API", example = "25000")
    private int total;
}
