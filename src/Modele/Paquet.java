package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Structures.Sequence;

import java.util.Random;

import Global.*;
import Structures.Couple;
import Structures.FAP;
import Structures.FAPListe;

public class Paquet {
    Sequence<Carte> pioche, defausse;
    Object[][] mainJoueurs;

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

    static final Element VIDE_ELEMENT = Element.VIDE;
    static final Element ROI = Element.ROI;
    static final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;

    static final Deplacement VIDE_DEPLACEMENT = Deplacement.VIDE;
    static final Deplacement RAPPROCHE = Deplacement.RAPPROCHE;
    static final Deplacement UN = Deplacement.UN;
    static final Deplacement DEUX = Deplacement.DEUX;
    static final Deplacement TROIS = Deplacement.TROIS;
    static final Deplacement QUATRE = Deplacement.QUATRE;
    static final Deplacement CINQ = Deplacement.CINQ;
    static final Deplacement MILIEU = Deplacement.MILIEU;
    static final Deplacement UN_PLUS_UN = Deplacement.UN_PLUS_UN;

    public Paquet(){
        pioche = Configuration.instance().nouvelleSequence();
        defausse = Configuration.instance().nouvelleSequence();
        mainJoueurs = new Object[NOMBRE_JOUEUR][NOMBRE_CARTE_EN_MAIN];
        creerPaquet();
        afficherPioche();
        melanger();
        afficherPioche();
        distribuer();
        afficherPioche();
        trier();
    }

    public void afficherPioche(){
        Sequence<Carte> tmp = Configuration.instance().nouvelleSequence();
        while(!pioche.estVide()){
            tmp.insereTete(pioche.extraitTete());
        }
        while(!tmp.estVide()){
            Carte carte = tmp.extraitTete();
            System.out.println(carte.toString());
            pioche.insereTete(carte);
        }
    }

    public void melanger() {
        FAP<Couple<Carte, Integer>> fap = new FAPListe<>();
        Random r = new Random();
        while(!pioche.estVide()){
            fap.insere(new Couple<Carte, Integer>(pioche.extraitTete(), r.nextInt(10000)));
        }
        while(!fap.estVide()){
            pioche.insereTete(fap.extrait().e());
        }
    }

    public void trier(){
        for(int i = 0; i < NOMBRE_JOUEUR ; i++){
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
                    position = nbChaqueType[0][0] = nbChaqueType[0][0] + 1;
                    type = 0;
                    break;

                case SORCIER:
                    position = nbChaqueType[1][0] = nbChaqueType[1][0] + 1;
                    type = 1;
                    break;

                case GARDE_GAUCHE:
                    position = nbChaqueType[2][0] = nbChaqueType[2][0] + 1;
                    type = 2;
                    break;

                case ROI:
                    position = nbChaqueType[3][0] = nbChaqueType[3][0] + 1;
                    type = 3;
                    break;

                default:
                    break;

            }
            tableauTrier[type][position] = (Carte) carte;
        }
        for (int i = 0; i < NOMBRE_TYPE_CARTE; i++) {
            trierTableau(tableauTrier[i],nbChaqueType[i][0]);
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

        private void trierTableau(Object[] objects, int nombreCarte){
            for(int i = 1 ; i < nombreCarte ; i++) {
                int x = objects[i].deplacement.getValeurDeplacement;   //recupere la valeur de la carte de deplacement à l'indice i et la met dans x                      
                int j = i;
                while(j > 0 && objects[j - 1].deplacement.getValeurDeplacement > x){ // décaler les cartes qui ont un plus grand deplacement que la carte stockées dans x, en partant de j = i-1
                    objects[j] = objects[j - 1];
                    j = j - 1;
                }                                             
                objects[j].deplacement.getValeurDeplacement = x; // placer la carte stockée dans x dans le "trou" laissé par le décalage
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

    public void enleverCarte(int joueur, Carte carte) {
        int i = 0;
        Carte main = (Carte) mainJoueurs[joueur][i];
        while ((i + 1 != NOMBRE_CARTE_EN_MAIN) && !main.estIdentique(carte)) {
            i++;
            main = (Carte) mainJoueurs[joueur][i];
        }
        defausse.insereTete(main);
        main = new Carte(VIDE_ELEMENT, VIDE_DEPLACEMENT);
        mainJoueurs[joueur][i] = main;
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
        return NOMBRE_CARTE_EN_MAIN - nbVide;
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
    }

    public void creerPaquet() {
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
                Carte carte = new Carte(GARDE_GAUCHE, type);
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
}
