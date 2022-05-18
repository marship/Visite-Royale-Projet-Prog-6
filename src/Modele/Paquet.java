package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Structures.Sequence;

import java.util.Random;

import Structures.Couple;
import Structures.FAP;
import Structures.FAPListe;

public class Paquet {

    Sequence<Carte> pioche, defausse, tourActuel;
    Object[][] mainJoueurs;

    // ===============================
    // ===== INFORMATIONS CARTES =====
    // ===============================
    static final int NOMBRE_TYPE_CARTE = 4;

    static final int NOMBRE_TYPE_CARTE_ROI = 1;
    static final int NOMBRE_TYPE_CARTE_GARDES = 3;
    static final int NOMBRE_TYPE_CARTE_FOU = 6;
    static final int NOMBRE_TYPE_CARTE_SORCIER = 3;

    static final int NOMBRE_CARTE_ROI_1 = 12;

    static final int NOMBRE_CARTE_GARDES_1 = 4;
    static final int NOMBRE_CARTE_GARDES_1_PLUS_1 = 10;
    static final int NOMBRE_CARTE_GARDES_RAPPROCHE = 2;

    static final int NOMBRE_CARTE_FOU_MILIEU = 2;
    static final int NOMBRE_CARTE_FOU_1 = 1;
    static final int NOMBRE_CARTE_FOU_2 = 3;
    static final int NOMBRE_CARTE_FOU_3 = 4;
    static final int NOMBRE_CARTE_FOU_4 = 3;
    static final int NOMBRE_CARTE_FOU_5 = 1;

    static final int NOMBRE_CARTE_SORCIER_1 = 2;
    static final int NOMBRE_CARTE_SORCIER_2 = 8;
    static final int NOMBRE_CARTE_SORCIER_3 = 2;

    static final int NOMBRE_JOUEUR = 2;
    static final int NOMBRE_CARTE_EN_MAIN = 8;

    // ====================
    // ===== ELEMENTS =====
    // ====================
    static final Element VIDE_ELEMENT = Element.VIDE;
    static final Element ROI = Element.ROI;
    static final Element GARDES = Element.GARDES;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;

    // ========================
    // ===== DEPLACEMENTS =====
    // ========================
    static final Deplacement VIDE_DEPLACEMENT = Deplacement.VIDE;
    static final Deplacement RAPPROCHE = Deplacement.RAPPROCHE;
    static final Deplacement UN = Deplacement.UN;
    static final Deplacement DEUX = Deplacement.DEUX;
    static final Deplacement TROIS = Deplacement.TROIS;
    static final Deplacement QUATRE = Deplacement.QUATRE;
    static final Deplacement CINQ = Deplacement.CINQ;
    static final Deplacement MILIEU = Deplacement.MILIEU;
    static final Deplacement UN_PLUS_UN = Deplacement.UN_PLUS_UN;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Paquet() {
        tourActuel = Configuration.instance().nouvelleSequence();
        creerPaquet();
        melanger();
        distribuer();
        trier();
    }

    public void afficherPioche() {
        Configuration.instance().logger().info("Debut de l'affichage de la pioche !\n");
        Sequence<Carte> tmp = Configuration.instance().nouvelleSequence();
        while (!pioche.estVide()) {
            tmp.insereTete(pioche.extraitTete());
        }
        while (!tmp.estVide()) {
            Carte carte = tmp.extraitTete();
            System.out.println(carte.toString());
            pioche.insereTete(carte);
        }
        Configuration.instance().logger().info("Fin de l'affichage de la pioche !\n");
    }

    public void afficherDefausse() {
        Configuration.instance().logger().info("Debut de l'affichage de la defausse !\n");
        Sequence<Carte> tmp = Configuration.instance().nouvelleSequence();
        while (!defausse.estVide()) {
            tmp.insereTete(defausse.extraitTete());
        }
        while (!tmp.estVide()) {
            Carte carte = tmp.extraitTete();
            System.out.println(carte.toString());
            defausse.insereTete(carte);
        }
        Configuration.instance().logger().info("Fin de l'affichage de la defausse !\n");
    }

