#!/bin/bash

# Script de test des endpoints Article Service
# Base URL
BASE_URL="https://satisfactorysquad.freeboxos.fr/article-service"

echo "🚀 Tests API Article Service"
echo "================================"

# Couleurs pour les outputs
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# ================================
# 1. HEALTH CHECK
# ================================
echo -e "\n${BLUE}1️⃣  TEST HEALTH CHECK${NC}"
echo "GET $BASE_URL/health"
curl -X GET "$BASE_URL/health" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n"

# ================================
# 2. LISTER LES CATÉGORIES
# ================================
echo -e "\n${BLUE}2️⃣  LISTER LES CATÉGORIES${NC}"
echo "GET $BASE_URL/categories"
CATEGORIES_RESPONSE=$(curl -s -X GET "$BASE_URL/categories" \
  -H "Accept: application/json")
echo "$CATEGORIES_RESPONSE" | jq '.' 2>/dev/null || echo "$CATEGORIES_RESPONSE"

# ================================
# 3. CRÉER UNE CATÉGORIE
# ================================
echo -e "\n${BLUE}3️⃣  CRÉER UNE CATÉGORIE${NC}"
echo "POST $BASE_URL/categories"
CATEGORIE_RESPONSE=$(curl -s -X POST "$BASE_URL/categories" \
  -H "Content-Type: application/json" \
  -d '{
    "libelle": "Électronique",
    "description": "Articles électroniques",
    "parent_id": null
  }')
echo "$CATEGORIE_RESPONSE" | jq '.' 2>/dev/null || echo "$CATEGORIE_RESPONSE"

# Extraire l'ID de la catégorie
CATEGORIE_ID=$(echo "$CATEGORIE_RESPONSE" | jq -r '.id_categorie // empty' 2>/dev/null)
if [ -z "$CATEGORIE_ID" ]; then
  CATEGORIE_ID="550e8400-e29b-41d4-a716-446655440001"
  echo -e "${YELLOW}⚠️  Utilisation d'un ID d'exemple: $CATEGORIE_ID${NC}"
else
  echo -e "${GREEN}✅ Catégorie créée: $CATEGORIE_ID${NC}"
fi

# ================================
# 4. LISTER LES BOUTIQUES
# ================================
echo -e "\n${BLUE}4️⃣  LISTER LES BOUTIQUES${NC}"
echo "GET $BASE_URL/boutiques"
BOUTIQUES_RESPONSE=$(curl -s -X GET "$BASE_URL/boutiques" \
  -H "Accept: application/json")
echo "$BOUTIQUES_RESPONSE" | jq '.' 2>/dev/null || echo "$BOUTIQUES_RESPONSE"

# ================================
# 5. CRÉER UNE BOUTIQUE
# ================================
echo -e "\n${BLUE}5️⃣  CRÉER UNE BOUTIQUE${NC}"
echo "POST $BASE_URL/boutiques"
VENDEUR_ID="550e8400-e29b-41d4-a716-446655440000"
BOUTIQUE_RESPONSE=$(curl -s -X POST "$BASE_URL/boutiques" \
  -H "Content-Type: application/json" \
  -d "{
    \"nom\": \"Ma Boutique\",
    \"description\": \"Ma boutique de test\",
    \"vendeur_id\": \"$VENDEUR_ID\"
  }")
echo "$BOUTIQUE_RESPONSE" | jq '.' 2>/dev/null || echo "$BOUTIQUE_RESPONSE"

# Extraire l'ID de la boutique
BOUTIQUE_ID=$(echo "$BOUTIQUE_RESPONSE" | jq -r '.id_boutique // empty' 2>/dev/null)
if [ -z "$BOUTIQUE_ID" ]; then
  BOUTIQUE_ID="550e8400-e29b-41d4-a716-446655440002"
  echo -e "${YELLOW}⚠️  Utilisation d'un ID d'exemple: $BOUTIQUE_ID${NC}"
else
  echo -e "${GREEN}✅ Boutique créée: $BOUTIQUE_ID${NC}"
fi

# ================================
# 6. CRÉER UN ARTICLE
# ================================
echo -e "\n${BLUE}6️⃣  CRÉER UN ARTICLE${NC}"
echo "POST $BASE_URL/articles"
ARTICLE_RESPONSE=$(curl -s -X POST "$BASE_URL/articles" \
  -H "Content-Type: application/json" \
  -d "{
    \"titre\": \"iPhone 15\",
    \"description\": \"Téléphone dernière génération\",
    \"prix\": 999.99,
    \"categorie_id\": \"$CATEGORIE_ID\",
    \"boutique_id\": \"$BOUTIQUE_ID\",
    \"vendeur_id\": \"$VENDEUR_ID\"
  }")
echo "$ARTICLE_RESPONSE" | jq '.' 2>/dev/null || echo "$ARTICLE_RESPONSE"

