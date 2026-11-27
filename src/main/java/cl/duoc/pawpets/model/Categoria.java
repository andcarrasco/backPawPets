package cl.duoc.pawpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Documento de la colección "categorias".
@Getter
@Setter
@Document(collection = "categorias")
public class Categoria {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador de la categoría (lo asigna la API al crear)", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Field("id")
    private int id;

    @Schema(description = "Nombre de la categoría", example = "Alimentos para perros")
    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    private String nombre;
}
