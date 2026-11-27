package cl.duoc.pawpets.repository;

import cl.duoc.pawpets.model.Producto;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioProductos extends MongoRepository<Producto, ObjectId> {

    // Busca por el campo "id" numérico (no por el _id de Mongo).
    @Query("{ 'id': ?0 }")
    Optional<Producto> buscarPorId(int id);

    // Consultas derivadas del nombre del método: Spring Data arma la consulta sola.
    List<Producto> findByCategoriaId(int categoriaId, Sort orden);

    boolean existsByCategoriaId(int categoriaId);
}
