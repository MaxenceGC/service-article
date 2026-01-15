-- extension pour UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ENUMS
CREATE TYPE article_statut AS ENUM (
    'en_attente', 'valide', 'refuse', 'banni', 'vendu'
);

CREATE TYPE prix_origine_type AS ENUM (
    'vendeur', 'auto_fraude', 'admin'
);

CREATE TYPE article_event_type AS ENUM (
    'creation', 'modification', 'validation', 'refus', 'suppression'
);

CREATE TYPE gravite_type AS ENUM (
    'leger', 'moyen', 'critique'
);

-- TABLE CATEGORIE (arborescente)
CREATE TABLE categorie (
    id_categorie    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    libelle         VARCHAR(255) NOT NULL,
    description     TEXT,
    parent_id       UUID,

    CONSTRAINT fk_categorie_parent
        FOREIGN KEY (parent_id)
        REFERENCES categorie(id_categorie)
        ON DELETE SET NULL
);

CREATE INDEX idx_categorie_parent ON categorie(parent_id);

-- TABLE BOUTIQUE
CREATE TABLE boutique (
    id_boutique     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vendeur_id      UUID NOT NULL,
    nom             VARCHAR(255) NOT NULL,
    description     TEXT,
    date_creation   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_boutique_vendeur ON boutique(vendeur_id);

-- TABLE ARTICLE (table principale)
CREATE TABLE article (
    id_article            UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    vendeur_id            UUID NOT NULL,
    boutique_id           UUID,
    categorie_id          UUID NOT NULL,

    titre                 VARCHAR(255) NOT NULL,
    description           TEXT,
    prix_actuel           NUMERIC(10,2) NOT NULL,

    statut                article_statut NOT NULL DEFAULT 'en_attente',
    motif_refus           TEXT,
    date_creation         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    date_mise_a_jour      TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    nb_signalements       INTEGER NOT NULL DEFAULT 0,
    qualite_vendeur_score INTEGER,
    meta_tags             JSONB,

    CONSTRAINT fk_article_categorie
        FOREIGN KEY (categorie_id)
        REFERENCES categorie(id_categorie)
        ON DELETE RESTRICT,

    CONSTRAINT fk_article_boutique
        FOREIGN KEY (boutique_id)
        REFERENCES boutique(id_boutique)
        ON DELETE SET NULL
);

-- INDEX ARTICLE
CREATE INDEX idx_article_categorie       ON article (categorie_id);
CREATE INDEX idx_article_boutique        ON article (boutique_id);
CREATE INDEX idx_article_prix            ON article (prix_actuel);
CREATE INDEX idx_article_statut          ON article (statut);
CREATE INDEX idx_article_meta_tags       ON article USING GIN (meta_tags);
CREATE INDEX idx_article_vendeur         ON article (vendeur_id);
CREATE INDEX idx_article_refus           ON article (motif_refus);

-- TABLE ARTICLE_PHOTO
CREATE TABLE article_photo (
    id_photo        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    article_id      UUID NOT NULL,
    url             TEXT NOT NULL,
    ordre           INTEGER NOT NULL DEFAULT 0,
    date_upload     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_photo_article
        FOREIGN KEY (article_id)
        REFERENCES article(id_article)
        ON DELETE CASCADE
);

CREATE INDEX idx_photo_article ON article_photo(article_id);
CREATE INDEX idx_photo_ordre  ON article_photo(article_id, ordre);

-- TABLE PRIX_HISTORIQUE
CREATE TABLE prix_historique (
    id_histo_prix      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    article_id         UUID NOT NULL,
    ancien_prix        NUMERIC(10,2) NOT NULL,
    nouveau_prix       NUMERIC(10,2) NOT NULL,
    date_changement    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    origine_changement prix_origine_type NOT NULL,

    CONSTRAINT fk_prix_article
        FOREIGN KEY (article_id)
        REFERENCES article(id_article)
        ON DELETE CASCADE
);

CREATE INDEX idx_prix_article       ON prix_historique(article_id);
CREATE INDEX idx_prix_changement   ON prix_historique(article_id, date_changement DESC);

-- TABLE ARTICLE_SIGNALEMENT
CREATE TABLE article_signalement (
    id_signalement    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    article_id        UUID NOT NULL,
    signalant_id      UUID NOT NULL,
    raison            TEXT NOT NULL,
    niveau_gravite    gravite_type NOT NULL,
    date_signalement  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_signalement_article
        FOREIGN KEY (article_id)
        REFERENCES article(id_article)
        ON DELETE CASCADE
);

CREATE INDEX idx_signalement_article    ON article_signalement(article_id);
CREATE INDEX idx_signalement_niveau     ON article_signalement(niveau_gravite);
CREATE INDEX idx_signalement_signalant  ON article_signalement(signalant_id);

