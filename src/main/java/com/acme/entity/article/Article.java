package com.acme.entity.article;

import com.acme.entity.article.enums.ArticleStatut;
import com.acme.entity.boutique.Boutique;
import com.acme.entity.categorie.Categorie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue
    @Column(name = "id_article", nullable = false)
    public UUID id_article;

    @Column(name = "vendeur_id", nullable = false)
    public UUID vendeur_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boutique_id")
    public Boutique boutique;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id", nullable = false)
    public Categorie categorie;

    @Column(name = "titre", nullable = false)
    public String titre;

    @Column(name = "description")
    public String description;

    @Column(name = "prix_actuel", nullable = false)
    public BigDecimal prix_actuel;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "statut", nullable = false)
    public ArticleStatut statut;

    @Column(name = "motif_refus")
    public String motif_refus;

    @Column(name = "date_creation", nullable = false)
    public OffsetDateTime date_creation;

    @Column(name = "date_mise_a_jour", nullable = false)
    public OffsetDateTime date_mise_a_jour;

    @Column(name = "nb_signalements", nullable = false)
    public Integer nb_signalements = 0;

    @Column(name = "qualite_vendeur_score")
    public Integer qualite_vendeur_score;

}
