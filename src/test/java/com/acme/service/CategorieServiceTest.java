package com.acme.service;

import com.acme.controller.dto.request.CategorieRequest;
import com.acme.entity.categorie.Categorie;
import com.acme.repository.CategorieRepository;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategorieServiceTest {

    @Mock
    CategorieRepository repo;

    CategorieService service;

    @BeforeEach
    void setUp() {
        service = new CategorieService();
        service.repo = repo;
    }

    @Test
    void create_setsParentReferenceWhenProvided() {
        CategorieRequest req = new CategorieRequest();
        req.libelle = "Parent";
        req.parent_id = UUID.randomUUID();

        Categorie parent = new Categorie();
        when(repo.getReference(Categorie.class, req.parent_id)).thenReturn(parent);

        service.create(req);

        ArgumentCaptor<Categorie> captor = ArgumentCaptor.forClass(Categorie.class);
        verify(repo).persist(captor.capture());
        Categorie persisted = captor.getValue();

        assertEquals("Parent", persisted.libelle);
        assertEquals(parent, persisted.parent);
    }

    @Test
    void findById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        Categorie existing = new Categorie();

        when(repo.findById(id)).thenReturn(existing);

        assertEquals(existing, service.findById(id));
        verify(repo).findById(id);
    }

    @Test
    void listAll_delegatesToRepository() {
        when(repo.listAll()).thenReturn(List.of());

        assertEquals(0, service.listAll().size());
        verify(repo).listAll();
    }

    @Test
    void update_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        CategorieRequest req = new CategorieRequest();

        when(repo.findById(id)).thenReturn(null);

        assertNull(service.update(id, req));
        verify(repo).findById(id);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void update_clearsParentWhenNull() {
        UUID id = UUID.randomUUID();
        Categorie existing = new Categorie();
        existing.parent = new Categorie();

        CategorieRequest req = new CategorieRequest();
        req.parent_id = null;

        when(repo.findById(id)).thenReturn(existing);
        when(repo.update(existing)).thenReturn(existing);

        Categorie updated = service.update(id, req);

        assertNull(existing.parent);
        assertEquals(existing, updated);
    }

    @Test
    void update_setsParentWhenProvided() {
        UUID id = UUID.randomUUID();
        Categorie existing = new Categorie();

        CategorieRequest req = new CategorieRequest();
        req.parent_id = UUID.randomUUID();

        Categorie parent = new Categorie();
        when(repo.findById(id)).thenReturn(existing);
        when(repo.getReference(Categorie.class, req.parent_id)).thenReturn(parent);
        when(repo.update(existing)).thenReturn(existing);

        Categorie updated = service.update(id, req);

        assertEquals(parent, existing.parent);
        assertEquals(existing, updated);
    }

    @Test
    void update_updatesFieldsWhenProvided() {
        UUID id = UUID.randomUUID();
        Categorie existing = new Categorie();
        existing.libelle = "Ancien";
        existing.description = "Ancienne desc";

        CategorieRequest req = new CategorieRequest();
        req.libelle = "Nouveau";
        req.description = "Nouvelle desc";

        when(repo.findById(id)).thenReturn(existing);
        when(repo.update(existing)).thenReturn(existing);

        Categorie updated = service.update(id, req);

        assertEquals("Nouveau", existing.libelle);
        assertEquals("Nouvelle desc", existing.description);
        assertEquals(existing, updated);
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
        Categorie existing = new Categorie();
        when(repo.findById(id)).thenReturn(existing);

        assertTrue(service.delete(id));
        verify(repo).delete(existing);
    }
}
