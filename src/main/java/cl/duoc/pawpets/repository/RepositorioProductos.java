package cl.duoc.pawpets.repository;

import cl.duoc.pawpets.model.Producto;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioProductos extends MongoRepository<Producto, ObjectId> {

    // Busca por el campo "id" numérico (no por el _id de Mongo).
    @Query("{ 'id': ?0 }")
    Optional<Producto> buscarPorId(int id);
}
