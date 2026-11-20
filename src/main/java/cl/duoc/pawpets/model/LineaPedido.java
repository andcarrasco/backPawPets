package cl.duoc.pawpets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Una línea del pedido: copia nombre y precio para que el comprobante no cambie
// aunque después se edite el producto.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {
    private int productoId;
    private String nombre;
    private int cantidad;
    private int precio;
    private int subtotal;
}
