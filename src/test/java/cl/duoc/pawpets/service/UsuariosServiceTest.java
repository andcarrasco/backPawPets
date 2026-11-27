package cl.duoc.pawpets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.exception.NoAutorizadoException;
import cl.duoc.pawpets.model.Usuario;
import cl.duoc.pawpets.repository.RepositorioUsuarios;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuariosServiceTest {

    @Mock
    private RepositorioUsuarios repositorio;

    @InjectMocks
    private UsuariosService servicio;

    private Usuario usuario(int id, String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Cliente Demo");
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRol("cliente");
        return usuario;
    }

    @Test
    void crearConUnCorreoRepetidoResponde409() {
        when(repositorio.findByEmailIgnoreCase("cliente@gmail.com"))
                .thenReturn(Optional.of(usuario(3, "cliente@gmail.com", "1234")));

        assertThrows(ConflictoException.class,
                () -> servicio.crear(usuario(0, "cliente@gmail.com", "abcd")));
        verify(repositorio, never()).save(any(Usuario.class));
    }

    @Test
    void loginConDatosCorrectosDevuelveElUsuario() {
        when(repositorio.findByEmailIgnoreCase("cliente@gmail.com"))
                .thenReturn(Optional.of(usuario(3, "cliente@gmail.com", "1234")));

        Usuario encontrado = servicio.login("cliente@gmail.com", "1234");

        assertEquals(3, encontrado.getId());
    }

    @Test
    void loginConContrasenaIncorrectaResponde401() {
        when(repositorio.findByEmailIgnoreCase("cliente@gmail.com"))
                .thenReturn(Optional.of(usuario(3, "cliente@gmail.com", "1234")));

        assertThrows(NoAutorizadoException.class, () -> servicio.login("cliente@gmail.com", "9999"));
    }

    @Test
    void actualizarSinContrasenaMantieneLaAnterior() {
        when(repositorio.buscarPorId(3)).thenReturn(Optional.of(usuario(3, "cliente@gmail.com", "1234")));
        when(repositorio.findByEmailIgnoreCase("cliente@gmail.com"))
                .thenReturn(Optional.of(usuario(3, "cliente@gmail.com", "1234")));
        when(repositorio.save(any(Usuario.class))).then(AdditionalAnswers.returnsFirstArg());

        Usuario actualizado = servicio.actualizar(3, usuario(3, "cliente@gmail.com", ""));

        assertEquals("1234", actualizado.getPassword());
    }
}
