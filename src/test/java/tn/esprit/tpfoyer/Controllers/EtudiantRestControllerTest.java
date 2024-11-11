package tn.esprit.tpfoyer.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.control.EtudiantRestController;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EtudiantRestControllerTest {

    @Mock
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(etudiantRestController).build();
    }

    @Test
    void testGetEtudiants() throws Exception {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(new Etudiant(1L, "John", "Doe", 123456789L, null, null));

        when(etudiantService.retrieveAllEtudiants()).thenReturn(etudiants);

        mockMvc.perform(get("/etudiant/retrieve-all-etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomEtudiant").value("John"));

        verify(etudiantService, times(1)).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant(1L, "John", "Doe", 123456789L, null, null);
        when(etudiantService.retrieveEtudiant(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEtudiant").value("John"));

        verify(etudiantService, times(1)).retrieveEtudiant(1L);
    }

    @Test
    void testAddEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant(1L, "John", "Doe", 123456789L, null, null);
        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\": \"John\", \"prenomEtudiant\": \"Doe\", \"cinEtudiant\": 123456789}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEtudiant").value("John"));

        verify(etudiantService, times(1)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testModifyEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant(1L, "Jane", "Doe", 123456789L, null, null);
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(put("/etudiant/modify-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\": \"Jane\", \"prenomEtudiant\": \"Doe\", \"cinEtudiant\": 123456789}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEtudiant").value("Jane"));

        verify(etudiantService, times(1)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testRemoveEtudiant() throws Exception {
        Long etudiantId = 1L;

        doNothing().when(etudiantService).removeEtudiant(etudiantId);

        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).removeEtudiant(etudiantId);
    }
}