# Extraire l'ID de l'article
ARTICLE_ID=$(echo "$ARTICLE_RESPONSE" | jq -r '.id_article // empty' 2>/dev/null)
if [ -z "$ARTICLE_ID" ]; then
  ARTICLE_ID="550e8400-e29b-41d4-a716-446655440003"
  echo -e "${YELLOW}⚠️  Utilisation d'un ID d'exemple: $ARTICLE_ID${NC}"
else
  echo -e "${GREEN}✅ Article créé: $ARTICLE_ID${NC}"
fi

# ================================
# 7. LISTER LES ARTICLES
# ================================
echo -e "\n${BLUE}7️⃣  LISTER LES ARTICLES${NC}"
echo "GET $BASE_URL/articles"
ARTICLES_RESPONSE=$(curl -s -X GET "$BASE_URL/articles" \
  -H "Accept: application/json")
echo "$ARTICLES_RESPONSE" | jq '.' 2>/dev/null || echo "$ARTICLES_RESPONSE"

# ================================
# 8. OBTENIR UN ARTICLE SPÉCIFIQUE
# ================================
echo -e "\n${BLUE}8️⃣  OBTENIR UN ARTICLE SPÉCIFIQUE${NC}"
echo "GET $BASE_URL/articles/$ARTICLE_ID"
curl -s -X GET "$BASE_URL/articles/$ARTICLE_ID" \
  -H "Accept: application/json" | jq '.' 2>/dev/null

# ================================
# 9. AJOUTER UNE PHOTO À L'ARTICLE
# ================================
echo -e "\n${BLUE}9️⃣  AJOUTER UNE PHOTO À L'ARTICLE${NC}"
echo "POST $BASE_URL/articles/$ARTICLE_ID/photos"
PHOTO_RESPONSE=$(curl -s -X POST "$BASE_URL/articles/$ARTICLE_ID/photos" \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://example.com/iphone15.jpg",
    "description": "Photo principale du produit",
    "ordre": 1
  }')
echo "$PHOTO_RESPONSE" | jq '.' 2>/dev/null || echo "$PHOTO_RESPONSE"

# ================================
# 10. LISTER LES PHOTOS DE L'ARTICLE
# ================================
echo -e "\n${BLUE}🔟 LISTER LES PHOTOS DE L'ARTICLE${NC}"
echo "GET $BASE_URL/articles/$ARTICLE_ID/photos"
curl -s -X GET "$BASE_URL/articles/$ARTICLE_ID/photos" \
  -H "Accept: application/json" | jq '.' 2>/dev/null

# ================================
# 11. METTRE À JOUR LE PRIX
# ================================
echo -e "\n${BLUE}1️⃣1️⃣ METTRE À JOUR LE PRIX${NC}"
echo "PATCH $BASE_URL/articles/$ARTICLE_ID/prix"
curl -s -X PATCH "$BASE_URL/articles/$ARTICLE_ID/prix" \
  -H "Content-Type: application/json" \
  -d '{
    "nouveau_prix": 899.99
  }' | jq '.' 2>/dev/null

# ================================
# 12. VALIDER L'ARTICLE
# ================================
echo -e "\n${BLUE}1️⃣2️⃣ VALIDER L'ARTICLE${NC}"
echo "PATCH $BASE_URL/articles/$ARTICLE_ID/valider"
curl -s -X PATCH "$BASE_URL/articles/$ARTICLE_ID/valider" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n"

# ================================
# 13. OBTENIR L'ARBORESCENCE DES CATÉGORIES
# ================================
echo -e "\n${BLUE}1️⃣3️⃣ OBTENIR L'ARBORESCENCE DES CATÉGORIES${NC}"
echo "GET $BASE_URL/categories/tree"
curl -s -X GET "$BASE_URL/categories/tree" \
  -H "Accept: application/json" | jq '.' 2>/dev/null

# ================================
# 14. FILTRER LES ARTICLES PAR STATUT
# ================================
echo -e "\n${BLUE}1️⃣4️⃣ FILTRER LES ARTICLES PAR STATUT${NC}"
echo "GET $BASE_URL/articles?statut=ACCEPTE"
curl -s -X GET "$BASE_URL/articles?statut=ACCEPTE" \
  -H "Accept: application/json" | jq '.' 2>/dev/null

# ================================
# 15. SIGNALER UN ARTICLE
# ================================
echo -e "\n${BLUE}1️⃣5️⃣ SIGNALER UN ARTICLE${NC}"
echo "POST $BASE_URL/articles/$ARTICLE_ID/signalements"
curl -s -X POST "$BASE_URL/articles/$ARTICLE_ID/signalements" \
  -H "Content-Type: application/json" \
  -d "{
    \"motif\": \"contenu_inapproprie\",
    \"description\": \"Cet article ne respecte pas les règles\",
    \"signalantId\": \"$VENDEUR_ID\"
  }" \
  -w "\nStatus: %{http_code}\n"

echo -e "\n${GREEN}✅ Tests terminés!${NC}"
