package cl.duoc.pawpets.exception;

// Reglas de negocio que no se pueden expresar con anotaciones (@NotBlank, @Min...). Responde 400.
public class DatosInvalidosException extends RuntimeException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
