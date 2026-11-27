package cl.duoc.pawpets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.pawpets.exception.DatosInvalidosException;
import cl.duoc.pawpets.exception.RecursoNoEncontradoException;
import cl.duoc.pawpets.model.Categoria;
import cl.duoc.pawpets.model.Producto;
import cl.duoc.pawpets.repository.RepositorioCategorias;
import cl.duoc.pawpets.repository.RepositorioProductos;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

// Pruebas unitarias del servicio de productos: los repositorios se reemplazan por mocks de Mockito,
// así no se necesita MongoDB para ejecutarlas.
@ExtendWith(MockitoExtension.class)
class ProductosServiceTest {

    @Mock
    private RepositorioProductos repositorio;

    @Mock
    private RepositorioCategorias categorias;

    @InjectMocks
    private ProductosService servicio;

    private Producto producto(int id, String nombre, boolean oferta) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setCategoriaId(1);
        producto.setOferta(oferta);
        return producto;
    }

    @Test
    void crearAsignaElSiguienteId() {
        when(categorias.buscarPorId(1)).thenReturn(Optional.of(new Categoria()));
        when(repositorio.findAll()).thenReturn(List.of(producto(1, "A", false), producto(4, "B", false)));
        when(repositorio.save(any(Producto.class))).then(AdditionalAnswers.returnsFirstArg());

        Producto creado = servicio.crear(producto(0, "Collar", false));

        assertEquals(5, creado.getId());
    }

    @Test
    void crearConCategoriaInexistenteResponde400YNoGuarda() {
        when(categorias.buscarPorId(1)).thenReturn(Optional.empty());

        assertThrows(DatosInvalidosException.class, () -> servicio.crear(producto(0, "Collar", false)));
        verify(repositorio, never()).save(any(Producto.class));
    }

    @Test
    void buscarUnIdInexistenteResponde404() {
        when(repositorio.buscarPorId(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.buscar(99));
    }

    @Test
    void listarFiltraSoloLasOfertas() {
        when(repositorio.findAll(any(Sort.class)))
                .thenReturn(List.of(producto(1, "Normal", false), producto(2, "En oferta", true)));

        List<Producto> ofertas = servicio.listar(null, true);

        assertEquals(1, ofertas.size());
        assertEquals("En oferta", ofertas.get(0).getNombre());
    }
}