    public void afficherMain(int joueur) {
        int i = 0;
        while (i < NOMBRE_CARTE_EN_MAIN) {
            Carte carte = (Carte) mainJoueurs[joueur][i];
            System.out.print("|" + carte.toString() + "|");
            i++;
        }
        System.out.println("\n");
    }

    public void completerCartesEnMain(int joueur) {
        int i = 0;
        Carte vide = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        while (i < NOMBRE_CARTE_EN_MAIN) {
            Carte carte = (Carte) mainJoueurs[joueur][i];
            if (carte.estIdentique(vide)) {
                mainJoueurs[joueur][i] = tourActuel.extraitTete();
            }
            i++;
        }
        trierJoueur(joueur);
    }

    public void melanger() {
        FAP<Couple<Carte, Integer>> fap = new FAPListe<>();
        Random r = new Random();
        while (!pioche.estVide()) {
            fap.insere(new Couple<Carte, Integer>(pioche.extraitTete(), r.nextInt(10000)));
        }
        while (!fap.estVide()) {
            pioche.insereTete(fap.extrait().e());
        }
    }

    public void trier() {
        for (int i = 0; i < NOMBRE_JOUEUR; i++) {
            trierJoueur(i);
        }
    }

    public void trierJoueur(int joueur) {
        Object[][] tableauTrier = new Object[NOMBRE_TYPE_CARTE][NOMBRE_CARTE_EN_MAIN];
        int[][] nbChaqueType = new int[NOMBRE_TYPE_CARTE][1];
        int type = 0;
        int position = 0;
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte carte = (Carte) mainJoueurs[joueur][i];
            switch (carte.personnage()) {
                case FOU:
                    position = nbChaqueType[0][0];
                    nbChaqueType[0][0] = nbChaqueType[0][0] + 1;
                    type = 0;
                    break;

                case SORCIER:
                    position = nbChaqueType[1][0];
                    nbChaqueType[1][0] = nbChaqueType[1][0] + 1;
                    type = 1;
                    break;

                case GARDES:
                    position = nbChaqueType[2][0];
                    nbChaqueType[2][0] = nbChaqueType[2][0] + 1;
                    type = 2;
                    break;

                case ROI:
                    position = nbChaqueType[3][0];
                    nbChaqueType[3][0] = nbChaqueType[3][0] + 1;
                    type = 3;
                    break;

                default:
                    break;

            }
            tableauTrier[type][position] = (Carte) carte;
        }
        for (int i = 0; i < NOMBRE_TYPE_CARTE; i++) {
            trierTableau(tableauTrier[i], 0, nbChaqueType[i][0] - 1);
        }

