### Projet Visite Royale Prog 6

## Membres du groupe

BALSSA Raphaël
CASAGRANDE Dorian
FAURE-JEUNOT Paul
FAURIE Alban
GLEMBA Adrien
LAMBERT Mathis

## Visite Royale

TODO - Descriptif du jeu

### Structure de l'espace de travail

L'espace de travail contient trois dossiers :

- `src` : le dossier pour maintenir le code source.
- `res` : le dossier pour maintenir les ressources.
- `lib` : le dossier pour maintenir les dépendances.

Les fichiers de sortie compilés seront générés dans le dossier `bin`.

Le fichier `.gitignore` permet de ne pas polluer le dépôt Git.

### Journal de bord

----------------------
    JOUR 1 (03/05)
----------------------

Nous avons choisi notre jeu parmi la sélection, et nous avons eu notre premier choix, Visite Royale.
Directement après la sélection du jeu, nous sommes allés acheter une version physique pour pouvoir comprendre plus facilement les règles et pouvoir scanner les cartes et le plateau pour l'aspect graphique du jeu.

----------------------
    JOUR 2 (04/05)
----------------------

Nous avons passé la matinée à jouer au jeu pour apprendre les règles, les différentes stratégies et ainsi en cerner les subtilités. Nous avons d'ailleurs eu un différend sur une règle spécifique, qui consiste a utiliser le pouvoir du Fou sur le Roi, même si le déplacement dépasse le Fou.
L'après-midi, nous avons commencé à réfléchir aux structures de données que nous allons utiliser pour représenter les personnages et les cartes. 
Nous avons ensuite commencé un diagramme de classes.

----------------------
    JOUR 3 (05/05)
----------------------

Paul, Alban, Adrien et Mathis :
Nous avons réfléchi à la mise en place de l'IA, et à comment évaluer un plateau en termes de situations avantageuses/désavantageuses. 
Pour ce faire, nous avons réalisé des calculs de probabilité et de moyenne, par exemple pour évaluer de combien un personnage pouvait se déplacer de cases avec 1 carte en moyenne.
Nous avons donc créé un système de notation selon la position des personnage sur le plateau ainsi que sur la position de la couronne. Nous avons testé de nombreuses configurations de plateau pour savoir si le poids de certains personnages était trop élevé par rapport aux autres (notamment le Fou dans notre cas), et il a fallu rééquilibrer certains coefficients multiplicateurs pour équilibrer l'évaluation par rapport a notre propre évaluation du plateau.
Un score élevé représente donc une situation dangereuse pour le joueur concerné par cette évaluation.

Dorian et Raphaël : 
Nous avons commencé à regarder comment organiser l'IHM du jeu en nous inspirant de plusieurs jeux de cartes en ligne auxquels nous avons joué.
Après plusieurs discussions avec le reste du groupe nous avons divisé le jeu en plusieurs fenêtres, posé les bases du plateau et commencé un prototype en papier.

----------------------
    JOUR 4 (06/05)
----------------------


Paul, Alban, Adrien et Mathis :
Nous avons continué notre évaluation de plateau. Nous avons décidé de produire un programme afin de tester plus rapidement des configurations au lieu de tout faire à la main. De nombreux bugs ont été trouvés et résolus. Au fur et à mesure la formule totale s'affine et devient plus proche de notre perception.

Dorian et Raphaël : 
La version papier est quasiment finie est présentable à l'audition. Nous nous sommes renseignés sur la manière d'implémenter des animations sous Java, comment interagissent certains éléments entre eux (trier les cartes automatiquement ou pas, affichage des pouvoirs magicien/fou, plateau, affichage des tours/joueurs)

----------------------
    JOUR 5 (09/05)
----------------------

Le diagramme de classe version 1 a été terminé pendant les vacances. 
Plusieurs documents ont été produits pour expliquer le fonctionnement de l'IA
La version papier de l'IHM est finie

Résultat de l'audit IHM : 
De nombreuses pistes et changements ont été trouvés. Le point de vue extérieur du jury nous a permis de prendre du recul sur le jeu, et de voir l'avis de personnes inexpérimentées dans le domaine du jeu vidéo.

Résultat de l'audit IA : 
Malgré l'avancement du programme, les documents et les preuves, la présentation a été assez mitigée. Nous avons compris le message suivant :
Pas de paramètres objectifs, programme intéressant mais pas dans la bonne voie. 
Plusieurs choix proposés, théorème des jeux à somme nulle, laisser le choix des paramètres lors de chaque configuration (customiser les coefficients)

Nous avons donc passé le reste de la journée à nous concerter, déterminer comment modifier l'IA et disposer l'IHM en suivant les conseils de l'audit.
Découverte de Figma, création d'un petit prototype graphique en plus du modèle papier.

----------------------
    JOUR 6 (10/05)
----------------------

