package cl.duoc.pawpets.repository;

import cl.duoc.pawpets.model.Categoria;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;




public interface CategoriaRepository extends MongoRepository<Categoria, ObjectId> {

    @Query("{ 'id': ?0 }")
    Optional<Categoria> buscarPorId(int id);
}
