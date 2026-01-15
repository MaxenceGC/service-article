package com.acme.entity.article;

import com.acme.entity.article.enums.prix.PrixOrigineType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "prix_historique")
public class PrixHistorique {

    @Id
    @GeneratedValue
    @Column(name = "id_histo_prix", nullable = false)
    public UUID id_histo_prix;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    public Article article;

    @Column(name = "ancien_prix", nullable = false)
    public BigDecimal ancien_prix;

    @Column(name = "nouveau_prix", nullable = false)
    public BigDecimal nouveau_prix;

    @Column(name = "date_changement", nullable = false)
    public OffsetDateTime date_changement;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "origine_changement",
            columnDefinition = "prix_origine_type")
    public PrixOrigineType origine_changement;
}