Paul, Alban, Adrien et Mathis :
Pause du côté de l'IA, suivi du diagramme de classe réalisé auparavant et développement du squelette du projet en respectant le modèle du préprojet.
Réutilisation du code, développement d'un plateau sur le terminal, gestion de l'aléatoire et de la pioche.
L'objectif de la journée a été principalement d'avoir un projet qui compile avec un maximum de structures et fonctionnalités disponibles.

Dorian et Raphaël : 
Création d'un menu de jeu basique, implémentation de fenêtres en Swing dans le projet en respectant la structure du projet.
Travail en cours sur les images en fond de fenêtre, architecture des boutons, aspect graphique des éléments et développement de la fenêtre des options en accord avec le prototype produit précédemment.

----------------------
    JOUR 7 (11/05)
----------------------

Nous avons continué de progression sur le codage du plateau et du paquet de cartes, ainsi que sur la future imbrication des deux parties.
Mise en place de fichiers de test pour vérifier le bon fonctionnement des grosses fonctions implémentées.
Scan de tous les éléments du jeu original afin d'avoir une base solide que l'on peut reprendre pour le côté graphique

Rendez-Vous avec le tuteur : Explication et validation de l'idée de la somme nulle pour le calcul de l'évaluation du plateau.
Mise en place d'un calendrier pour avoir des objectifs réguliers pour progresser.
Les futurs rendez-vous sont définis les mardi après-midi et les vendredi matin pour qu'ils soient réguliers.

----------------------
    JOUR 8 (12/05)
----------------------

Dans l'optique de se préparer pour l'audit de code du 13/05, nous avons essayé d'avoir une version présentable.
Cependant, nous nous sommes aperçus d'un problème de compatibilité de classe, notamment dû au fait qu'une classe ne peut pas avoir deux extends.
Il a donc été nécessaire de créer une classe Jeu pour tout centraliser, et a donc nécessité une grande partie de la journée.
Mise en place d'une version textuelle sur le terminal pour avoir une version "jouable". Nettoyage du code et de certaines fonctions pour rendre le tout plus compact.

----------------------
    JOUR 9 (13/05)
----------------------

L'audit de code a soulevé un problème de gestion des priorités. L'urgence est désormais d'intégrer l'interface graphique avec le fond du jeu pour pouvoir faire des tests plus facilement que sur la simple interface textuelle.
Le rendez-vous avec le tuteur a conforté cette idée. Nous n'avons pas de retard a proprement parlé, mais nous avons mal géré nos priorités pour le moment, et il faut corriger cela.
Le reste de la journée a donc été axé sur cette intégration. Malgré un bug incompréhensible (affichage marchant sur un pc et pas sur l'autre), nous avons commencé à a avancer dans ce sens-là 

----------------------
    JOUR 10 (16/05)
----------------------

L'interface Graphique a pris une bonne forme et on commence à pouvoir bouger les jetons sur le plateau, tandis que l'interface textuelle est définitivement terminée.
L'intégration se poursuit et nous décidons de reprendre les cartes existantes et le plateau en les épurant et en ne gardant que les informations essentielles pour un maximum de lisibilité.
L'IA aléatoire est également en bonne voie, il suffit juste de pouvoir la rattacher a l'interface graphique après le moteur de jeu. 

----------------------
    JOUR 11 (17/05)
----------------------

L'intégration reste la priorité, avec en ligne de mire la possibilité de faire jouer une IA aléatoire sur le plateau graphique, tout en construisant le plateau graphique.
Le rendez-vous avec le tuteur nous permet de nous dire que nous ne sommes pas trop en retard.
L'historique doit être traité dans la mesure du possible.
A la fin de la journée, l'implémentation de l'IA aléatoire sur le plateau est réussie, même si on ne voit pas précisément ses mouvements.

----------------------
    JOUR 12 (18/05)
----------------------
Mise en place de davantage de feedback (prévisualisation du coup réalisable à partir d'une carte avec des colonnes en surbrillance)
Les bugs présents pour l'IA aléatoire sont corrigés, notamment la fin de partie, et permettent de passer à une version améliorée sous peu
Côté IHM, épuration du plateau de jeu qui paraissait trop chargé et mise en place d'un masquage gris pour les cartes non jouables

----------------------
    JOUR 13 (19/05)
----------------------
Début du travail autour d'une nouvelle IA, qui prendrai un coup aléatoire mais qui ne le jouerai que si la valeur de l'évaluation du plateau est supérieure à la valeur du plateau actuel.
Recherche de thème sonore pour le jeu, que ce soit pour l'écran d'accueil ou pour la phase de jeu.
Les différents écrans de jeu sont reliés (écran d'accueil, options, ...)

----------------------
    JOUR 14 (20/05)
----------------------
Après le rendez-vous avec le tuteur, la priorité se pose clairement sur l'IA intelligente (arbre MinMax) ainsi que sur l'historique et sur la sauvegarde d'une partie.
