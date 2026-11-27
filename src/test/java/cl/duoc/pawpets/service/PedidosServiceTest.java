package cl.duoc.pawpets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.model.ClientePedido;
import cl.duoc.pawpets.model.ItemPedido;
import cl.duoc.pawpets.model.Pedido;
import cl.duoc.pawpets.model.PedidoRequest;
import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.RepositorioPedidos;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PedidosServiceTest {

    @Mock
    private RepositorioPedidos repositorio;

    @Mock
    private RepositorioProductos productos;

    @InjectMocks
    private PedidosService servicio;

    private Producto producto(int id, int precio, int stock) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Producto " + id);
        producto.setPrecio(precio);
        producto.setStock(stock);
        return producto;
    }

    private PedidoRequest solicitud(String entrega, int productoId, int cantidad) {
        ClientePedido cliente = new ClientePedido();
        cliente.setNombre("Cliente Demo");
        cliente.setEmail("cliente@gmail.com");
        ItemPedido item = new ItemPedido();
        item.setProductoId(productoId);
        item.setCantidad(cantidad);
        PedidoRequest solicitud = new PedidoRequest();
        solicitud.setCliente(cliente);
        solicitud.setEntrega(entrega);
        solicitud.setItems(List.of(item));
        return solicitud;
    }

    @Test
    void crearDescuentaElStockYCalculaElTotal() {
        Producto hueso = producto(3, 3500, 30);
        when(productos.buscarPorId(3)).thenReturn(Optional.of(hueso));
        when(repositorio.findAll()).thenReturn(List.of());
        when(repositorio.save(any(Pedido.class))).then(AdditionalAnswers.returnsFirstArg());

        Pedido pedido = servicio.crear(solicitud("retiro", 3, 2));

        assertEquals(7000, pedido.getTotal());
        assertEquals(1, pedido.getId());
        assertEquals(28, hueso.getStock());
        verify(productos).saveAll(anyIterable());
    }

    @Test
    void crearSinStockSuficienteResponde409YNoGuardaNada() {
        when(productos.buscarPorId(4)).thenReturn(Optional.of(producto(4, 15000, 1)));

        assertThrows(ConflictoException.class, () -> servicio.crear(solicitud("retiro", 4, 5)));
        verify(productos, never()).saveAll(anyIterable());
        verify(repositorio, never()).save(any(Pedido.class));
    }

    @Test
    void despachoADomicilioSinDireccionResponde400() {
        assertThrows(DatosInvalidosException.class, () -> servicio.crear(solicitud("domicilio", 1, 1)));
    }
}
