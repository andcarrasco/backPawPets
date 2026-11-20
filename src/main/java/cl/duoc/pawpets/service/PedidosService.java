package cl.duoc.pawpets.service;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.DireccionPedido;
import cl.duoc.pawpets.model.ItemPedido;
import cl.duoc.pawpets.model.LineaPedido;
import cl.duoc.pawpets.model.Pedido;
import cl.duoc.pawpets.model.PedidoRequest;
import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.RepositorioPedidos;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PedidosService {
    private static final Sort MAS_RECIENTES = Sort.by(Sort.Direction.DESC, "id");

    private final RepositorioPedidos repositorio;
    private final RepositorioProductos productos;

    public PedidosService(RepositorioPedidos repositorio, RepositorioProductos productos) {
        this.repositorio = repositorio;
        this.productos = productos;
    }

    // Sin usuarioId lista todos los pedidos (administración); con usuarioId, el historial de esa persona.
    public List<Pedido> listar(Integer usuarioId) {
        return usuarioId == null
                ? repositorio.findAll(MAS_RECIENTES)
                : repositorio.findByUsuarioId(usuarioId, MAS_RECIENTES);
    }

    public Pedido buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el pedido número " + id + "."));
    }

    // 1) Revisa todos los productos y su stock. 2) Si todo está bien, descuenta el stock
    // y guarda el pedido. Así no queda stock descontado de un pedido que falló a la mitad.
    public Pedido crear(PedidoRequest solicitud) {
        validarDireccion(solicitud);

        Map<Integer, Producto> revisados = new LinkedHashMap<>();
        List<LineaPedido> lineas = new ArrayList<>();
        for (ItemPedido item : solicitud.getItems()) {
            Producto producto = revisados.get(item.getProductoId());
            if (producto == null) {
                producto = productos.buscarPorId(item.getProductoId())
                        .orElseThrow(() -> new DatosInvalidosException(
                                "El producto " + item.getProductoId() + " ya no está disponible."));
                revisados.put(producto.getId(), producto);
            }
            if (item.getCantidad() > producto.getStock()) {
                throw new ConflictoException("No hay stock suficiente de " + producto.getNombre()
                        + ". Quedan " + producto.getStock() + " unidades.");
            }
            producto.setStock(producto.getStock() - item.getCantidad());
            lineas.add(new LineaPedido(producto.getId(), producto.getNombre(), item.getCantidad(),
                    producto.getPrecio(), producto.getPrecio() * item.getCantidad()));
        }

        // Descuenta el stock.
        productos.saveAll(revisados.values());

        Pedido pedido = new Pedido();
        pedido.setId(repositorio.findAll().stream().mapToInt(Pedido::getId).max().orElse(0) + 1);
        pedido.setUsuarioId(solicitud.getUsuarioId());
        pedido.setCliente(solicitud.getCliente());
        pedido.setEntrega(solicitud.getEntrega());
        pedido.setDireccion("domicilio".equals(solicitud.getEntrega()) ? solicitud.getDireccion() : null);
        pedido.setFecha(LocalDateTime.now(ZoneId.of("America/Santiago")));
        pedido.setProductos(lineas);
        pedido.setTotal(lineas.stream().mapToInt(LineaPedido::getSubtotal).sum());
        return repositorio.save(pedido);
    }

    private void validarDireccion(PedidoRequest solicitud) {
        if (!"domicilio".equals(solicitud.getEntrega())) return;
        DireccionPedido direccion = solicitud.getDireccion();
        if (direccion == null || vacio(direccion.getCalle()) || vacio(direccion.getRegion()) || vacio(direccion.getComuna())) {
            throw new DatosInvalidosException("Para despacho a domicilio indica dirección, región y comuna.");
        }
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
