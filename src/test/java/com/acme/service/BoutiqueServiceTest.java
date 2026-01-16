package com.acme.service;

import com.acme.controller.dto.request.BoutiqueRequest;
import com.acme.entity.boutique.Boutique;
import com.acme.repository.BoutiqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoutiqueServiceTest {

    @Mock
    BoutiqueRepository repo;

    BoutiqueService service;

    @BeforeEach
    void setUp() {
        service = new BoutiqueService();
        service.repo = repo;
    }

    @Test
    void create_persistsWithDefaults() {
        BoutiqueRequest req = new BoutiqueRequest();
        req.nom = "Ma Boutique";
        req.description = "Desc";

        Boutique created = service.create(req);

        ArgumentCaptor<Boutique> captor = ArgumentCaptor.forClass(Boutique.class);
        verify(repo).persist(captor.capture());
        Boutique persisted = captor.getValue();

        assertNotNull(persisted.vendeur_id);
        assertEquals(req.nom, persisted.nom);
        assertEquals(req.description, persisted.description);
        assertNotNull(persisted.date_creation);
        assertEquals(persisted, created);
    }

    @Test
    void create_usesProvidedVendeurId() {
        BoutiqueRequest req = new BoutiqueRequest();
        req.vendeur_id = UUID.randomUUID();
        req.nom = "Ma Boutique";

        service.create(req);

        ArgumentCaptor<Boutique> captor = ArgumentCaptor.forClass(Boutique.class);
        verify(repo).persist(captor.capture());
        Boutique persisted = captor.getValue();

        assertEquals(req.vendeur_id, persisted.vendeur_id);
    }

    @Test
    void update_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        BoutiqueRequest req = new BoutiqueRequest();

        when(repo.findById(id)).thenReturn(null);

        assertNull(service.update(id, req));
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_updatesFieldsWhenProvided() {
        UUID id = UUID.randomUUID();
        Boutique existing = new Boutique();
        existing.nom = "Ancien";
        existing.description = "Ancienne desc";

        BoutiqueRequest req = new BoutiqueRequest();
        req.nom = "Nouveau";
        req.description = null;

        when(repo.findById(id)).thenReturn(existing);
        when(repo.update(existing)).thenReturn(existing);

        Boutique updated = service.update(id, req);

        assertEquals("Nouveau", existing.nom);
        assertEquals("Ancienne desc", existing.description);
        assertEquals(existing, updated);
        verify(repo).update(existing);
    }

    @Test
    void findById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        Boutique existing = new Boutique();

        when(repo.findById(id)).thenReturn(existing);

        assertEquals(existing, service.findById(id));
        verify(repo).findById(id);
    }

    @Test
    void delete_returnsFalseWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(null);

        assertFalse(service.delete(id));
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void delete_callsRepoDeleteWhenFound() {
        UUID id = UUID.randomUUID();
        Boutique existing = new Boutique();
        when(repo.findById(id)).thenReturn(existing);

        assertTrue(service.delete(id));
        verify(repo).delete(existing);
    }

    @Test
    void listAll_delegatesToRepository() {
        when(repo.listAll()).thenReturn(List.of());

        assertEquals(0, service.listAll().size());
        verify(repo).listAll();
    }
}
