package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Pattern.Observable;
import Structures.Sequence;

public class Jeu extends Observable {

    static Plateau plateau;
    static int joueurGagnant = 0;
    public static int joueurCourant;
    int nombreTour; // TODO nombre Tour de jeu
    static boolean partieEnCours = false;
    static boolean partieTerminee = false;
    static Element dernierTypeDePersonnageJouer;
    public static Element personnageManipulerParLeFou;

    // ===============================
    // ===== INFORMATION PLATEAU =====
    // ===============================
    static final int TAILLE_DU_PLATEAU = 17;
    static final int CENTRE_DU_PLATEAU = 0;
    static final int EXTREMITE_GAUCHE_DU_PLATEAU = -8;
    static final int EXTREMITE_DROITE_DU_PLATEAU = 8;
    static final int ENTREE_CHATEAU_GAUCHE = -6;
    static final int ENTREE_CHATEAU_DROIT = 6;

    // ==================
    // ===== JOUEUR =====
    // ==================
    static final int JOUEUR_GAUCHE = 0;
    static final int JOUEUR_DROIT = 1;

    // ============================
    // ===== VALEURS ELEMENTS =====
    // ============================
    static final Element VIDE = Element.VIDE;
    static final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    static final Element GARDE_DROIT = Element.GARDE_DROIT;
    static final Element ROI = Element.ROI;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;

    // ===========================
    // ===== VALEURS GAGNANT =====
    // ===========================
    static final int AUCUN_GAGNANT = 0;
    static final int ROI_GAGNANT = 1;
    static final int COURONNE_GAGNANTE = 2;

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

    public static Plateau plateau() {
        return plateau;
    }

    // ========================
    // ===== INFOS PARTIE =====
    // ========================
    public static boolean estPartieTerminee() {
        return partieTerminee;
    }

    public static boolean estPartieEnCours() {
        return partieEnCours;
    }

    public void changerEtatPartie() {
        partieEnCours = !partieEnCours;
    }

    public static boolean actionAutoriser() {
        if (!estPartieTerminee() && estPartieEnCours()) {
            return true;
        } else {
            return false;
        }
    }

