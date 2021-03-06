package Modele;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
// import java.util.Random;
import java.util.Scanner;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Global.InfoJeu;
import Global.InfoPlateau;
import Pattern.Observable;
// import Structures.Iterateur;
import Structures.Sequence;

public class Jeu extends Observable {

    public Plateau plateau;
    int nombreTour; // TODO nombre Tour de jeu
    boolean partieEnCours = false;
    boolean partieTerminee = false;
    public boolean teleportationFaite = false;
    public boolean mainJoueurSecondaireVisible = false;
    public Element dernierTypeDePersonnageJouer;
    public Element typeDePersonnageJouerAuDernierTour;
    public Element personnageManipulerParLeFou;
    public int carteActuelle = 8;
    public int cartePassee = 8;
    public int casePassee = -1;

    // ===============================
    // ===== INFORMATION PLATEAU =====
    // ===============================
    static final int TAILLE_DU_PLATEAU = 17;
    static final int CENTRE_DU_PLATEAU = 0;
    static final int EXTREMITE_GAUCHE_DU_PLATEAU = -8;
    static final int EXTREMITE_DROITE_DU_PLATEAU = 8;
    static final int ENTREE_CHATEAU_GAUCHE = -6;
    static final int ENTREE_CHATEAU_DROIT = 6;

    static final int GAUCHE = 0;
    static final int DROITE = 1;

    int POSITION_DEBUT_TOUR_ROI = 0;
    int POSITION_DEBUT_TOUR_FOU = -1;
    int POSITION_DEBUT_TOUR_GARDE_GAUCHE = -2;
    int POSITION_DEBUT_TOUR_GARDE_DROIT = 2;
    int POSITION_DEBUT_TOUR_GARDE_SORCIER = 1;
    int JOUEUR_DEBUT_TOUR = 1;

    InfoJeu ETAT_JEU = InfoJeu.DEBUT_TOUR;

