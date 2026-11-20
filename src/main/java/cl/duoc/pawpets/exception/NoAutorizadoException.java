package cl.duoc.pawpets.exception;

// Credenciales incorrectas en el login. Responde 401.
public class NoAutorizadoException extends RuntimeException {
    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
