package cl.duoc.pawpets.repository;

import cl.duoc.pawpets.model.Usuario;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioUsuarios extends MongoRepository<Usuario, ObjectId> {

    @Query("{ 'id': ?0 }")
    Optional<Usuario> buscarPorId(int id);

    // Consulta derivada: busca el correo sin distinguir mayúsculas.
    Optional<Usuario> findByEmailIgnoreCase(String email);
}
