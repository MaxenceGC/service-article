#!/bin/bash
# Script de déploiement de la stack complète

set -e

NAMESPACE="production"

echo "=== Déploiement de la stack production ==="
echo ""

# 1. Créer le namespace
echo "[1/3] Création du namespace..."
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -
echo "✓ Namespace créé"
echo ""

# 2. Appliquer tous les manifests
echo "[2/3] Application des manifests Kubernetes..."
kubectl apply -f complete-stack.yaml
echo "✓ Manifests appliqués"
echo ""

# 3. Attendre que les pods démarrent
echo "[3/3] Attente du démarrage des pods..."
kubectl wait --for=condition=ready pod -l app=postgresql-article -n $NAMESPACE --timeout=120s
kubectl wait --for=condition=ready pod -l app=postgresql-user -n $NAMESPACE --timeout=120s
kubectl wait --for=condition=ready pod -l app=article-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=user-service -n $NAMESPACE --timeout=300s
echo "✓ Tous les pods sont prêts"
echo ""

echo "=== Déploiement réussi ==="
echo ""
echo "Services disponibles:"
kubectl get svc -n $NAMESPACE
echo ""
echo "Pods:"
kubectl get pods -n $NAMESPACE
echo ""
echo "Accès:"
echo "  - article-service: https://satisfactorysquad.freeboxos.fr/article-service"
echo "  - user-service: https://satisfactorysquad.freeboxos.fr/user-service"