    // ==================
    // ===== JOUEUR =====
    // ==================
    String nomJoueurGauche, nomJoueurDroite;
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
    static final int AUCUN_GAGNANT = 0;
    static final int ROI_GAGNANT = 1;
    static final int COURONNE_GAGNANTE = 2;
    static final int MEULE_GAGNANTE_GAUCHE = 3;
    static final int MEULE_GAGNANTE_DROITE = 4;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Jeu() {
        plateau = new Plateau();
        changerEtatPartie();
        personnageManipulerParLeFou(FOU);
        initialiserDernierTypeDePersonnageJouer();
        initialiserTypeDePersonnageJouerDernierTour();
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

    public void nonFinPartie(){
        partieTerminee = false;
    }

    public boolean actionAutoriser() {
        if (!estPartieTerminee() && estPartieEnCours()) {
            return true;
        } else {
            return false;
        }
    }

    public void mainJoueurSecondaireVisible(){
        mainJoueurSecondaireVisible = !mainJoueurSecondaireVisible;
        metAJour();
    }

    public void changerEtatJeu(InfoJeu etatJeu){
        ETAT_JEU = etatJeu;
        metAJour();
    }

    public InfoJeu getEtatJeu(){
        return ETAT_JEU;
    }

    public String nomJoueurGauche(){
        return nomJoueurGauche;
    }

    public String nomJoueurDroite(){
        return nomJoueurDroite;
    }

    public void initNomJoueurs(String joueurGauche, String joueurDroite){
        nomJoueurGauche = joueurGauche;
        nomJoueurDroite = joueurDroite;
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
                        return false;
                    }
                case GARDE_DROIT:
                    if ((nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)
                            && (nouvellePositionElement > obtenirPositionElement(ROI))) {
                        return true;
                    } else {
                        return false;
                    }
                case ROI:
                    if ((nouvellePositionElement > obtenirPositionElement(GARDE_GAUCHE))
                            && (nouvellePositionElement < obtenirPositionElement(GARDE_DROIT))) {
                        return true;
                    } else {
                        return false;
                    }
                case FOU:
                    if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU)
                            && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                        return true;
                    } else {
                        return false;
                    }
                case SORCIER:
                    if ((nouvellePositionElement >= EXTREMITE_GAUCHE_DU_PLATEAU)
                            && (nouvellePositionElement <= EXTREMITE_DROITE_DU_PLATEAU)) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    Configuration.instance().logger().warning("Element " + element.name() + " inconnu !!");
                    return false;
            }
        } else {
            return false;
        }
    }

    public int obtenirPositionElement(Element element) {
        switch (element) {
            case ROI:
                return plateau().roi.positionPersonnage();
            case FOU:
                return plateau().fou.positionPersonnage();
            case SORCIER:
                return plateau().sorcier.positionPersonnage();
            case GARDE_GAUCHE:
                return plateau().gardeGauche.positionPersonnage();
            case GARDE_DROIT:
                return plateau().gardeDroit.positionPersonnage();
            case COURONNE:
                return plateau().couronne.positionCouronne();
            default:
                return 10;
        }
    }

    public Element obtenirElementPosition(int positon) {
        if (obtenirPositionElement(ROI) == positon) {
            return ROI;
        }
        if (obtenirPositionElement(FOU) == positon) {
            return FOU;
        }
        if (obtenirPositionElement(SORCIER) == positon) {
            return SORCIER;
        }
        if (obtenirPositionElement(GARDE_GAUCHE) == positon) {
            return GARDE_GAUCHE;
        }
        if (obtenirPositionElement(GARDE_DROIT) == positon) {
            return GARDE_DROIT;
        }
        return VIDE;
    }

    public Personnage obtenirPersonnageElement(Element element) {
        switch (element) {
            case ROI:
                return plateau().roi;
            case FOU:
                return plateau().fou;
            case SORCIER:
                return plateau().sorcier;
            case GARDE_GAUCHE:
                return plateau().gardeGauche;
            case GARDE_DROIT:
                return plateau().gardeDroit;
            default:
                return null;
        }
    }

    public int obtenirInfoPlateau(InfoPlateau element) {
        switch (element) {
            case TAILLE_DU_PLATEAU:
                return TAILLE_DU_PLATEAU;
            case CENTRE_DU_PLATEAU:
                return CENTRE_DU_PLATEAU;
            case EXTREMITE_GAUCHE_DU_PLATEAU:
                return EXTREMITE_GAUCHE_DU_PLATEAU;
            case EXTREMITE_DROITE_DU_PLATEAU:
                return EXTREMITE_DROITE_DU_PLATEAU;
            case ENTREE_CHATEAU_GAUCHE:
                return ENTREE_CHATEAU_GAUCHE;
            case ENTREE_CHATEAU_DROIT:
                return ENTREE_CHATEAU_DROIT;
            default:
                return -1;
        }
    }

    public int carteActuelle() {
        return carteActuelle;
    }

    public void changeCarteActuelle(int i) {
        carteActuelle = i;
        metAJour();
    }

    public int cartePasse() {
        return cartePassee;
    }

    public void choisirPasserSurCarte(int i) {
        cartePassee = i;
        metAJour();
    }

    public int casePassee() {
        return casePassee;
    }

    public void choisirPasserSurCase(int i) {
        casePassee = i;
        metAJour();
    }

    public void echangerFouSorcier() {
        plateau().echangerFouSorcier();
    }

    public void annulerTour() {
        obtenirPersonnageElement(ROI).positionnerPersonnage(POSITION_DEBUT_TOUR_ROI);
        obtenirPersonnageElement(FOU).positionnerPersonnage(POSITION_DEBUT_TOUR_FOU);
        obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(POSITION_DEBUT_TOUR_GARDE_DROIT);
        obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(POSITION_DEBUT_TOUR_GARDE_GAUCHE);
        obtenirPersonnageElement(SORCIER).positionnerPersonnage(POSITION_DEBUT_TOUR_GARDE_SORCIER);
        plateau().paquet.completerCartesEnMain(joueurCourant());
        personnageManipulerParLeFou = FOU;
        dernierTypeDePersonnageJouer = VIDE;
        teleportationFaite = false;
        if (joueurCourant() != JOUEUR_DEBUT_TOUR) {
            plateau().joueurCourant = JOUEUR_DEBUT_TOUR;
        }
        metAJour();
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
        return plateau().numeroJoueurValide(numeroJoueurVoulu);
    }

    public void changerJoueurCourant() {
        if (actionAutoriser()) {
            plateau().changerJoueurCourant();
        }
        metAJour();
    }

    public int joueurCourant() {
        return plateau().joueurCourant;
    }

    public int joueurSecondaire() {
        if(plateau().joueurCourant == 0){
            return 1;
        }
        return 0;
    }

    public int joueurGagnant() {
        return plateau().joueurGagnant;
    }

    // ===================
    // ===== GAGNANT =====
    // ===================
    public boolean estGagnant() {
        return plateau().estGagnant() != AUCUN_GAGNANT;
    }

    public String traiterGagnant() {
        String nomWin;
        if(joueurGagnant() == JOUEUR_GAUCHE){
            nomWin = nomJoueurGauche;
        }
        else{
            nomWin = nomJoueurDroite;
        }
        switch (plateau().estGagnant()) {
            case COURONNE_GAGNANTE:
                partieTerminee = true;
                partieEnCours = false;
                metAJour();
                return "Victoire de " + nomWin + " avec la couronne !!";
            case ROI_GAGNANT:
                partieTerminee = true;
                partieEnCours = false;
                metAJour();
                return "Victoire de " + nomWin + " avec le roi !!";
            default:
                break;
        }
        if (plateau().victoirePioche) {
            partieTerminee = true;
            partieEnCours = false;
            metAJour();
            return "Victoire de " + nomWin + " ?? la pioche !!";
        }
        return "";
    }

    // =======================
    // ===== FIN DE TOUR =====
    // =======================
    public void finDeTour() {
        deplacerCouronne(plateau().valeurDeplacementCouronne());
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
                    Configuration.instance().logger().info("La pioche se recharge pour la premi??re fois !");
                } else {
                    if (obtenirPositionElement(ROI) == 0) {
                        plateau.paquet.melangerDefausse();
                        plateau.paquet.remplirMain(plateau().joueurCourant);
                        changerJoueurCourant();
                        Configuration.instance().logger().info("Le roi est au centre, la partie continue !");
                    } else {
                        if (obtenirPositionElement(ROI) > 0) {
                            plateau().joueurGagnant = JOUEUR_DROIT;
                        } else {
                            plateau().joueurGagnant = JOUEUR_GAUCHE;
                        }
                        plateau().victoirePioche = true;
                        traiterGagnant();
                    }
                }
            }
        }
        personnageManipulerParLeFou(FOU);
        typeDePersonnageJouerAuDernierTour = dernierTypeDePersonnageJouer;
        initialiserDernierTypeDePersonnageJouer();
        teleportationFaite = false;
        carteActuelle = 8;
        fixerPositions();
        metAJour();
    }

    public void fixerPositions() {
        POSITION_DEBUT_TOUR_FOU = obtenirPositionElement(FOU);
        POSITION_DEBUT_TOUR_GARDE_DROIT = obtenirPositionElement(GARDE_DROIT);
        POSITION_DEBUT_TOUR_GARDE_GAUCHE = obtenirPositionElement(GARDE_GAUCHE);
        POSITION_DEBUT_TOUR_GARDE_SORCIER = obtenirPositionElement(SORCIER);
        POSITION_DEBUT_TOUR_ROI = obtenirPositionElement(ROI);
        JOUEUR_DEBUT_TOUR = joueurCourant();
    }

    // ====================
    // ===== COURONNE =====
    // ====================
    public void deplacerCouronne(int deplacementCouronne) {
        plateau().deplacerCouronne(deplacementCouronne);
        metAJour();
    }

    public void poserCouronne(int nouvellePosition){
        plateau().couronne.positionnerCouronne(nouvellePosition);
    }

    public void changerEtatCouronne() {
        if (actionAutoriser()) {
            plateau().changerEtatCouronne();
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

    public void deplacerCour(int direction, int[] cartes) {
        if (actionAutoriser()) {
            int i = 0;
            while (i != cartes.length) {
                poserCarte(cartes[i]);
                i++;
            }
            if (direction == GAUCHE) { // Gauche
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(obtenirPositionElement(GARDE_GAUCHE) - 1);
                obtenirPersonnageElement(ROI).positionnerPersonnage(obtenirPositionElement(ROI) - 1);
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(obtenirPositionElement(GARDE_DROIT) - 1);
            } else { // Droit
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(obtenirPositionElement(GARDE_DROIT) + 1);
                obtenirPersonnageElement(ROI).positionnerPersonnage(obtenirPositionElement(ROI) + 1);
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(obtenirPositionElement(GARDE_GAUCHE) + 1);
            }
        }
        metAJour();
    }

    public void unPlusUn(int direction, int carte) {
        if (actionAutoriser()) {
            poserCarte(carte);
            if (direction == GAUCHE) { // Gauche
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(obtenirPositionElement(GARDE_GAUCHE) - 1);
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(obtenirPositionElement(GARDE_DROIT) - 1);
            } else {
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(obtenirPositionElement(GARDE_GAUCHE) + 1);
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(obtenirPositionElement(GARDE_DROIT) + 1);
            }
        }
        metAJour();
    }

    public void rapproche(int carte) {
        if (actionAutoriser()) {
            poserCarte(carte);
            obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(obtenirPositionElement(ROI) - 1);
            obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(obtenirPositionElement(ROI) + 1);
        }
        metAJour();
    }

    public void poserCarte(int positionCarteDansLaMain) {
        majDernierTypeDePersonnageJouer(
                recupererMainJoueur(plateau().joueurCourant)[positionCarteDansLaMain].personnage());
        plateau().paquet.enleverCarte(plateau().joueurCourant, positionCarteDansLaMain);
    }

    public Carte[] recupererMainJoueur(int joueur) {
        if (actionAutoriser()) {
            if (numeroJoueurValide(joueur)) {
                return plateau().paquet.mainJoueur(joueur);
            } else {
                Configuration.instance().logger().warning("Numero de joueur incorect !!");
                return null;
            }
        } else {
            Configuration.instance().logger().warning("Partie stopee !!");
            return null;
        }
    }

    public void initialiserTypeDePersonnageJouerDernierTour() {
        typeDePersonnageJouerAuDernierTour = VIDE;
    }

    public void initialiserDernierTypeDePersonnageJouer() {
        dernierTypeDePersonnageJouer = VIDE;
    }

    public void majDernierTypeDePersonnageJouer(Element perso) {
        dernierTypeDePersonnageJouer = perso;
    }

    public void majTypeDePersoDuDernierTour(){
        typeDePersonnageJouerAuDernierTour = dernierTypeDePersonnageJouer;
    }

    public int[] listeCarteJouable() {
        int nombreCartes = plateau().paquet.nombreCartesEnMain();
        int indice = 0;
        int[] resultat;
        if (dernierTypeDePersonnageJouer == VIDE) {
            resultat = initialiserTableau(nombreCartes, 1);
        } else {
            resultat = new int[nombreCartes];
            while (indice < nombreCartes) {
                Carte carte = plateau().paquet.mainJoueur(plateau().joueurCourant)[indice];
                if (carte.personnage() == dernierTypeDePersonnageJouer) {
                    resultat[indice] = 1;
                }
                indice++;
            }
        }
        return resultat;
    }

    public boolean carteJouable(Carte carteCourante) {
        if(personnageManipulerParLeFou() != Element.FOU){
            if(carteCourante.personnage() == personnageManipulerParLeFou() || carteCourante.personnage() == Element.FOU){
                return true;
            }else{
                return false;
            }
        }
        else{
            if (carteCourante.personnage() == dernierTypeDePersonnageJouer || dernierTypeDePersonnageJouer == VIDE) {
                return true;
            }
            return false;
        } 
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

    public int[] listeDeplacementPossiblesAvecPerso(Element perso) {
        int[] positionAccessibleAvecPerso = initialiserTableau(TAILLE_DU_PLATEAU, 0);
        Carte[] listeCarte = plateau().paquet.carteSelonPerso(plateau().joueurCourant, perso);
        int positionDeBase = obtenirPositionElement(perso);
        if (perso == GARDE_GAUCHE || perso == GARDE_DROIT) {
            Carte[] listeCarte2 = plateau().paquet.carteSelonPerso(plateau().joueurCourant, Element.GARDES);
            if (perso == GARDE_GAUCHE) {
                int pos2 = obtenirPositionElement(GARDE_DROIT);
                positionAccessibleAvecPerso = selonLePersoMaisEnRecurcifPersoGardes(GARDE_GAUCHE, GARDE_DROIT,
                        listeCarte2, positionAccessibleAvecPerso, positionDeBase, pos2);
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(positionDeBase);
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(pos2);
            } else {
                int pos2 = obtenirPositionElement(GARDE_GAUCHE);
                positionAccessibleAvecPerso = selonLePersoMaisEnRecurcifPersoGardes(GARDE_DROIT, GARDE_GAUCHE,
                        listeCarte2, positionAccessibleAvecPerso, positionDeBase, pos2);
                obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(positionDeBase);
                obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(pos2);
            }
            return positionAccessibleAvecPerso;
        }
        positionAccessibleAvecPerso = selonLePersoMaisEnRecurcifPersoBase(perso, listeCarte,
                positionAccessibleAvecPerso, positionDeBase);
        obtenirPersonnageElement(perso).positionnerPersonnage(positionDeBase);
        return positionAccessibleAvecPerso;
    }

    public int[] selonLePersoMaisEnRecurcifPersoBase(Element perso, Carte[] listeCarte, int[] positions,
            int positionRelatif) {
        int i = 0;
        Carte vide = new Carte(VIDE, Deplacement.VIDE);
        while (i < 8) {
            obtenirPersonnageElement(perso).positionnerPersonnage(positionRelatif);
            Carte carte = listeCarte[i];
            Carte[] sansLaCarte = Arrays.copyOf(listeCarte, 8);
            sansLaCarte[i] = vide;
            if (!carte.estIdentique(vide)) {
                int[] deplacementRelatif = listeDeplacementPossiblesAvecCarte(perso, carte.deplacement());
                positions = fustionTableau(positions, deplacementRelatif);
                int j = 0;
                while (j < TAILLE_DU_PLATEAU) {
                    if (deplacementRelatif[j] == 1) {
                        positions = selonLePersoMaisEnRecurcifPersoBase(perso, sansLaCarte, positions, j - 8);
                    }
                    j++;
                }
            }
            i++;
        }
        return positions;
    }

    public int[] selonLePersoMaisEnRecurcifPersoGardes(Element perso1, Element perso2, Carte[] listeCarte,
            int[] positions, int positionRelatif1, int positionRelatif2) {
        int i = 0;
        Carte vide = new Carte(VIDE, Deplacement.VIDE);
        while (i < 8) {
            obtenirPersonnageElement(perso1).positionnerPersonnage(positionRelatif1);
            obtenirPersonnageElement(perso2).positionnerPersonnage(positionRelatif2);
            Carte carte = listeCarte[i];
            Carte[] sansLaCarte = Arrays.copyOf(listeCarte, 8);
            sansLaCarte[i] = vide;
            if (!carte.estIdentique(vide)) {
                int[] deplacementRelatif = listeDeplacementPossiblesAvecCarte(perso1, carte.deplacement());
                positions = fustionTableau(positions, deplacementRelatif);
                int j = 0;
                while (j < TAILLE_DU_PLATEAU) {
                    if (deplacementRelatif[j] == 1) {
                        if (carte.deplacement() == Deplacement.UN_PLUS_UN) {
                            if (perso1 == GARDE_GAUCHE) {
                                if (j == positionRelatif1 - 2) {
                                    positionRelatif1 = j - 8;
                                }
                                if (j == positionRelatif1 - 1) {
                                    positionRelatif1 = j - 8;
                                    positionRelatif2 = positionRelatif2 - 1;
                                }
                                if (j == positionRelatif1 + 1) {
                                    positionRelatif1 = j - 8;
                                    positionRelatif2 = positionRelatif2 + 1;
                                }
                                if (j == positionRelatif1 + 2) {
                                    positionRelatif1 = j - 8;
                                }
                            } else {
                                if (j == positionRelatif1 - 2) {
                                    positionRelatif1 = j - 8;
                                }
                                if (j == positionRelatif1 - 1) {
                                    positionRelatif1 = j - 8;
                                    positionRelatif2 = positionRelatif2 - 1;
                                }
                                if (j == positionRelatif1 + 1) {
                                    positionRelatif1 = j - 8;
                                    positionRelatif2 = positionRelatif2 - 1;
                                }
                                if (j == positionRelatif1 + 2) {
                                    positionRelatif1 = j - 8;
                                }
                            }
                        }
                        if (carte.deplacement() == Deplacement.RAPPROCHE) {
                            if (perso1 == GARDE_GAUCHE) {
                                positionRelatif1 = obtenirPositionElement(ROI) - 1;
                                positionRelatif2 = obtenirPositionElement(ROI) + 1;
                            } else {
                                positionRelatif1 = obtenirPositionElement(ROI) + 1;
                                positionRelatif2 = obtenirPositionElement(ROI) - 1;
                            }
                        }
                        if (carte.deplacement() == Deplacement.UN) {
                            positionRelatif1 = j - 8;
                        }
                    }
                    positions = selonLePersoMaisEnRecurcifPersoGardes(perso1, perso2, sansLaCarte, positions,
                            positionRelatif1, positionRelatif2);
                    j++;
                }
            }
            i++;
        }
        return positions;
    }

    public int[] fustionTableau(int[] un, int[] deux) {
        int[] res = initialiserTableau(TAILLE_DU_PLATEAU, 0);
        int i = 0;
        while (i < TAILLE_DU_PLATEAU) {
            if ((un[i] == 1) || (deux[i] == 1)) {
                res[i] = 1;
            }
            i++;
        }
        return res;
    }

    public int positionPlus8(int positionElement) {
        return positionElement + EXTREMITE_DROITE_DU_PLATEAU;
    }

    public int positionsPourCour() {
        if (obtenirPositionElement(GARDE_DROIT) == EXTREMITE_DROITE_DU_PLATEAU
                && obtenirPositionElement(GARDE_GAUCHE) == EXTREMITE_GAUCHE_DU_PLATEAU) {
            return 3; // Les deux impossible
        }
        if (obtenirPositionElement(GARDE_DROIT) == EXTREMITE_DROITE_DU_PLATEAU) {
            return 1; // Droit impossible
        }
        if (obtenirPositionElement(GARDE_GAUCHE) == EXTREMITE_GAUCHE_DU_PLATEAU) {
            return 2; // Gauche impossible
        }
        return 0; // Tout possible
    }

    public int[] listeDeplacementPossiblesAvecCarte(Element perso, Deplacement deplace) {
        int[] positionAccessibleAvecCarte = initialiserTableau(TAILLE_DU_PLATEAU, 0);
        int deplacementCarte = deplace.getValeurDeplacement();
        switch (perso) {
            case GARDES:
                if (personnageManipulerParLeFou == Element.GARDES) {
                    Element el = personnageManipulerParLeFou;
                    personnageManipulerParLeFou(GARDE_GAUCHE);
                    positionAccessibleAvecCarte = listeDeplacementPossiblesAvecCarte(FOU, deplace);
                    personnageManipulerParLeFou(GARDE_DROIT);
                    positionAccessibleAvecCarte = fustionTableau(positionAccessibleAvecCarte,
                            listeDeplacementPossiblesAvecCarte(FOU, deplace));
                    personnageManipulerParLeFou(el);
                } else {
                    positionAccessibleAvecCarte = fustionTableau(
                            listeDeplacementPossiblesAvecCarte(GARDE_GAUCHE, deplace),
                            listeDeplacementPossiblesAvecCarte(GARDE_DROIT, deplace));
                }
                break;
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
                    case GARDES:
                        Element el = personnageManipulerParLeFou;
                        personnageManipulerParLeFou(GARDE_GAUCHE);
                        positionAccessibleAvecCarte = listeDeplacementPossiblesAvecCarte(FOU, deplace);
                        personnageManipulerParLeFou(GARDE_DROIT);
                        positionAccessibleAvecCarte = fustionTableau(positionAccessibleAvecCarte,
                                listeDeplacementPossiblesAvecCarte(FOU, deplace));
                        personnageManipulerParLeFou(el);
                        break;
                    case GARDE_GAUCHE:
                        if (deplace == Deplacement.MILIEU) {
                            if (validationDeplacement(GARDE_GAUCHE, -obtenirPositionElement(GARDE_GAUCHE))) {
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
                        if (estPouvoirFouActivable()) {
                            if (deplace == Deplacement.MILIEU) {
                                if (validationDeplacement(personnageManipulerParLeFou,
                                        -obtenirPositionElement(personnageManipulerParLeFou))) {
                                    positionAccessibleAvecCarte[EXTREMITE_DROITE_DU_PLATEAU] = 1;
                                }
                            } else {
                                if (validationDeplacement(personnageManipulerParLeFou, deplacementCarte)) {
                                    positionAccessibleAvecCarte[positionPlus8(
                                            obtenirPositionElement(personnageManipulerParLeFou))
                                            + deplacementCarte] = 1;
                                }
                                if (validationDeplacement(personnageManipulerParLeFou, -deplacementCarte)) {
                                    positionAccessibleAvecCarte[positionPlus8(
                                            obtenirPositionElement(personnageManipulerParLeFou))
                                            - deplacementCarte] = 1;
                                }
                            }
                        }
                        break;
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
    public Element personnageManipulerParLeFou() {
        return personnageManipulerParLeFou;
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
                    metAJour();
                    return false;
                }
            } else {
                if (positionFou < positionRoi) {
                    metAJour();
                    return true;
                } else {
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
                    teleportationFaite = true;
                    break;

                case GARDE_GAUCHE:
                    plateau.gardeGauche.deplacerPersonnage(distanceTeleportation);
                    teleportationFaite = true;
                    break;

                case GARDE_DROIT:
                    plateau.gardeDroit.deplacerPersonnage(distanceTeleportation);
                    teleportationFaite = true;
                    break;

                default:
                    break;
            }
        } else {
        }
        metAJour();
    }

    public boolean estPouvoirSorcierActivable(Element element) {
        int distanceTeleportation = calculerTeleportation(element);
        if (estTeleportationValide(element, distanceTeleportation)) {
            metAJour();
            return true;
        } else {
            metAJour();
            return false;
        }
    }

    public boolean estTeleportationValide(Element element, int distanceDeLateleportation) {
        return validationDeplacement(element, distanceDeLateleportation);
    }

    public int calculerTeleportation(Element element) {
        int teleporter = 0;
        int positionSorcier = obtenirPositionElement(SORCIER);
        int positionElement = obtenirPositionElement(element);
        teleporter = positionSorcier - positionElement;
        return teleporter;
    }

    // ======================
    // ===== HISTORIQUE =====
    // ======================
    public Coup creerCoup(Plateau plat) {
        return plateau.creerCoup(plat);
    }

    public void jouerCoup(Coup coup) {
        if (coup == null) {
            System.out.println("Coup Vide !!!");
        } else {
            plateau().jouerCoup(coup);
            metAJour();
        }
    }

    public Coup annule() {
        Coup futur = creerCoup(plateau());
        futur.fixerPlateau(plateau());
        Coup coup = plateau().annuler(futur);
        // plateau().afficherPlateau();
        metAJour();
        return coup;
    }

    public Coup refaire() {
        Coup passe = creerCoup(plateau());
        passe.fixerPlateau(plateau());
        Coup coup = plateau().refaire(passe);
        // plateau().afficherPlateau();
        metAJour();
        return coup;
    }

    public void viderHistorique() {
        plateau().viderHistorique();
    }

    // ======================
    // ===== SAUVEGARDE =====
    // ======================

    public void sauvegarderPlateau(BufferedWriter bw){
        try {

            // Sauvegarde du joueur du tour 
            bw.write("" + plateau().joueurCourant + "\n"); 

            // Sauvegarde des positions
            bw.write("" + plateau().roi.positionPersonnage + "\n"); // On note la position du ROI
            bw.write("" + plateau().gardeGauche.positionPersonnage + "\n"); // On note la position du GARDE_GAUCHE
            bw.write("" + plateau().gardeDroit.positionPersonnage + "\n"); // On note la position du GARDE_DROIT
            bw.write("" + plateau().fou.positionPersonnage + "\n"); // On note la position du FOU
            bw.write("" + plateau().sorcier.positionPersonnage + "\n"); // On note la position du SORCIER
            bw.write("" + plateau().couronne.positionCouronne + "\n"); // On note la position de la COURONNE
            bw.write("" + plateau().couronne.etatCouronne + "\n"); // On note l'??tat

            //Sauvegarde de la main des joueurs
            int i = 0;
            while(i < 8){
                bw.write("" + plateau().paquet.mainJoueur(JOUEUR_GAUCHE)[i].personnage() + "\n");
                bw.write("" + plateau().paquet.mainJoueur(JOUEUR_GAUCHE)[i].deplacement() + "\n");
                bw.write("" + plateau().paquet.mainJoueur(JOUEUR_DROIT)[i].personnage() + "\n");
                bw.write("" + plateau().paquet.mainJoueur(JOUEUR_DROIT)[i].deplacement() + "\n");
                i++;
            }

            // Sauvegarde des cartes de la pioche
            Sequence<Carte> liste = Configuration.instance().nouvelleSequence();
            Carte carte = null;
            while( !plateau().paquet.pioche().estVide() ){
                carte = plateau().paquet.pioche().extraitTete();
                liste.insereTete(carte);
            }
            carte = null;
            while( !liste.estVide() ){
                carte = liste.extraitTete();
                plateau().paquet.pioche().insereTete(carte);
                bw.write("" + carte.personnage() + "\n");
                bw.write("" + carte.deplacement() + "\n");
            }
            bw.write("SAUVEGARDE\n");

            // Sauvegarde des cartes de la defausse
            liste = Configuration.instance().nouvelleSequence();
            carte = null;
            while( !plateau().paquet.defausse().estVide() ){
                carte = plateau().paquet.defausse().extraitTete();
                liste.insereTete(carte);
            }
            carte = null;
            while( !liste.estVide() ){
                carte = liste.extraitTete();
                plateau().paquet.defausse().insereTete(carte);
                bw.write("" + carte.personnage() + "\n");
                bw.write("" + carte.deplacement() + "\n");
            }

            bw.write("SAUVEGARDE\n");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }


    public void sauvegarder(int type1, int type2){
        try {
            annulerTour();
            // On r??cup??re la date pour le nom de la sauvegarde
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            File dossier = new File(System.getProperty("user.dir") + File.separator + "Sauvegardes Visite Royal"); 
            dossier.mkdir();

            String fic = System.getProperty("user.dir") + File.separator + "Sauvegardes Visite Royal" + File.separator + dtf.format(LocalDateTime.now()) + ".txt";
            File f = new File(fic);

            // On fait le fichier
            if (!f.isFile()){
                f.createNewFile();
                Configuration.instance().logger().info("Fichier Creer !");
            }
            else{
                Configuration.instance().logger().info("Sauvegarde existe deja !");
            }

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);

            // On note les noms des joueurs
            bw.write("" + nomJoueurGauche + "\n");
            bw.write("" + nomJoueurDroite + "\n");

            // On note les type de joueurs
            bw.write("" + type1 + "\n"); 
            bw.write("" + type2 + "\n"); 

            // On remonte dans le temps 
            int tourActuel = 0;
            while(plateau().peutAnnuler()){
                tourActuel++;
                annule();
            }

            // On note le tour actuel
            bw.write("" + tourActuel + "\n"); 

            // On sauvegarde tous les plateaux 

            // On commence par le 1er
            sauvegarderPlateau(bw);

            // Puis tous ceux du futur
            int tourTotal = 0;
            while (plateau().peutRefaire()) {
                tourTotal++;
                refaire();
                sauvegarderPlateau(bw);
            }

            int voulu = tourTotal - tourActuel;
            while(voulu != 0){
                voulu--;
                annule();
            }

            // Puis on ferme le fichier
            bw.close();
            Configuration.instance().logger().info("Fichier Sauvegarde !");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void chargerPlateau(Scanner scan){

        // On charge le joueur courant 
        plateau().joueurCourant = Integer.parseInt(scan.nextLine());

        // Lecture des positions des personnages
        obtenirPersonnageElement(ROI).positionnerPersonnage(Integer.parseInt(scan.nextLine()));
        obtenirPersonnageElement(GARDE_GAUCHE).positionnerPersonnage(Integer.parseInt(scan.nextLine()));
        obtenirPersonnageElement(GARDE_DROIT).positionnerPersonnage(Integer.parseInt(scan.nextLine()));
        obtenirPersonnageElement(FOU).positionnerPersonnage(Integer.parseInt(scan.nextLine()));
        obtenirPersonnageElement(SORCIER).positionnerPersonnage(Integer.parseInt(scan.nextLine()));

        // Lecture de la couronne
        plateau().couronne.positionnerCouronne(Integer.parseInt(scan.nextLine()));
        plateau().couronne.definirEtatCouronne(Boolean.parseBoolean(scan.nextLine()));

        // Lecture des mains des joueurs
        String lecture = scan.nextLine();
        String deplace = null;
        Carte carte = null;
        int i = 0;
        int j = 0;
        int k = 0;
        while(i != 16){
            deplace = scan.nextLine();
            carte = creationCarte(lecture, deplace);
            if(j == 2){
                j = 0;
                k++;
            }
            plateau().paquet.mainJoueurs[j][k] = carte;
            j++;
            i++;
            lecture = scan.nextLine();
        }

        // Lecture de la pioche
        while( !plateau().paquet.pioche().estVide() ){
            plateau().paquet.pioche().extraitTete();
        }
        carte = null;
        while(!lecture.equals("SAUVEGARDE")){
            deplace = scan.nextLine();
            carte = creationCarte(lecture, deplace);
            plateau().paquet.pioche().insereTete(carte);
            lecture = scan.nextLine();
        }

        // Lecture de la defausse
        while( !plateau().paquet.defausse().estVide() ){
            plateau().paquet.defausse().extraitTete();
        }

        lecture = scan.nextLine();
        carte = null;
        while(!lecture.equals("SAUVEGARDE")){
            deplace = scan.nextLine();
            carte = creationCarte(lecture, deplace);
            plateau().paquet.defausse().insereTete(carte);
            if(scan.hasNext()){
                lecture = scan.nextLine();
            }
        }
    }

    public int[] charger(String fic){
        int type[] = new int[2];
        try{
            Scanner scan = new Scanner(new File(fic));

            // Lecture des noms des joueurs
            nomJoueurGauche = scan.nextLine();
            nomJoueurDroite = scan.nextLine();

            // Lecture du type des joueurs 
            type[0] = Integer.parseInt(scan.nextLine()); 
            type[1] = Integer.parseInt(scan.nextLine()); 

            // Lecture du tour actuel (C'est un outil surprise qui nous servira plus tard)
            int tourActuel = Integer.parseInt(scan.nextLine()); 

            int tourTotal = 0;

            chargerPlateau(scan);

            while(scan.hasNext()){
                tourTotal++;
                gestionHistorique();
                chargerPlateau(scan);
            }

            int voulu = tourTotal - tourActuel;
            while(voulu != 0){
                voulu--;
                annule();
            }

            scan.close();
            Configuration.instance().logger().info("Partie charg??e !");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return type;
    }

    void gestionHistorique() {
        Coup coup = creerCoup(plateau());
        if (coup != null) {
            jouerCoup(coup);
        } else {
            System.out.println("Creation d'un coup null !!!");
        }
    }

    Carte creationCarte(String el, String de){
        Carte carte = null;
        Element perso = VIDE;
        switch (el) {
            case "ROI":
                perso = ROI;
                break;

            case "GARDES":
                perso = Element.GARDES;
                break;

            case "FOU":
                perso = FOU;
                break;

            case "SORCIER":
                perso = SORCIER;
                break;

            default:
                break;
        }
        Deplacement deplace = Deplacement.VIDE;
        switch (de) {
            case "UN":
                deplace = Deplacement.UN;
                break;

            case "DEUX":
                deplace = Deplacement.DEUX;
                break;

            case "TROIS":
                deplace = Deplacement.TROIS;
                break;

            case "QUATRE":
                deplace = Deplacement.QUATRE;
                break;

            case "CINQ":
                deplace = Deplacement.CINQ;
                break;

            case "MILIEU":
                deplace = Deplacement.MILIEU;
                break;

            case "UN_PLUS_UN":
                deplace = Deplacement.UN_PLUS_UN;
                break;

            case "RAPPROCHE":
                deplace = Deplacement.RAPPROCHE;
                break;

            default:
                break;
        }
        carte = new Carte(perso, deplace);
        return carte;
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
