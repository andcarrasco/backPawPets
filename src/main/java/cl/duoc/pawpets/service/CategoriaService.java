package cl.duoc.pawpets.service;

import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.repository.CategoriaRepository;
import cl.duoc.pawpets.repository.RepositorioProductos;
import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.exception.ConflictoException;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categorias;
    private final RepositorioProductos productos;

    public CategoriaService(
            CategoriaRepository categorias,
            RepositorioProductos productos) {
        this.categorias = categorias;
        this.productos = productos;
    }

    public List<Categoria> listar() {
        return categorias.findAll(
            Sort.by(Sort.Direction.ASC, "id")
        );
    }

    public Categoria buscarPorId(int id) {
        return categorias.buscarPorId(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Categoria no encontrada: " + id
                )
            );
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException(
                "El nombre de la categoria es obligatorio."
            );
        }
        return nombre.trim();
    }

    private void comprobarDuplicado(String nombre, int idExcluir) {
        boolean duplicado = categorias.findAll().stream()
            .anyMatch(c ->
                c.getId() != idExcluir &&
                c.getNombre() != null &&
                c.getNombre().trim().equalsIgnoreCase(nombre)
            );

        if (duplicado) {
            throw new ConflictoException(
                "Ya existe una categoria con ese nombre."
            );
        }
    }

    public Categoria crear(Categoria datos) {
        if (datos == null) {
            throw new DatosInvalidosException(
                "Debe proporcionar los datos de la categoria."
            );
        }

        String nombre = validarNombre(datos.getNombre());
        comprobarDuplicado(nombre, -1);

        int nuevoId = categorias.findAll().stream()
            .mapToInt(Categoria::getId)
            .max()
            .orElse(0) + 1;

        Categoria nueva = new Categoria(nuevoId, nombre);
        return categorias.save(nueva);
    }

    public Categoria actualizar(int id, Categoria datos) {
        Categoria existente = buscarPorId(id);

        if (datos == null) {
            throw new DatosInvalidosException(
                "Debe proporcionar los datos de la categoria."
            );
        }

        String nombre = validarNombre(datos.getNombre());
        comprobarDuplicado(nombre, id);

        existente.setNombre(nombre);
        return categorias.save(existente);
    }

    public void eliminar(int id) {
        Categoria existente = buscarPorId(id);

        boolean tieneProductos = productos.findAll().stream()
            .anyMatch(p -> p.getCategoriaId() == id);

        if (tieneProductos) {
            throw new ConflictoException(
                "No se puede eliminar una categoria con productos asociados."
            );
        }

        categorias.delete(existente);
    }
}
