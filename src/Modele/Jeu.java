package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Pattern.Observable;
import Structures.Sequence;

public class Jeu extends Observable {

    Plateau plateau;
    int joueurGagnant = 0;
    int joueurCourant;
    int nombreTour; // TODO nombre Tour de jeu
    boolean partieEnCours = false;
    boolean partieTerminee = false;
    public Element dernierTypeDePersonnageJouer;
    public Element personnageManipulerParLeFou;

    // ===============================
    // ===== INFORMATION PLATEAU =====
    // ===============================
    final int TAILLE_DU_PLATEAU = 17;
    final int CENTRE_DU_PLATEAU = 0;
    final int EXTREMITE_GAUCHE_DU_PLATEAU = -8;
    final int EXTREMITE_DROITE_DU_PLATEAU = 8;
    final int ENTREE_CHATEAU_GAUCHE = -6;
    final int ENTREE_CHATEAU_DROIT = 6;

    // ==================
    // ===== JOUEUR =====
    // ==================
    final int JOUEUR_GAUCHE = 0;
    final int JOUEUR_DROIT = 1;

    // ============================
    // ===== VALEURS ELEMENTS =====
    // ============================
    final Element VIDE = Element.VIDE;
    final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    final Element GARDE_DROIT = Element.GARDE_DROIT;
    final Element ROI = Element.ROI;
    final Element FOU = Element.FOU;
    final Element SORCIER = Element.SORCIER;

    // ===========================
    // ===== VALEURS GAGNANT =====
    // ===========================
    final int AUCUN_GAGNANT = 0;
    final int ROI_GAGNANT = 1;
    final int COURONNE_GAGNANTE = 2;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Jeu() {
        plateau = new Plateau();
        changerEtatPartie();
        personnageManipulerParLeFou(FOU);
        initialiserDernierTypeDePersonnageJouer();
        metAJour();
    }

    public Plateau plateau() {
        return plateau;
    }

    // ========================
    // ===== INFOS PARTIE =====
    // ========================
    public boolean estPartieTerminee() {
        return partieTerminee;
    }

    public boolean estPartieEnCours() {
        return partieEnCours;
    }

    public void changerEtatPartie() {
        partieEnCours = !partieEnCours;
    }

    public boolean actionAutoriser() {
        if (!estPartieTerminee() && estPartieEnCours()) {
            return true;
        } else {
            return false;
        }
    }

    // ====================
    // ===== ELEMENTS =====
    // ====================
    public void deplacerElement(Element element, int deplacementElement) {
        if (actionAutoriser()) {
            if (element == Element.COURONNE) {
                deplacerCouronne(deplacementElement);
            } else {
                obtenirPersonnageElement(element).deplacerPersonnage(deplacementElement);
            }
        } else {
            Configuration.instance().logger().info("Partie stopee !!");
        }
        metAJour();
    }

