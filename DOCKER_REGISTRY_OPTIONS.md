# Configuration Docker Registry pour GitHub Actions

## 📦 Options de Registry

### 1️⃣ GitHub Container Registry (GHCR) - RECOMMANDÉ ✅

**Configuration actuelle dans la pipeline.**

#### Avantages
- ✅ Gratuit avec GitHub
- ✅ Authentification automatique (secrets.GITHUB_TOKEN)
- ✅ Stockage 500 MB gratuit (puis facturé en excédent)
- ✅ Linked à votre repository

#### Endpoints
```
ghcr.io/MaxenceGC/service-article:latest
ghcr.io/MaxenceGC/service-article:main-abc123
```

#### Utilisation
```bash
# Pull l'image
docker pull ghcr.io/MaxenceGC/service-article:latest

# Run l'image
docker run -p 8080:8080 ghcr.io/MaxenceGC/service-article:latest

# Si image privée, authentifiez d'abord
echo $GITHUB_TOKEN | docker login ghcr.io -u $GITHUB_USERNAME --password-stdin
```

---

### 2️⃣ Docker Hub (Alternative)

**Si vous préférez Docker Hub au lieu de GHCR.**

#### Configuration
Dans `.github/workflows/ci-cd.yml`, modifiez le job `build-docker` :

```yaml
build-docker:
  steps:
    - uses: docker/login-action@v3
      with:
        username: ${{ secrets.DOCKERHUB_USERNAME }}
        password: ${{ secrets.DOCKERHUB_TOKEN }}

    - uses: docker/metadata-action@v5
      id: meta
      with:
        images: docker.io/MaxenceGC/service-article  # ← Changez ici
        tags: |
          type=ref,event=branch
          type=sha
          type=semver,pattern={{version}}

    - uses: docker/build-push-action@v5
      with:
        context: .
        push: true
        tags: ${{ steps.meta.outputs.tags }}
        labels: ${{ steps.meta.outputs.labels }}
```

#### Secrets GitHub à ajouter
1. `DOCKERHUB_USERNAME` : Votre username Docker Hub
2. `DOCKERHUB_TOKEN` : Token d'accès Docker Hub

#### Génération du token Docker Hub
1. Allez sur https://hub.docker.com/settings/security
2. Créez un "New Access Token"
3. Copiez le token
4. Ajoutez-le dans GitHub Secrets

---

### 3️⃣ GitLab Registry (Garder GitLab-CI)

**Si vous voulez continuer avec GitLab en plus de GitHub.**

```yaml
# Dans .github/workflows/ci-cd.yml
build-docker:
  env:
    CI_REGISTRY: registry.gitlab.com
    CI_REGISTRY_IMAGE: registry.gitlab.com/b-s-m/article-service
    CI_REGISTRY_USER: ${{ secrets.GITLAB_USERNAME }}
    CI_REGISTRY_PASSWORD: ${{ secrets.GITLAB_TOKEN }}
  
  steps:
    - uses: docker/login-action@v3
      with:
        registry: ${{ env.CI_REGISTRY }}
        username: ${{ env.CI_REGISTRY_USER }}
        password: ${{ env.CI_REGISTRY_PASSWORD }}
```

#### Secrets à ajouter
1. `GITLAB_USERNAME` : Votre username GitLab
2. `GITLAB_TOKEN` : Personal Access Token GitLab (read_registry, write_registry)

---

### 4️⃣ Registry personnalisé (ACR, ECR, etc.)

#### Azure Container Registry (ACR)
```yaml
- uses: docker/login-action@v3
  with:
    registry: myregistry.azurecr.io
    username: ${{ secrets.ACR_USERNAME }}
    password: ${{ secrets.ACR_PASSWORD }}
```

#### AWS ECR
```yaml
- uses: aws-actions/amazon-ecr-login@v2
  with:
    aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
    aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
```

---

## 🔐 Secrets GitHub à configurer

### Pour GHCR (défaut actuel)
✅ **Aucun secret supplémentaire** - Le token GitHub est automatique !

### Pour Docker Hub
```bash
# Allez sur Settings → Secrets and variables → Actions
# Créez :
DOCKERHUB_USERNAME=your_username
DOCKERHUB_TOKEN=your_access_token
```

### Pour GitLab Registry
```bash
GITLAB_USERNAME=your_username
GITLAB_TOKEN=your_token
```

---

## 📊 Comparaison des Registries

| Feature | GHCR | Docker Hub | GitLab | ECR |
|---------|------|-----------|--------|-----|
| Gratuit | ✅ | ✅ | ✅ | ❌ |
| Intégré GitHub | ✅ | ❌ | ❌ | ❌ |
| Auth auto | ✅ | ❌ | ❌ | ❌ |
| Stockage gratuit | 500MB | Illimité | Illimité | - |
| Simple setup | ✅✅✅ | ✅✅ | ✅ | ❌ |

---

## ✨ Recommandation

**Utilisez GHCR** (configuration par défaut) car :
- ✅ Configuration minimale
- ✅ Authentification automatique
- ✅ Lié à votre repository GitHub
- ✅ Gratuit

Si vous avez beaucoup de storage : **Passez à Docker Hub**

---

## 🔗 Ressources

- [GHCR Documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Docker Login Action](https://github.com/docker/login-action)
- [Docker Build Action](https://github.com/docker/build-push-action)
