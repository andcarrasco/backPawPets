package cl.duoc.pawpets.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

@Document(collection = "categorias")
public class Categoria {

    @Id
    @JsonIgnore
    private ObjectId _id;

    @Field("id")
    private int id;

    private String nombre;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor con parámetros
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y setters
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