    public boolean validationDeplacement(Element element, int deplacementElement) {
        if (actionAutoriser()) {
            int nouvellePositionElement = obtenirPositionElement(element) + deplacementElement;
            switch (element) {
                    case COURONNE:
                        return true;
                    case GARDE_GAUCHE:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU)
                                && (nouvellePositionElement < obtenirPositionElement(ROI))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case GARDE_DROIT:
                        if ((nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)
                                && (nouvellePositionElement > obtenirPositionElement(ROI))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case ROI:
                        if ((nouvellePositionElement > obtenirPositionElement(GARDE_GAUCHE))
                                && (nouvellePositionElement < obtenirPositionElement(GARDE_DROIT))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case FOU:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU)
                                && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                            Configuration.instance().logger().info("Deplacement " + nouvellePositionElement);
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case SORCIER:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU)
                                && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    default:
                        Configuration.instance().logger().warning("Element " + element.name() + " inconnu !!");
                        return false;
                }
        } 
        else {
            return false;
        }
    }

    public int obtenirPositionElement(Element element) {
        switch (element) {
            case ROI:
                return plateau.roi.positionPersonnage();
            case FOU:
                return plateau.fou.positionPersonnage();
            case SORCIER:
                return plateau.sorcier.positionPersonnage();
            case GARDE_GAUCHE:
                return plateau.gardeGauche.positionPersonnage();
            case GARDE_DROIT:
                return plateau.gardeDroit.positionPersonnage();
            default:
                return 10;
        }
    }

    public Personnage obtenirPersonnageElement(Element element) {
        switch (element) {
            case ROI:
                return plateau.roi;
            case FOU:
                return plateau.fou;
            case SORCIER:
                return plateau.sorcier;
            case GARDE_GAUCHE:
                return plateau.gardeGauche;
            case GARDE_DROIT:
                return plateau.gardeDroit;
            default:
                return null;
        }
    }

    private void echangerFouSorcier() {
        plateau.echangerFouSorcier();
    }

    // ==================
    // ===== JOUEUR =====
    // ==================
    public void choixPremierJoueur(int premierJoueur) {
        if (numeroJoueurValide(premierJoueur)) {
            if (premierJoueur != plateau().joueurCourant) {
                echangerFouSorcier();
                changerJoueurCourant();
            }
        }
        metAJour();
    }

    public boolean numeroJoueurValide(int numeroJoueurVoulu) {
        return plateau.numeroJoueurValide(numeroJoueurVoulu);
    }

    public void changerJoueurCourant() {
        if (actionAutoriser()) {
            plateau.changerJoueurCourant();
        }
        metAJour();
    }

    public int joueurCourant() {
        return plateau.joueurCourant;
    }

    // TODO IA
    // Numero : 0 = Joueur Gauche | 1 = Joueur Droit
    // Type : 0 = Joueur Humain | 1 = Joueur IA
    public void changerJoueurCourant(int numeroJoueur, int typeJoueur) {
        metAJour();
    }

    // ===================
    // ===== GAGNANT =====
    // ===================
    public boolean estGagnant() {
        return plateau.estGagnant() != AUCUN_GAGNANT;
    }

    public void traiterGagnant() {
        switch (plateau.estGagnant()) {
            case COURONNE_GAGNANTE:
                partieTerminee = true;
                partieEnCours = false;
                joueurGagnant = plateau().joueurGagnant;
                Configuration.instance().logger().info("Victoire du joueur " + joueurGagnant + " avec la couronne !!");
                break;
            case ROI_GAGNANT:
                partieTerminee = true;
                partieEnCours = false;
                joueurGagnant = plateau().joueurGagnant;
                Configuration.instance().logger().info("Victoire du joueur " + joueurGagnant + " avec le roi !!");
                break;
            default:
                Configuration.instance().logger().warning("Condition de victoire inconnue !!");
                break;
        }
        metAJour();
    }

    // =======================
    // ===== FIN DE TOUR =====
    // =======================
    public void finDeTour() {
        deplacerCouronne(plateau.valeurDeplacementCouronne());
        if (estGagnant()) {
            traiterGagnant();
        } else {
            plateau.paquet.viderCartePoser();
            if (plateau.paquet
                    .resteAssezCarteDansPioche(plateau.paquet.nombreCarteManquante(plateau().joueurCourant))) {
                plateau.paquet.remplirMain(plateau().joueurCourant);
                changerJoueurCourant();
            } else {
                if (getEtatCouronne()) {
                    plateau.paquet.melangerDefausse();
                    plateau.paquet.remplirMain(plateau().joueurCourant);
                    changerEtatCouronne();
                    changerJoueurCourant();
                } else {
                    if (obtenirPositionElement(ROI) == 0) {
                        plateau.paquet.melangerDefausse();
                        plateau.paquet.remplirMain(plateau().joueurCourant);
                        changerJoueurCourant();
                    } else {
                        if (obtenirPositionElement(ROI) > 0) {
                            joueurGagnant = JOUEUR_DROIT;
                        } else {
                            joueurGagnant = JOUEUR_GAUCHE;
                        }
                        traiterGagnant();
                    }
                }
            }
        }
        personnageManipulerParLeFou(FOU);
        initialiserDernierTypeDePersonnageJouer();
        metAJour();
    }

    // ====================
    // ===== COURONNE =====
    // ====================
    public void deplacerCouronne(int deplacementCouronne) {
        plateau.deplacerCouronne(deplacementCouronne);
        metAJour();
    }

    public void changerEtatCouronne() {
        if (actionAutoriser()) {
            plateau.changerEtatCouronne();
        }
        metAJour();
    }

    // =======================
    // ===== JOUER CARTE =====
    // =======================
    public void jouerCarte(Element element, int positionArriveeElement, int carteJouer) {
        if (actionAutoriser()) {
            poserCarte(carteJouer);
            int deplacementElement = obtenirPositionElement(element) - positionArriveeElement;
            deplacerElement(element, -deplacementElement);
        }
        metAJour();
    }

    public void jouerSequenceCarte(Sequence<Element> elements, int[] positionsArriveeElements, int[] cartesJouer) {
        if (actionAutoriser()) {
            int i = 0;
            while (i != cartesJouer.length) {
                poserCarte(cartesJouer[i]);
                i++;
            }
            i = 0;
            while (!elements.estVide()) {
                Element elementExtrait = elements.extraitTete();
                int deplacementElementExtrait = obtenirPositionElement(elementExtrait) - positionsArriveeElements[i];
                deplacerElement(elementExtrait, -deplacementElementExtrait);
                i++;
            }
        }
        metAJour();
    }

    public void poserCarte(int positionCarteDansLaMain) {
        majDernierTypeDePersonnageJouer(recupererMainJoueur(plateau().joueurCourant)[positionCarteDansLaMain].personnage());
        plateau.paquet.enleverCarte(plateau().joueurCourant, positionCarteDansLaMain);
    }

    public Carte[] recupererMainJoueur(int joueur) {
        if (actionAutoriser()) {
            if (numeroJoueurValide(joueur)) {
                return plateau.paquet.mainJoueur(joueur);
            } else {
                Configuration.instance().logger().warning("Numero de joueur incorect !!");
                return null;
            }
        } else {
            Configuration.instance().logger().warning("Partie stopee !!");
            return null;
        }
    }

    public void initialiserDernierTypeDePersonnageJouer() {
        dernierTypeDePersonnageJouer = VIDE;
    }

    public void majDernierTypeDePersonnageJouer(Element perso) {
        dernierTypeDePersonnageJouer = perso;
    }

    public int[] listeCarteJouable() {
        int nombreCartes = plateau.paquet.nombreCartesEnMain();
        int indice = 0;
        int[] resultat;
        if (dernierTypeDePersonnageJouer == VIDE) {
            resultat = initialiserTableau(nombreCartes, 1);
        } else {
            resultat = new int[nombreCartes];
            while (indice < nombreCartes) {
                Carte carte = plateau.paquet.mainJoueur(plateau().joueurCourant)[indice];
                if (carte.personnage() == dernierTypeDePersonnageJouer) {
                    resultat[indice] = 1;
                }
                indice++;
            }
        }
        return resultat;
    }

    public int[] initialiserTableau(int taille, int valeurDefaut) {
        int[] tableau = new int[taille];
        int i = 0;
        while (i < taille) {
            tableau[i] = valeurDefaut;
            i++;
        }
        return tableau;
    }

    public int positionPlus8(int positionElement) {
        return positionElement + EXTREMITE_DROITE_DU_PLATEAU;
    }

    public int positionsPourCour() {
        if (obtenirPositionElement(GARDE_DROIT) == EXTREMITE_DROITE_DU_PLATEAU) {
            return 1;
        } else {
            if (obtenirPositionElement(GARDE_GAUCHE) == EXTREMITE_GAUCHE_DU_PLATEAU) {
                return 2;
            }
            return 0;
        }
    }

    public int[] listeDeplacementPossiblesAvecCarte(Element perso, Deplacement deplace) {
        int[] positionAccessibleAvecCarte = initialiserTableau(TAILLE_DU_PLATEAU, 0);
        int deplacementCarte = deplace.getValeurDeplacement();
        switch (perso) {
            case GARDE_GAUCHE:
                switch (deplace) {
                    case UN:
                        if (validationDeplacement(GARDE_GAUCHE, deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE))
                                    + deplacementCarte] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE))
                                    - deplacementCarte] = 1;
                        }
                        break;
                    case UN_PLUS_UN:
                        if (validationDeplacement(GARDE_GAUCHE, 1) && validationDeplacement(GARDE_DROIT, 1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + 1] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -1) && validationDeplacement(GARDE_DROIT, -1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - 1] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, 2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + 2] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - 2] = 1;
                        }
                        break;
                    case RAPPROCHE:
                        positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(ROI)) - 1] = 1;
                        break;
                    default:
                        break;
                }
                break;
            case GARDE_DROIT:
                switch (deplace) {
                    case UN:
                        if (validationDeplacement(GARDE_DROIT, deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                    + deplacementCarte] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, -deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                    - deplacementCarte] = 1;
                        }
                        break;
                    case UN_PLUS_UN:
                        if (validationDeplacement(GARDE_GAUCHE, 1) && validationDeplacement(GARDE_DROIT, 1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + 1] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -1) && validationDeplacement(GARDE_DROIT, -1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - 1] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, 2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + 2] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, -2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - 2] = 1;
                        }
                        break;
                    case RAPPROCHE:
                        positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(ROI)) + 1] = 1;
                        break;
                    default:
                        break;
                }
                break;

            case ROI:
                if (validationDeplacement(ROI, deplacementCarte)) {
                    positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(ROI)) + deplacementCarte] = 1;
                }
                if (validationDeplacement(ROI, -deplacementCarte)) {
                    positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(ROI)) - deplacementCarte] = 1;
                }
                break;
            case SORCIER:
                if (validationDeplacement(SORCIER, deplacementCarte)) {
                    positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(SORCIER)) + deplacementCarte] = 1;
                }
                if (validationDeplacement(SORCIER, -deplacementCarte)) {
                    positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(SORCIER)) - deplacementCarte] = 1;
                }
                break;
            case FOU:
                switch (personnageManipulerParLeFou) {
                    case GARDE_GAUCHE:
                        if (deplace == Deplacement.MILIEU) {
                            if (validationDeplacement(GARDE_GAUCHE, -obtenirPositionElement(GARDE_GAUCHE))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, -obtenirPositionElement(GARDE_DROIT))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                        } else {
                            if (validationDeplacement(GARDE_GAUCHE, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE))
                                        + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_GAUCHE, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE))
                                        - deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                        + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                        - deplacementCarte] = 1;
                            }
                        }
                        break;
                    case GARDE_DROIT:
                        if (deplace == Deplacement.MILIEU) {
                            if (validationDeplacement(GARDE_DROIT, -obtenirPositionElement(GARDE_DROIT))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                        } else {
                            if (validationDeplacement(GARDE_DROIT, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                        + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT))
                                        - deplacementCarte] = 1;
                            }
                        }
                        break;
                    case ROI:
                    case FOU:
                    case SORCIER:
                        if (deplace == Deplacement.MILIEU) {
                            if (validationDeplacement(personnageManipulerParLeFou,
                                    -obtenirPositionElement(personnageManipulerParLeFou))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                        } else {
                            if (validationDeplacement(personnageManipulerParLeFou, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(
                                        obtenirPositionElement(personnageManipulerParLeFou)) + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(personnageManipulerParLeFou, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(
                                        obtenirPositionElement(personnageManipulerParLeFou)) - deplacementCarte] = 1;
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return positionAccessibleAvecCarte;
    }

    // =======================
    // ===== POUVOIR FOU =====
    // =======================
    public void personnageManipulerParLeFou(Element personnage) {
        if (actionAutoriser()) {
            personnageManipulerParLeFou = personnage;
        }
        metAJour();
    }

    public boolean estPouvoirFouActivable() {
        if (actionAutoriser()) {
            int positionFou = obtenirPositionElement(FOU);
            int positionRoi = obtenirPositionElement(ROI);

            if (plateau().joueurCourant == JOUEUR_DROIT) {
                if (positionFou > positionRoi) {
                    metAJour();
                    return true;
                } else {
                    Configuration.instance().logger().info("Pouvoir du Fou inutilisable pour le joueur de droite !!");
                    metAJour();
                    return false;
                }
            } else {
                if (positionFou < positionRoi) {
                    metAJour();
                    return true;
                } else {
                    Configuration.instance().logger().info("Pouvoir du Fou inutilisable pour le joueur de gauche !!");
                    metAJour();
                    return false;
                }
            }

        } else {
            metAJour();
            return false;
        }
    }

    // ===========================
    // ===== POUVOIR SORCIER =====
    // ===========================
    public void activerPouvoirSorcier(Element element, boolean teleportationElement) {
        if (teleportationElement) {
            teleportationPouvoirSorcier(element);
        } else {
            estPouvoirSorcierActivable(element);
        }
    }

    public void teleportationPouvoirSorcier(Element element) {
        int distanceTeleportation = calculerTeleportation(element);
        if (estTeleportationValide(element, distanceTeleportation)) {
            switch (element) {
                case ROI:
                    plateau.roi.deplacerPersonnage(distanceTeleportation);
                    break;

                case GARDE_GAUCHE:
                    plateau.gardeGauche.deplacerPersonnage(distanceTeleportation);
                    break;

                case GARDE_DROIT:
                    plateau.gardeDroit.deplacerPersonnage(distanceTeleportation);
                    break;

                default:
                    break;
            }
        } else {
            Configuration.instance().logger().info("Teleportation " + element.name() + " impossible !!");
        }
        metAJour();
    }

    public boolean estPouvoirSorcierActivable(Element element) {
        int distanceTeleportation = calculerTeleportation(element);
        if (estTeleportationValide(element, distanceTeleportation)) {
            metAJour();
            return true;
        } else {
            Configuration.instance().logger().info("Teleportation " + element.name() + " impossible !!");
            metAJour();
            return false;
        }
    }

    public boolean estTeleportationValide(Element element, int teleporter) {
        return validationDeplacement(element, teleporter);
    }

    public int calculerTeleportation(Element element) {
        int teleporter = 0;
        int positionSorcier = obtenirPositionElement(SORCIER);
        int positionElement = obtenirPositionElement(element);
        teleporter = positionSorcier - positionElement;
        return teleporter;
    }

    // ================
    // ===== TEST =====
    // ================
    public int getPositionCouronne() {
        return plateau().couronne.positionCouronne();
    }

    public boolean getEtatCouronne() {
        return plateau().couronne.etatCouronne();
    }

}
