package cl.duoc.pawpets.service;

import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.RepositorioCategorias;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductosService {
    private static final Sort POR_ID = Sort.by(Sort.Direction.ASC, "id");

    private final RepositorioProductos repositorio;
    private final RepositorioCategorias categorias;

    public ProductosService(RepositorioProductos repositorio, RepositorioCategorias categorias) {
        this.repositorio = repositorio;
        this.categorias = categorias;
    }

    // Los dos filtros son opcionales: /api/productos?categoriaId=1&oferta=true
    public List<Producto> listar(Integer categoriaId, Boolean oferta) {
        List<Producto> productos = categoriaId == null
                ? repositorio.findAll(POR_ID)
                : repositorio.findByCategoriaId(categoriaId, POR_ID);
        if (oferta == null) return productos;
        return productos.stream().filter(producto -> producto.isOferta() == oferta).toList();
    }

    public Producto buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el producto con id " + id + "."));
    }

    public Producto crear(Producto producto) {
        validarCategoria(producto.getCategoriaId());
        // Mongo genera un _id nuevo.
        producto.set_id(null);
        producto.setId(siguienteId());
        return repositorio.save(producto);
    }

    public Producto actualizar(int id, Producto datos) {
        Producto existente = buscar(id);
        validarCategoria(datos.getCategoriaId());
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setImagen(datos.getImagen());
        existente.setCategoriaId(datos.getCategoriaId());
        existente.setStock(datos.getStock());
        existente.setOferta(datos.isOferta());
        return repositorio.save(existente);
    }

    public void eliminar(int id) {
        repositorio.delete(buscar(id));
    }

    private void validarCategoria(int categoriaId) {
        if (categorias.buscarPorId(categoriaId).isEmpty()) {
            throw new DatosInvalidosException("La categoría " + categoriaId + " no existe.");
        }
    }

    // El id numérico siguiente al mayor que existe.
    private int siguienteId() {
        return repositorio.findAll().stream().mapToInt(Producto::getId).max().orElse(0) + 1;
    }
}
