package cl.duoc.pawpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Documento de la colección "usuarios".
@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador del usuario (lo asigna la API al crear)", example = "3",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Field("id")
    private int id;

    @Schema(description = "Nombre completo", example = "Cliente Demo")
    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @Schema(description = "Correo, único en la tienda", example = "cliente@gmail.com")
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Ingresa un correo válido.")
    private String email;

    // WRITE_ONLY: se recibe al crear o editar, pero nunca se devuelve en las respuestas.
    @Schema(description = "Contraseña (no se devuelve en las respuestas)", example = "1234",
            accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(description = "RUT sin puntos ni guion", example = "15678901K")
    private String rut;

    @Schema(description = "Región", example = "Valparaíso")
    private String region;

    @Schema(description = "Comuna", example = "Viña del Mar")
    private String comuna;

    @Schema(description = "Dirección", example = "Av. España 890")
    private String direccion;

    @Schema(description = "Rol: cliente, vendedor o administrador", example = "cliente")
    @Pattern(regexp = "cliente|vendedor|administrador", message = "El rol debe ser cliente, vendedor o administrador.")
    private String rol;
}
