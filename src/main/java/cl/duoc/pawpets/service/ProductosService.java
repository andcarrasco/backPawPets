package cl.duoc.pawpets.service;

import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductosService {
    private final RepositorioProductos repositorio;

    public ProductosService(RepositorioProductos repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