        position = 0;
        for (int i = 0; i < NOMBRE_TYPE_CARTE; i++) {
            int t = nbChaqueType[i][0];
            for (int j = 0; j < t; j++) {
                mainJoueurs[joueur][position] = (Carte) tableauTrier[i][j];
                position++;
            }
        }
    }

    int partition(Object[] objects, int start, int end) {
        Carte carte = (Carte) objects[end];
        int pivot = carte.deplacement().getValeurDeplacement();
        int i = (start - 1);

        for (int j = start; j <= end - 1; j++) {
            Carte carte2 = (Carte) objects[j];
            int pivote = carte2.deplacement().getValeurDeplacement();
            if (pivote < pivot) {
                i++;
                Carte cartei = (Carte) objects[i];
                objects[i] = objects[j];
                objects[j] = cartei;
            }
        }
        Carte carteencore = (Carte) objects[i + 1];
        objects[i + 1] = objects[end];
        objects[end] = carteencore;
        return (i + 1);
    }

    void trierTableau(Object[] objects, int start, int end) {
        if (start < end) {
            int p = partition(objects, start, end);
            trierTableau(objects, start, p - 1);
            trierTableau(objects, p + 1, end);
        }
    }

    public void melangerDefausse() {
        while (!defausse.estVide()) {
            pioche.insereTete(defausse.extraitTete());
        }
        melanger();
    }

    public void distribuer() {
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            mainJoueurs[0][i] = pioche.extraitTete();
            mainJoueurs[1][i] = pioche.extraitTete();
        }
    }

    public void enleverCarte(int joueur, int carte) {
        Carte vide = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        Carte main = (Carte) mainJoueurs[joueur][carte];
        mainJoueurs[joueur][carte] = vide;
        tourActuel.insereQueue(main);
    }

    public void viderCartePosee() {
        while (!tourActuel.estVide()) {
            defausse.insereTete(tourActuel.extraitTete());
        }
    }

    public int trouverRoi(int joueur, int ignore){
        Carte roi = new Carte(ROI, UN);
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte main = (Carte) mainJoueurs[joueur][i];
            if (main.estIdentique(roi)) {
                if(ignore == 0){
                    return i;
                }else{
                    ignore = ignore - 1;
                }
            }
        }
        return 10;
    }

    public Carte[] carteSelonPerso(int joueur, Element voulu){
        Carte[] res = new Carte[NOMBRE_CARTE_EN_MAIN];
        Carte vide = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte main = (Carte) mainJoueurs[joueur][i];
            if (main.personnage() == voulu) {
                res[i] = main;
            }
            else{
                res[i] = vide;
            }
        }
        return res;
    }

    public int nombreCartesElement(int joueur, Element voulu, int ignore){
        int nbVoulu = 0;
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte main = (Carte) mainJoueurs[joueur][i];
            if (main.personnage() == voulu) {
                if(ignore != 0){
                    ignore--;
                }
                else{
                    nbVoulu++;
                }
            }
        }
        return nbVoulu;
    }

    public int nombreCarteManquante(int joueur) {
        Carte vide = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        int nbVide = 0;
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte main = (Carte) mainJoueurs[joueur][i];
            if (main.estIdentique(vide)) {
                nbVide++;
            }
        }
        return nbVide;
    }

    public boolean resteAssezCarteDansPioche(int i) {
        return pioche.taille() >= i;
    }

    public void remplirMain(int joueur) {
        Carte vide = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        for (int i = 0; i < NOMBRE_CARTE_EN_MAIN; i++) {
            Carte main = (Carte) mainJoueurs[joueur][i];
            if (main.estIdentique(vide)) {
                main = pioche.extraitTete();
                mainJoueurs[joueur][i] = main;
            }
        }
        trierJoueur(joueur);
    }

    public void creerPaquet() {
        pioche = Configuration.instance().nouvelleSequence();
        defausse = Configuration.instance().nouvelleSequence();
        mainJoueurs = new Object[NOMBRE_JOUEUR][NOMBRE_CARTE_EN_MAIN];
        creerCartesRoi();
        creerCartesGarde();
        creerCartesFou();
        creerCartesSorcier();
    }

    void creerCartesRoi() {
        Deplacement type = null;
        int nb = 0;
        for (int i = 0; i < NOMBRE_TYPE_CARTE_ROI; i++) {
            switch (i) {
                case 0:
                    type = UN;
                    nb = NOMBRE_CARTE_ROI_1;
                    break;

                default:
                    break;
            }
            for (int j = 0; j < nb; j++) {
                Carte carte = new Carte(ROI, type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesGarde() {
        Deplacement type = null;
        int nb = 0;
        for (int i = 0; i < NOMBRE_TYPE_CARTE_GARDES; i++) {
            switch (i) {
                case 0:
                    type = RAPPROCHE;
                    nb = NOMBRE_CARTE_GARDES_RAPPROCHE;
                    break;

                case 1:
                    type = UN;
                    nb = NOMBRE_CARTE_GARDES_1;
                    break;

                case 2:
                    type = UN_PLUS_UN;
                    nb = NOMBRE_CARTE_GARDES_1_PLUS_1;
                    break;

                default:
                    break;
            }
            for (int j = 0; j < nb; j++) {
                Carte carte = new Carte(GARDES, type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesFou() {
        Deplacement type = null;
        int nb = 0;
        for (int i = 0; i < NOMBRE_TYPE_CARTE_FOU; i++) {
            switch (i) {
                case 0:
                    type = MILIEU;
                    nb = NOMBRE_CARTE_FOU_MILIEU;
                    break;

                case 1:
                    type = UN;
                    nb = NOMBRE_CARTE_FOU_1;
                    break;

                case 2:
                    type = DEUX;
                    nb = NOMBRE_CARTE_FOU_2;
                    break;

                case 3:
                    type = TROIS;
                    nb = NOMBRE_CARTE_FOU_3;
                    break;

                case 4:
                    type = QUATRE;
                    nb = NOMBRE_CARTE_FOU_4;
                    break;

                case 5:
                    type = CINQ;
                    nb = NOMBRE_CARTE_FOU_5;
                    break;

                default:
                    break;
            }

            for (int j = 0; j < nb; j++) {
                Carte carte = new Carte(FOU, type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesSorcier() {
        Deplacement type = null;
        int nb = 0;
        for (int i = 0; i < NOMBRE_TYPE_CARTE_SORCIER; i++) {
            switch (i) {
                case 0:
                    type = UN;
                    nb = NOMBRE_CARTE_SORCIER_1;
                    break;
                case 1:
                    type = DEUX;
                    nb = NOMBRE_CARTE_SORCIER_2;
                    break;

                case 2:
                    type = TROIS;
                    nb = NOMBRE_CARTE_SORCIER_3;
                    break;

                default:
                    break;
            }
            for (int j = 0; j < nb; j++) {
                Carte carte = new Carte(SORCIER, type);
                pioche.insereTete(carte);
            }
        }
    }

    public Sequence<Carte> pioche() {
        return pioche;
    }

    public Sequence<Carte> defausse() {
        return defausse;
    }

    public Sequence<Carte> tourActuel() {
        return tourActuel;
    }

    public Carte[] mainJoueur(int joueur) {
        return copieTableau(joueur);
    }

    public int nombreCartesEnMain() {
        return NOMBRE_CARTE_EN_MAIN;
    }

    public Sequence<Carte> copieSequence(Sequence<Carte> copie) {
        Sequence<Carte> res = Configuration.instance().nouvelleSequence();
        Sequence<Carte> tmp = Configuration.instance().nouvelleSequence();
        Carte carte;
        while (!copie.estVide()) {
            carte = copie.extraitTete();
            res.insereQueue(carte);
            tmp.insereQueue(carte);
        }
        while (!tmp.estVide()) {
            copie.insereQueue(tmp.extraitTete());
        }
        return res;
    }

    public Carte[] copieTableau(int joueur) {
        int i = 0;
        Carte[] res = new Carte[NOMBRE_CARTE_EN_MAIN];
        while (i < NOMBRE_CARTE_EN_MAIN) {
            res[i] = (Carte) mainJoueurs[joueur][i];
            i++;
        }
        return res;
    }

    @Override
    public Paquet clone() {
        Paquet res = null;
        try {
            res = (Paquet) super.clone();
            res.pioche = copieSequence(pioche);
            res.defausse = copieSequence(defausse);
            res.mainJoueurs[0] = copieTableau(0);
            res.mainJoueurs[1] = copieTableau(1);
            return res;
        } catch (Exception e) {
            Configuration.instance().logger().severe("Echec du clone du paquet !!!!!!!!");
        }
        return res;
    }
}
