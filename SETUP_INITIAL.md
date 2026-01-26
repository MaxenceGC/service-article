# SETUP INITIAL - À exécuter UNE SEULE FOIS

## Étape 1: Se connecter au cluster

```bash
ssh maxence@192.168.1.31
cd article-service
```

## Étape 2: Créer le namespace

```bash
kubectl create namespace production
```

## Étape 3: Créer le Secret pour GitLab Registry

```bash
kubectl create secret docker-registry registry-credentials \
  --docker-server=registry.gitlab.com \
  --docker-username="oauth2:git_read_registry" \
  --docker-password="VOTRE_GITLAB_TOKEN" \
  -n production
```

## Étape 4: Créer le Secret pour la Base de Données

```bash
kubectl create secret generic article-service-secrets \
  --from-literal=db.username="article_user" \
  --from-literal=db.password="VOTRE_MDP_BD" \
  -n production
```

## Étape 5: Créer le ConfigMap

```bash
kubectl create configmap article-service-config \
  --from-literal=datasource.jdbc.url="jdbc:postgresql://VOTRE_HOST_BD:5432/article_db" \
  -n production
```

## Étape 6: Appliquer les permissions RBAC

```bash
kubectl apply -f k8s/rbac-namespace.yaml
kubectl apply -f k8s/rbac-gitlab-agent.yaml
```

## Étape 7: Appliquer le déploiement initial

```bash
kubectl apply -f k8s/deployment.yaml -n production
kubectl apply -f k8s/service.yaml -n production
kubectl apply -f k8s/ingress.yaml -n production
```

## Étape 8: Vérifier

```bash
kubectl get pods -n production -l app=article-service -w
kubectl logs -n production -l app=article-service -f
```

Ensuite, le pipeline GitLab mettra à jour simplement l'image Docker quand vous pushez du code.
