package cl.duoc.pawpets.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.pawpets.exception.ConflictoException;
import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.repository.RepositorioCategorias;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriasServiceTest {

    @Mock
    private RepositorioCategorias repositorio;

    @Mock
    private RepositorioProductos productos;

    @InjectMocks
    private CategoriasService servicio;

    private Categoria categoria(int id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNombre("Juguetes");
        return categoria;
    }

    @Test
    void eliminarUnaCategoriaConProductosResponde409() {
        when(repositorio.buscarPorId(3)).thenReturn(Optional.of(categoria(3)));
        when(productos.existsByCategoriaId(3)).thenReturn(true);

        assertThrows(ConflictoException.class, () -> servicio.eliminar(3));
        verify(repositorio, never()).delete(any(Categoria.class));
    }

    @Test
    void eliminarUnaCategoriaSinProductosLaBorra() {
        Categoria vacia = categoria(5);
        when(repositorio.buscarPorId(5)).thenReturn(Optional.of(vacia));
        when(productos.existsByCategoriaId(5)).thenReturn(false);

        servicio.eliminar(5);

        verify(repositorio).delete(vacia);
    }
}
