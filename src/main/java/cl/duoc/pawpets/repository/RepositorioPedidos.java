package cl.duoc.pawpets.repository;

import cl.duoc.pawpets.model.Pedido;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioPedidos extends MongoRepository<Pedido, ObjectId> {

    @Query("{ 'id': ?0 }")
    Optional<Pedido> buscarPorId(int id);

    // Historial de compras de un usuario.
    List<Pedido> findByUsuarioId(Integer usuarioId, Sort orden);
}
