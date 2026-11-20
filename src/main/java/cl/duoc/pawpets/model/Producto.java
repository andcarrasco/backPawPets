package cl.duoc.pawpets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Documento de la colección "productos". Mongo genera el _id y el frontend usa el campo "id" numérico.
@Getter
@Setter
@Document(collection = "productos")
public class Producto {
    // El _id interno de Mongo no se envía al frontend.
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador del producto", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Nombre del producto", example = "Master Dog Adulto 15 kg")
    private String nombre;

    @Schema(description = "Descripción corta", example = "Alimento para perros adultos en formato de 15 kg.")
    private String descripcion;

    @Schema(description = "Precio en pesos chilenos (CLP)", example = "25000")
    private int precio;

    @Schema(description = "URL de la imagen", example = "https://images.unsplash.com/photo-1589924691995-400dc9ecc119")
    private String imagen;

    @Schema(description = "Id de la categoría", example = "1")
    private int categoriaId;

    @Schema(description = "Unidades disponibles", example = "20")
    private int stock;

    @Schema(description = "Indica si el producto está en oferta", example = "false")
    private boolean oferta;
}
