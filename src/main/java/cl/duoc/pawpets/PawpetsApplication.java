package cl.duoc.pawpets;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API de PawPets",
        description = "API REST de la tienda de mascotas PawPets (DSY1104, Evaluación 3).",
        version = "1.0"))
public class PawpetsApplication {
    public static void main(String[] args) {
        // Spring inicia el servidor HTTP y detecta los componentes del paquete cl.duoc.pawpets.
        SpringApplication.run(PawpetsApplication.class, args);
    }
}
