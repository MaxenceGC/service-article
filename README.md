

## Endpoints

Toutes les routes consomment et produisent du JSON (`Content-Type: application/json`).

### Articles

- `POST /articles` : creer un article.
- `GET /articles/{id}` : recuperer un article par id.
- `PUT /articles/{id}` : mettre a jour un article.
- `GET /articles?categorieId={uuid}&vendeurId={uuid}&statut={statut}` : lister les articles, avec filtres optionnels.
- `PATCH /articles/{id}/prix` : modifier le prix d'un article.
- `PATCH /articles/{id}/valider` : valider un article.
- `PATCH /articles/{id}/refuser` : refuser un article avec un motif.
- `DELETE /articles/{id}` : supprimer un article.

### Photos d'articles

- `POST /articles/{articleId}/photos` : ajouter une photo a un article.
- `GET /articles/{articleId}/photos` : lister les photos d'un article.
- `DELETE /articles/{articleId}/photos/{photoId}` : supprimer une photo d'article.
- `PATCH /articles/{articleId}/photos/{photoId}/ordre?ordre={int}` : modifier l'ordre d'affichage.

### Signalements d'articles

- `POST /articles/{articleId}/signalements` : signaler un article.

### Boutiques

- `POST /boutiques` : creer une boutique.
- `GET /boutiques?vendeurId={uuid}` : lister les boutiques, filtre optionnel par vendeur.
- `GET /boutiques/{id}` : recuperer une boutique.
- `PUT /boutiques/{id}` : mettre a jour une boutique.
- `DELETE /boutiques/{id}` : supprimer une boutique.

### Categories

- `POST /categories` : creer une categorie.
- `GET /categories?parentId={uuid}` : lister les categories, filtre optionnel par parent.
- `GET /categories/{id}` : recuperer une categorie.
- `GET /categories/{id}/children` : lister les enfants directs d'une categorie.
- `GET /categories/tree` : obtenir l'arbre complet des categories.
- `PUT /categories/{id}` : mettre a jour une categorie (parent_id ne peut pas etre egal a l'id).
- `DELETE /categories/{id}` : supprimer une categorie.

## Running the application in dev mode service-article

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/service-article-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
