package com.acme.entity.categorie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "categorie")
public class Categorie {

    @Id
    @GeneratedValue
    @Column(name = "id_categorie", nullable = false)
    public UUID id_categorie;

    @Column(name = "libelle", nullable = false)
    public String libelle;

    @Column(name = "description")
    public String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    public Categorie parent;
}

