package cl.duoc.pawpets.service;

import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.CategoriaRepository;
import cl.duoc.pawpets.repository.RepositorioProductos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductosService {

    private final RepositorioProductos repositorio;
    private final CategoriaRepository categorias;

    public ProductosService(
            RepositorioProductos repositorio,
            CategoriaRepository categorias) {
        this.repositorio = repositorio;
        this.categorias = categorias;
    }

    public List<Producto> listar() {
        return repositorio.findAll(
            Sort.by(Sort.Direction.ASC, "id")
        );
    }

    public Producto buscarPorId(int id) {
        return repositorio.buscarPorId(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Producto no encontrado: " + id
                )
            );
    }

    private void validar(Producto producto) {

        if (producto == null) {
            throw new DatosInvalidosException(
                "Debe proporcionar los datos del producto."
            );
        }

        if (producto.getNombre() == null ||
            producto.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException(
                "El nombre del producto es obligatorio."
            );
        }

        if (producto.getPrecio() <= 0) {
            throw new DatosInvalidosException(
                "El precio debe ser mayor que cero."
            );
        }

        if (producto.getStock() < 0) {
            throw new DatosInvalidosException(
                "El stock no puede ser negativo."
            );
        }

        if (categorias.buscarPorId(
                producto.getCategoriaId()).isEmpty()) {
            throw new DatosInvalidosException(
                "La categoria seleccionada no existe."
            );
        }
    }

    public Producto crear(Producto datos) {
        validar(datos);

        int nuevoId = repositorio.findAll().stream()
            .mapToInt(Producto::getId)
            .max()
            .orElse(0) + 1;

        Producto nuevo = new Producto();
        nuevo.setId(nuevoId);
        copiarDatos(datos, nuevo);

        return repositorio.save(nuevo);
    }

    public Producto actualizar(int id, Producto datos) {
        Producto existente = buscarPorId(id);
        validar(datos);

        copiarDatos(datos, existente);

        return repositorio.save(existente);
    }

    private void copiarDatos(Producto origen, Producto destino) {
        destino.setNombre(origen.getNombre().trim());
        destino.setDescripcion(origen.getDescripcion());
        destino.setPrecio(origen.getPrecio());
        destino.setImagen(origen.getImagen());
        destino.setCategoriaId(origen.getCategoriaId());
        destino.setStock(origen.getStock());
        destino.setOferta(origen.isOferta());
    }

    public void eliminar(int id) {
        Producto existente = buscarPorId(id);
        repositorio.delete(existente);
    }

    public List<Producto> listarPorCategoria(int categoriaId) {
        if (categorias.buscarPorId(categoriaId).isEmpty()) {
            throw new RecursoNoEncontradoException(
                "Categoria no encontrada: " + categoriaId
            );
        }

        return listar().stream()
            .filter(p -> p.getCategoriaId() == categoriaId)
            .toList();
    }

    public List<Producto> listarOfertas() {
        return listar().stream()
            .filter(Producto::isOferta)
            .toList();
    }
}