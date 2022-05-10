package Modele;

import Structures.Sequence;
import Global.Configuration;

public class Paquet {
    Sequence<Carte> pioche, defausse;
    Object[][] mainJoueurs;

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
    

    Paquet(){
        pioche = Configuration.instance().nouvelleSequence();
        defausse = Configuration.instance().nouvelleSequence();
        mainJoueurs = new Object[NOMBRE_JOUEUR][NOMBRE_CARTE_EN_MAIN];
        creerPaquet();
        pioche.melanger();
        distribuer();
        trier();
    }

    public void trier(){
        // TO DO
    }

    public void distribuer(){
        for(int i = 0 ; i < NOMBRE_CARTE_EN_MAIN ; i++){
            mainJoueurs[0][i] = pioche.extraitTete();
            mainJoueurs[1][i] = pioche.extraitTete();
        }
    }

    public void enleverCarte(int joueur, Carte carte){
        int i = 0;
        Carte main = (Carte) mainJoueurs[joueur][i];
        while( (i+1 != NOMBRE_CARTE_EN_MAIN) && !main.estIdentique(carte)){
            i++;
            main = (Carte) mainJoueurs[joueur][i];
        }
        main = new Carte("Vide", "0");
        mainJoueurs[joueur][i] = main;
    }

    public int nombreCarteManquante(int joueur){
        Carte vide = new Carte("Vide", "0");
        int nbVide = 0;
        for(int i = 0 ; i < NOMBRE_CARTE_EN_MAIN ; i++){
            Carte main = (Carte) mainJoueurs[joueur][i];
            if(main.estIdentique(vide)){
                nbVide++;
            }
        }
        return NOMBRE_CARTE_EN_MAIN - nbVide;
    }

    public boolean resteAssezCarteDansPioche(int i){
        return pioche.nombre() >= i;
    }

    public void remplirMain(int joueur){
        Carte vide = new Carte("Vide", "0");
        for(int i = 0 ; i < NOMBRE_CARTE_EN_MAIN ; i++){
            Carte main = (Carte) mainJoueurs[joueur][i];
            if(main.estIdentique(vide)){
                main = pioche.extraitTete();
                mainJoueurs[joueur][i] = main;
            }
        }
    }

    public void creerPaquet(){
        creerCartesRoi();
        creerCartesGarde();
        creerCartesFou();
        creerCartesSorcier();
    }

    void creerCartesRoi(){
        String type = "";
        int nb = 0;
        for(int i = 0 ; i < NOMBRE_TYPE_CARTE_ROI ; i++){
            switch (i) {
                case 0:
                    type = "1";
                    nb = NOMBRE_CARTE_ROI_1;
                    break;

                default:
                    break;
            }
            for(int j = 0; j < nb; j++){
                Carte carte = new Carte("Roi", type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesGarde(){
        String type = "";
        int nb = 0;
        for(int i = 0 ; i < NOMBRE_TYPE_CARTE_GARDES ; i++){
            switch (i) {
                case 0:
                    type = "Rapproche";
                    nb = NOMBRE_CARTE_GARDES_RAPPROCHE;
                    break;

                case 1:
                    type = "1";
                    nb = NOMBRE_CARTE_GARDES_1;
                    break;

                case 2:
                    type = "1Plus1";
                    nb = NOMBRE_CARTE_GARDES_1_PLUS_1;
                    break;
            
                default:
                    break;
            }
            for(int j = 0; j < nb; j++){
                Carte carte = new Carte("Garde", type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesFou(){
        String type = "";
        int nb = 0;
        for(int i = 0 ; i < NOMBRE_TYPE_CARTE_FOU ; i++){
            switch (i) {
                case 0:
                    type = "Milieu";
                    nb = NOMBRE_CARTE_FOU_MILIEU;
                    break;

                case 1:
                    type = "1";
                    nb = NOMBRE_CARTE_FOU_1;
                    break;

                case 2:
                    type = "2";
                    nb = NOMBRE_CARTE_FOU_2;
                    break;

                case 3:
                    type = "3";
                    nb = NOMBRE_CARTE_FOU_3;
                    break;

                case 4:
                    type = "4";
                    nb = NOMBRE_CARTE_FOU_4;
                    break;

                case 5:
                    type = "5";
                    nb = NOMBRE_CARTE_FOU_5;
                    break;
            
                default:
                    break;
            }

            for(int j = 0; j < nb; j++){
                Carte carte = new Carte("Fou", type);
                pioche.insereTete(carte);
            }
        }
    }

    void creerCartesSorcier(){
        String type = "";
        int nb = 0;
        for(int i = 0 ; i < NOMBRE_TYPE_CARTE_SORCIER ; i++){
            switch (i) {
                case 0:
                    type = "1";
                    nb = NOMBRE_CARTE_SORCIER_1;
                    break;
                case 1:
                    type = "2";
                    nb = NOMBRE_CARTE_SORCIER_2;
                    break;

                case 2:
                    type = "3";
                    nb = NOMBRE_CARTE_SORCIER_3;
                    break;
            
                default:
                    break;
            }
            for(int j = 0; j < nb; j++){
                Carte carte = new Carte("Sorcier", type);
                pioche.insereTete(carte);
            }
        }
    }
}
