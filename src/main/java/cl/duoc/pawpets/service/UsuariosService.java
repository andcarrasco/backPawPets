package cl.duoc.pawpets.service;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.NoAutorizadoException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.Usuario;
import cl.duoc.pawpets.repository.RepositorioUsuarios;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {
    private final RepositorioUsuarios repositorio;

    public UsuariosService(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }

    public List<Usuario> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Usuario buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id + "."));
    }

    public Usuario crear(Usuario usuario) {
        if (vacio(usuario.getPassword())) {
            throw new DatosInvalidosException("La contraseña es obligatoria.");
        }
        if (repositorio.findByEmailIgnoreCase(usuario.getEmail().trim()).isPresent()) {
            throw new ConflictoException("Ya existe un usuario con ese correo.");
        }
        usuario.set_id(null);
        usuario.setId(repositorio.findAll().stream().mapToInt(Usuario::getId).max().orElse(0) + 1);
        usuario.setEmail(usuario.getEmail().trim());
        if (vacio(usuario.getRol())) usuario.setRol("cliente");
        return repositorio.save(usuario);
    }

    // Nombre y correo se reemplazan siempre. Los demás campos solo si vienen en la petición,
    // así el perfil puede enviar únicamente los datos que el usuario puede editar.
    public Usuario actualizar(int id, Usuario datos) {
        Usuario existente = buscar(id);
        repositorio.findByEmailIgnoreCase(datos.getEmail().trim())
                .filter(otro -> otro.getId() != id)
                .ifPresent(otro -> {
                    throw new ConflictoException("Ya existe un usuario con ese correo.");
                });

        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail().trim());
        if (datos.getRut() != null) existente.setRut(datos.getRut());
        if (datos.getRegion() != null) existente.setRegion(datos.getRegion());
        if (datos.getComuna() != null) existente.setComuna(datos.getComuna());
        if (datos.getDireccion() != null) existente.setDireccion(datos.getDireccion());
        if (!vacio(datos.getRol())) existente.setRol(datos.getRol());
        if (!vacio(datos.getPassword())) existente.setPassword(datos.getPassword());
        return repositorio.save(existente);
    }

    public void eliminar(int id) {
        repositorio.delete(buscar(id));
    }

    // Login simple con correo y contraseña. Si no coinciden: 401.
    public Usuario login(String email, String password) {
        return repositorio.findByEmailIgnoreCase(email.trim())
                .filter(usuario -> usuario.getPassword().equals(password))
                .orElseThrow(() -> new NoAutorizadoException("Correo o contraseña incorrectos."));
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
