package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Bloctest {

    @InjectMocks
    private BlocServiceImpl blocService;

    @Mock
    private BlocRepository blocRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBlocs() {
        // Given
        Bloc bloc1 = new Bloc(1L, "Bloc A", 100, null, null);
        Bloc bloc2 = new Bloc(2L, "Bloc B", 75, null, null);
        List<Bloc> blocs = Arrays.asList(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(blocs);

        // When
        List<Bloc> result = blocService.retrieveAllBlocs();

        // Then
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testAddBloc() {
        // Given
        Bloc bloc = new Bloc(1L, "Bloc A", 100, null, null);
        when(blocRepository.save(any())).thenReturn(bloc);

        // When
        Bloc result = blocService.addBloc(bloc);

        // Then
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).save(any());
    }

    @Test
    void testRetrieveBloc() {
        // Given
        Bloc bloc = new Bloc(1L, "Bloc A", 100, null, null);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // When
        Bloc result = blocService.retrieveBloc(1L);

        // Then
        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testRemoveBloc() {
        // Given
        long idBloc = 1L;

        // When
        blocService.removeBloc(idBloc);

        // Then
        verify(blocRepository, times(1)).deleteById(idBloc);
    }

    @Test
    void testRetrieveBlocsSelonCapacite() {
        // Given
        Bloc bloc1 = new Bloc(1L, "Bloc A", 100, null, null);
        Bloc bloc2 = new Bloc(2L, "Bloc B", 75, null, null);
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // When
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(80);

        // Then
        assertEquals(1, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testTrouverBlocsSansFoyer() {
        // Given
        Bloc bloc1 = new Bloc(1L, "Bloc A", 100, null, null);
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(Arrays.asList(bloc1));

        // When
        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        // Then
        assertEquals(1, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void testTrouverBlocsParNomEtCap() {
        // Given
        Bloc bloc1 = new Bloc(1L, "Bloc A", 100, null, null);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100)).thenReturn(Arrays.asList(bloc1));

        // When
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc A", 100);

        // Then
        assertEquals(1, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Bloc A", 100);
    }
}
