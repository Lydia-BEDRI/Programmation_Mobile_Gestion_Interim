## Présentation du Projet

Le projet de Programmation Mobile (HAI811I) consiste à développer une application mobile dédiée à la gestion des offres et des candidatures pour le travail intérimaire.

### Utilisateurs Gérés

1. **Utilisateurs Anonymes** :
   - Affichage et recherche d'annonces/offres d'emploi.
   - Possibilité de s'inscrire ou de s'authentifier.
   
2. **Candidats Inscrits/Connectés** :
   - Candidature pour une offre.
   - Gestion des candidatures (en cours, acceptées).
   - Consultation et gestion des candidatures.
   
3. **Employeurs** :
   - Dépôt d'offres d'emploi.
   - Gestion des offres déposées.
   - Gestion des candidatures (en attente, acceptées).

## Installation
Pour installer et exécuter l'application, suivez ces étapes :

1. Clonez ce dépôt GitHub sur votre machine locale :

`git clone https://github.com/Lydia-BEDRI/Programmation_Mobile_Gestion_Interim.git`

2. Ouvrez le projet dans Android Studio.

3. Connectez votre appareil Android ou lancez un émulateur.

4. Lier le projet avec Firebase :
- Créez un projet sur [Firebase Console](https://console.firebase.google.com/).
- Ajoutez votre application Android à votre projet Firebase en suivant les instructions de configuration.
- Téléchargez le fichier de configuration `google-services.json` et placez-le dans le répertoire `app` de votre projet Android.
- Ajoutez les dépendances Firebase nécessaires dans le fichier `build.gradle` du module `app`.

5. Compilez et exécutez l'application sur votre appareil ou émulateur.

## Hiérarchie des fichiers (sous `app/src`)
- **Activities**: Contient les activités principales de l'application.
- **Helpers**: Contient les classes utilitaires pour aider dans le développement.
- **Fragments**: Contient les fragments utilisés dans l'interface utilisateur.
- **Models**: Contient les modèles de données de l'application.
- **Adapters**: Contient les adaptateurs utilisés pour lier les données aux vues dans les listes ou les grilles.

## Contenu du Répertoire

- **app/Maquettes/** : Ce répertoire contient toutes les maquettes de l'application au format SVG.
- **Rapports/** : Contient les trois versions des rapports du projet à remettre.
