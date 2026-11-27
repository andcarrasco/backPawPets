package cl.duoc.pawpets.service;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.repository.RepositorioCategorias;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoriasService {
    private final RepositorioCategorias repositorio;
    private final RepositorioProductos productos;

    public CategoriasService(RepositorioCategorias repositorio, RepositorioProductos productos) {
        this.repositorio = repositorio;
        this.productos = productos;
    }

    public List<Categoria> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Categoria buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + id + "."));
    }

    public Categoria crear(Categoria categoria) {
        categoria.set_id(null);
        categoria.setId(repositorio.findAll().stream().mapToInt(Categoria::getId).max().orElse(0) + 1);
        return repositorio.save(categoria);
    }

    public Categoria actualizar(int id, Categoria datos) {
        Categoria existente = buscar(id);
        existente.setNombre(datos.getNombre());
        return repositorio.save(existente);
    }

    // No se borra una categoría que todavía tiene productos (409).
    public void eliminar(int id) {
        Categoria existente = buscar(id);
        if (productos.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar la categoría porque tiene productos asociados.");
        }
        repositorio.delete(existente);
    }
}
