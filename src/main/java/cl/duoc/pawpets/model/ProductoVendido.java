package cl.duoc.pawpets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Una fila del ranking de productos más vendidos.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoVendido {
    private int productoId;
    private String nombre;
    private int cantidad;
    private int total;
}
