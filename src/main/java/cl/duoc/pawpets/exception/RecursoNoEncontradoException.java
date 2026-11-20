package cl.duoc.pawpets.exception;

// Se lanza cuando se pide un id que no existe. El manejador global responde 404.
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
