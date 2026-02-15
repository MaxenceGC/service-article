# Migration GitLab-CI → GitHub Actions

## 📊 Comparaison

| Feature | GitLab-CI | GitHub Actions |
|---------|-----------|-----------------|
| Tests unitaires | ✅ Maven (3.9-eclipse-temurin-21) | ✅ Maven + setup-java action |
| Tests d'intégration | ✅ Service PostgreSQL | ✅ Service PostgreSQL + health check |
| Build JAR | ✅ Maven package | ✅ Maven package |
| Cache Maven | ✅ `.m2/repository` | ✅ Automatique avec setup-java |
| Build Docker | ✅ Docker-in-Docker | ✅ Buildx (plus rapide, multi-platform) |
| Push Registry | ✅ `registry.gitlab.com` | ✅ `ghcr.io` (GitHub Container Registry) |
| Déploiement K8s | ❌ Log seulement | ✅ kubectl apply + rollout status |
| Artifacts | ✅ Expire 1 week | ✅ Expire 7 days (similaire) |
| Triggers | ✅ Branch commit | ✅ Push + PR sur main |

## 🔄 Équivalences

### Tests unitaires
```yaml
# GitLab-CI
verify:
  script:
    - ./mvnw $MAVEN_CLI_OPTS -DskipTests=false -DskipITs=true verify

# GitHub Actions
verify:
  run: ./mvnw $MAVEN_OPTS -DskipTests=false -DskipITs=true verify
```

### Tests d'intégration
```yaml
# GitLab-CI
services:
  - name: postgres:16-alpine

# GitHub Actions
services:
  postgres:
    image: postgres:16-alpine
    options: --health-cmd pg_isready ...
```

### Build Docker
```yaml
# GitLab-CI
docker build --tag "$CI_REGISTRY_IMAGE:$CI_COMMIT_SHORT_SHA" .
docker push "$CI_REGISTRY_IMAGE:$CI_COMMIT_SHORT_SHA"

# GitHub Actions (plus avancé)
docker/build-push-action avec:
  - Multi-tag automatique
  - Cache Buildx
  - Support multi-platform
```

## 🎯 Avantages GitHub Actions

1. **Cache plus intelligent** : Automatique avec `actions/setup-java@v4`
2. **Docker Buildx** : Plus rapide, support multi-arch (arm64, amd64, etc)
3. **Registry intégré** : GHCR sans config supplémentaire
4. **Meilleure isolation** : GITHUB_TOKEN unique par workflow
5. **Debugging facile** : Web UI plus claire + logs détaillés
6. **Gratuit** : Même limites que GitLab (2000 min/mois)

## ⚙️ Configuration post-migration

### GitHub Secrets (si déploiement K8s)
```bash
# Settings → Secrets and variables → Actions
# Ajouter: KUBECONFIG
```

### GitHub Container Registry (GHCR)
- Authentification automatique avec `secrets.GITHUB_TOKEN`
- Images publiques par défaut (configurable)
- Endpoint: `ghcr.io/MaxenceGC/service-article`

### Image tags
```
ghcr.io/MaxenceGC/service-article:latest      # Main branch
ghcr.io/MaxenceGC/service-article:main-abc123  # Commit SHA
ghcr.io/MaxenceGC/service-article:v1.0.0      # Git tag
```

## 📝 Passer au déploiement sur GitHub

### 1. Vérifier la pipeline
Allez sur **GitHub → Actions** et vérifiez que la pipeline passe ✅

### 2. Configurer Kubernetes (optionnel)
```bash
# Si vous voulez le déploiement automatique K8s
# Settings → Secrets → KUBECONFIG
```

### 3. Supprimer GitLab-CI (optionnel)
```bash
git rm .gitlab-ci.yml
git rm -r .gitlab/
git commit -m "Remove GitLab CI configuration"
git push github main
```

## 🚀 Étapes suivantes

1. ✅ Pipeline GitHub Actions créée
2. ✅ Code poussé vers GitHub
3. ⏳ Configurer secrets si K8s needed
4. ✅ Monitorer les workflows sur GitHub → Actions

---

**Status** : Migration terminée ✅
**Registry** : GHCR (ghcr.io)
**Triggers** : Push sur main + Pull Requests
