package cl.duoc.pawpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Documento de la colección "productos". Mongo genera el _id y el frontend usa el campo "id".
// Las anotaciones de validación se revisan con @Valid en el controlador (si fallan: 400).
@Getter
@Setter
@Document(collection = "productos")
public class Producto {
    // El _id interno de Mongo no se envía al frontend.
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador del producto (lo asigna la API al crear)", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Field("id")
    private int id;

    @Schema(description = "Nombre del producto", example = "Master Dog Adulto 15 kg")
    @NotBlank(message = "El nombre del producto es obligatorio.")
    private String nombre;

    @Schema(description = "Descripción corta", example = "Alimento para perros adultos en formato de 15 kg.")
    private String descripcion;

    @Schema(description = "Precio en pesos chilenos (CLP)", example = "25000")
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private int precio;

    @Schema(description = "URL de la imagen", example = "https://images.unsplash.com/photo-1589924691995-400dc9ecc119")
    private String imagen;

    @Schema(description = "Id de una categoría existente", example = "1")
    @Min(value = 1, message = "Selecciona una categoría.")
    private int categoriaId;

    @Schema(description = "Unidades disponibles", example = "20")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private int stock;

    @Schema(description = "Indica si el producto está en oferta", example = "false")
    private boolean oferta;
}
