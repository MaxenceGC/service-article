package com.acme.entity.boutique;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "boutique")
public class Boutique {

    @Id
    @GeneratedValue
    @Column(name = "id_boutique", nullable = false)
    public UUID id_boutique;

    @Column(name = "vendeur_id", nullable = false)
    public UUID vendeur_id;

    @Column(name = "nom", nullable = false)
    public String nom;

    @Column(name = "description")
    public String description;

    @Column(name = "date_creation")
    public OffsetDateTime date_creation;

    public String getNom() {
        return nom;
    }
}

