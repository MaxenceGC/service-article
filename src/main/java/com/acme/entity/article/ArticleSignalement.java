package com.acme.entity.article;

import com.acme.entity.article.enums.signalement.GraviteType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "article_signalement")
public class ArticleSignalement {

    @Id
    @GeneratedValue
    @Column(name = "id_signalement", nullable = false)
    public UUID id_signalement;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    public Article article;

    @Column(name = "signalant_id", nullable = false)
    public UUID signalant_id;

    @Column(name = "raison", nullable = false)
    public String raison;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_gravite", nullable = false)
    public GraviteType niveau_gravite;

    @Column(name = "date_signalement", nullable = false)
    public OffsetDateTime date_signalement;
}

