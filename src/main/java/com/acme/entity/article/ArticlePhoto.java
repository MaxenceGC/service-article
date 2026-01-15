package com.acme.entity.article;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "article_photo")
public class ArticlePhoto {

    @Id
    @GeneratedValue
    @Column(name = "id_photo", nullable = false)
    public UUID id_photo;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    public Article article;

    @Column(name = "url", nullable = false)
    public String url;

    @Column(name = "ordre", nullable = false)
    public Integer ordre = 0;

    @Column(name = "date_upload", nullable = false)
    public OffsetDateTime date_upload;
}