    // ====================
    // ===== ELEMENTS =====
    // ====================
    public static void deplacerElement(Element element, int deplacementElement) {
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

    public static boolean validationDeplacement(Element element, int deplacementElement) {
        if (actionAutoriser()) {
            int nouvellePositionElement = obtenirPositionElement(element) + deplacementElement;
            if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU) && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                switch (element) {
                    case COURONNE:
                        return true;
                    case GARDE_GAUCHE:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU) && (nouvellePositionElement < obtenirPositionElement(ROI))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case GARDE_DROIT:
                        if ((nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU) && (nouvellePositionElement > obtenirPositionElement(ROI))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case ROI:
                        if ((nouvellePositionElement > obtenirPositionElement(GARDE_GAUCHE)) && (nouvellePositionElement < obtenirPositionElement(GARDE_DROIT))) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case FOU:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU) && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                            Configuration.instance().logger().info("Deplacement " + nouvellePositionElement);
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    case SORCIER:
                        if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU) && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                            return true;
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                            return false;
                        }
                    default:
                        Configuration.instance().logger().warning("Element " + element.name() + " inconnu !!");
                        return false;
                }
            } else {
                Configuration.instance().logger().warning("Valeur impedictible cas hors plateau !!");
                return false;
            }
            
        } else {
            return false;
        }
    }

    public static int obtenirPositionElement(Element element) {
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

    public static Personnage obtenirPersonnageElement(Element element) {
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

    public static boolean numeroJoueurValide(int numeroJoueurVoulu) {
        return plateau.numeroJoueurValide(numeroJoueurVoulu);
    }

    public static void changerJoueurCourant() {
        if (actionAutoriser()) {
            plateau.changerJoueurCourant();
        }
        metAJour();
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
    public static boolean estGagnant() {
        return plateau.estGagnant() != AUCUN_GAGNANT;
    }

    public static void traiterGagnant() {
        switch (plateau.estGagnant()) {
            case COURONNE_GAGNANTE:
                partieTerminee = true;
                partieEnCours = false;
                joueurGagnant = plateau().joueurCourant;
                Configuration.instance().logger().info("Victoire du joueur " + joueurGagnant + " avec la couronne !!");
                break;
            case ROI_GAGNANT:
                partieTerminee = true;
                partieEnCours = false;
                joueurGagnant = plateau().joueurCourant;
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
    public static void finDeTour() {
        deplacerCouronne(plateau.valeurDeplacementCouronne());
        if (estGagnant()) {
            traiterGagnant();
        } else {
            plateau.paquet.viderCartePoser();
            if(plateau.paquet.resteAssezCarteDansPioche(plateau.paquet.nombreCarteManquante(plateau().joueurCourant))){
                plateau.paquet.remplirMain(plateau().joueurCourant);
                changerJoueurCourant();
            } else {
                if(getEtatCouronne()) {
                    plateau.paquet.melangerDefausse();
                    plateau.paquet.remplirMain(plateau().joueurCourant);
                    changerEtatCouronne();
                    changerJoueurCourant();
                } else {
                    if(obtenirPositionElement(ROI) == 0) {
                        plateau.paquet.melangerDefausse();
                        plateau.paquet.remplirMain(plateau().joueurCourant);
                        changerJoueurCourant();
                    } else {
                        if(obtenirPositionElement(ROI) > 0) {
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
    public static void deplacerCouronne(int deplacementCouronne) {
        plateau.deplacerCouronne(deplacementCouronne);
        metAJour();
    }

    public static void changerEtatCouronne() {
        if (actionAutoriser()) {
            plateau.changerEtatCouronne();
        }
        metAJour();
    }

    // =======================
    // ===== JOUER CARTE =====
    // =======================
    public static void jouerCarte(Element element, int positionArriveeElement, int carteJouer) {
        if (actionAutoriser()) {
            poserCarte(carteJouer);
            int deplacementElement = obtenirPositionElement(element) - positionArriveeElement;
            deplacerElement(element, -deplacementElement);
        }
        metAJour();
    }

    public static void jouerSequenceCarte(Sequence<Element> elements, int[] positionsArriveeElements, int[] cartesJouer) {
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
                deplacerElement(elementExtrait, deplacementElementExtrait);
                i++;
            }
        }
        metAJour();
    }

    public static void poserCarte(int positionCarteDansLaMain) {
        majDernierTypeDePersonnageJouer(positionCarteDansLaMain);
        plateau.paquet.enleverCarte(plateau().joueurCourant, positionCarteDansLaMain);
    }

    public static Carte[] recupererMainJoueur(int joueur) {
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

    public static void initialiserDernierTypeDePersonnageJouer() {
        dernierTypeDePersonnageJouer = VIDE;
    }

    public static void majDernierTypeDePersonnageJouer(int positionCarteDansLaMain) {
        dernierTypeDePersonnageJouer = recupererMainJoueur(plateau().joueurCourant)[positionCarteDansLaMain].personnage();
    }

    public static int[] listeCarteJouable() {
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
                indice ++;
            }
        }
        return resultat;
    }

    public static int[] initialiserTableau(int taille, int valeurDefaut) {
        int[] tableau = new int[taille];
        int i = 0;
        while (i < taille) {
            tableau[i] = valeurDefaut;
            i ++;
        }
        return tableau;
    }

    public static int positionPlus8(int positionElement) {
        return positionElement + EXTREMITE_DROITE_DU_PLATEAU;
    }

    public static int positionsPourCour(){
        if(obtenirPositionElement(GARDE_DROIT) == EXTREMITE_DROITE_DU_PLATEAU){
            return 1;
        }
        else{
            if(obtenirPositionElement(GARDE_GAUCHE) == EXTREMITE_GAUCHE_DU_PLATEAU){
                return 2;
            }
            return 0;
        }
    }

    public static int[] listeDeplacementPossiblesAvecCarte(int position) {
        int[] positionAccessibleAvecCarte = initialiserTableau(TAILLE_DU_PLATEAU, 0);
        Carte carte = recupererMainJoueur(plateau().joueurCourant)[position];
        int deplacementCarte = carte.deplacement().getValeurDeplacement();
        System.out.println(deplacementCarte);
        switch (carte.personnage()) {
            case GARDE_GAUCHE:
                switch (carte.deplacement()) {
                    case UN:
                        if (validationDeplacement(GARDE_GAUCHE, deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + deplacementCarte] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - deplacementCarte] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + deplacementCarte] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, -deplacementCarte)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - deplacementCarte] = 1;
                        }
                        break;
                    case UN_PLUS_UN:
                        if (validationDeplacement(GARDE_GAUCHE, 1) && validationDeplacement(GARDE_DROIT, 1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + 1] = 1;
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + 1] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -1) && validationDeplacement(GARDE_DROIT, -1)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - 1] = 1;
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - 1] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, 2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + 2] = 1;
                        }
                        if (validationDeplacement(GARDE_GAUCHE, -2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - 2] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, 2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + 2] = 1;
                        }
                        if (validationDeplacement(GARDE_DROIT, -2)) {
                            positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - 2] = 1;
                        }
                        break;
                    case RAPPROCHE:
                        positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(ROI)) - 1] = 1;
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
                        if (carte.deplacement() == Deplacement.MILIEU) {
                            if (validationDeplacement(GARDE_GAUCHE, -obtenirPositionElement(GARDE_GAUCHE))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, -obtenirPositionElement(GARDE_DROIT))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                        } else {
                            if (validationDeplacement(GARDE_GAUCHE, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_GAUCHE, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_GAUCHE)) - deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(GARDE_DROIT, -deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(GARDE_DROIT)) - deplacementCarte] = 1;
                            }
                        }
                        break;
                    case ROI:
                    case FOU:
                    case SORCIER:
                        if (carte.deplacement() == Deplacement.MILIEU) {
                            if (validationDeplacement(personnageManipulerParLeFou, -obtenirPositionElement(personnageManipulerParLeFou))) {
                                positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                            }
                        } else {
                            if (validationDeplacement(personnageManipulerParLeFou, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(personnageManipulerParLeFou)) + deplacementCarte] = 1;
                            }
                            if (validationDeplacement(personnageManipulerParLeFou, deplacementCarte)) {
                                positionAccessibleAvecCarte[positionPlus8(obtenirPositionElement(personnageManipulerParLeFou)) - deplacementCarte] = 1;
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
    public static void personnageManipulerParLeFou(Element personnage) {
        if (actionAutoriser()) {
            personnageManipulerParLeFou = personnage;
        }
        metAJour();
    }

    public static boolean estPouvoirFouActivable() {
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

    public static void teleportationPouvoirSorcier(Element element) {
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

    public static boolean estPouvoirSorcierActivable(Element element) {
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

    public static boolean estTeleportationValide(Element element, int teleporter) {
        return validationDeplacement(element, teleporter);
    }

    public static int calculerTeleportation(Element element) {
        int teleporter = 0;
        int positionSorcier = obtenirPositionElement(SORCIER);
        int positionElement = obtenirPositionElement(element);
        teleporter = positionSorcier - positionElement;
        return teleporter;
    }

    // ================
    // ===== TEST =====
    // ================
    public static int getPositionCouronne() {
        return plateau().couronne.positionCouronne();
    }

    public static boolean getEtatCouronne() {
        return plateau().couronne.etatCouronne();
    }

}
