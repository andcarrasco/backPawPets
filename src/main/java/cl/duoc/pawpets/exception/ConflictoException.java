package cl.duoc.pawpets.exception;

// Conflicto con los datos guardados: correo repetido, stock insuficiente,
// categoría con productos, etc. Responde 409.
public class ConflictoException extends RuntimeException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
