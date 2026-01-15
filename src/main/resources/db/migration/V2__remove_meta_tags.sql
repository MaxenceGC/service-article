-- Suppression de l’index GIN sur meta_tags
DROP INDEX IF EXISTS idx_article_meta_tags;

-- Suppression de la colonne meta_tags
ALTER TABLE article
DROP COLUMN IF EXISTS meta_tags;
