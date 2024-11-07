package tn.esprit.tpfoyer.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllEtudiants() {
        Etudiant e1 = new Etudiant(1L, "John", "Doe", 12345678L, null, null);
        Etudiant e2 = new Etudiant(2L, "Jane", "Doe", 87654321L, null, null);

        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();

        assertNotNull(etudiants);
        assertEquals(2, etudiants.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveEtudiant() {
        Etudiant e = new Etudiant(1L, "John", "Doe", 12345678L, null, null);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(e));

        Etudiant etudiant = etudiantService.retrieveEtudiant(1L);

        assertNotNull(etudiant);
        assertEquals("John", etudiant.getNomEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testAddEtudiant() {
        Etudiant e = new Etudiant(1L, "John", "Doe", 12345678L, null, null);

        when(etudiantRepository.save(e)).thenReturn(e);

        Etudiant addedEtudiant = etudiantService.addEtudiant(e);

        assertNotNull(addedEtudiant);
        assertEquals("John", addedEtudiant.getNomEtudiant());
        verify(etudiantRepository, times(1)).save(e);
    }

    @Test
    void testModifyEtudiant() {
        Etudiant e = new Etudiant(1L, "John", "Doe", 12345678L, null, null);

        when(etudiantRepository.save(e)).thenReturn(e);

        Etudiant modifiedEtudiant = etudiantService.modifyEtudiant(e);

        assertNotNull(modifiedEtudiant);
        assertEquals("John", modifiedEtudiant.getNomEtudiant());
        verify(etudiantRepository, times(1)).save(e);
    }

    @Test
    void testRemoveEtudiant() {
        Long etudiantId = 1L;

        doNothing().when(etudiantRepository).deleteById(etudiantId);

        etudiantService.removeEtudiant(etudiantId);

        verify(etudiantRepository, times(1)).deleteById(etudiantId);
    }

    @Test
    void testRecupererEtudiantParCin() {
        Etudiant e = new Etudiant(1L, "John", "Doe", 12345678L, null, null);

        when(etudiantRepository.findEtudiantByCinEtudiant(12345678L)).thenReturn(e);

        Etudiant etudiant = etudiantService.recupererEtudiantParCin(12345678L);

        assertNotNull(etudiant);
        assertEquals(12345678L, etudiant.getCinEtudiant());
        verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant(12345678L);
    }
}
