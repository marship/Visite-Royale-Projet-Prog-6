package Modele;

import java.util.Arrays;
import java.util.Hashtable;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Structures.Sequence;

public class ListePlateaux {

    Jeu jeu;
    Plateau base;
    int baseFou;
    int baseGardeGauche;
    int baseGardeDroit;
    int baseSorcier;
    int baseRoi;
    Carte[] mainBase;

    Sequence<CoupleAtteindrePlateau> res;
    Hashtable<String, Boolean> hashTable;

    public ListePlateaux(Jeu j) {

        jeu = j;
        baseRoi = jeu.obtenirPositionElement(Element.ROI);
        baseGardeGauche = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        baseGardeDroit = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        baseFou = jeu.obtenirPositionElement(Element.FOU);
        baseSorcier = jeu.obtenirPositionElement(Element.SORCIER);

        hashTable = new Hashtable<String, Boolean>();

    }

    void remettreLesPositions(int[] positions) {
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    int numeroPerso(Element perso) {
        switch (perso) {
            case ROI:
                return 0;
            case GARDE_GAUCHE:
                return 1;
            case GARDE_DROIT:
                return 2;
            case FOU:
                return 3;
            case SORCIER:
                return 4;
            default:
                return 42;
        }
    }

    public Sequence<CoupleAtteindrePlateau> constructionListePlateau() {

        int[] positions = new int[5];
        positions[0] = baseRoi;
        positions[1] = baseGardeGauche;
        positions[2] = baseGardeDroit;
        positions[3] = baseFou;
        positions[4] = baseSorcier;

        int[] cartesJouees = jeu.initialiserTableau(8, 0);

        res = Configuration.instance().nouvelleSequence();

        calculFou(Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        calculGardes(Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        calculSorcier(Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        calculRoi(Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));

        remettreLesPositions(positions);

        return res;
    }

    private void calculGardes(int[] positions, int[] cartesJouees) {
        // Jouer les cartes des gardes
        newGardes(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.GARDES, 0), 0, 0, "G");
    }

    private void calculRoi(int[] positions, int[] cartesJouees) {
        // Jouer les cartes du Roi en prenant en compte le fait d'en jouer deux
        int nbRoi = jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.ROI, 0);
        calculDeplaceRoi(positions, cartesJouees, nbRoi, 0);
    }

    private void calculSorcier(int[] positions, int[] cartesJouees) {

        // Le pouvoir du sorcier
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
            int tmpGG = positions[1];
            positions[1] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
            res.insereQueue(couple);
            positions[1] = tmpGG;
        }
        if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
            int tmpRoi = positions[0];
            positions[0] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
            res.insereQueue(couple);
            positions[0] = tmpRoi;
        }
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
            int tmpGD = positions[2];
            positions[2] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
            res.insereQueue(couple);
            positions[2] = tmpGD;
        }
        // Jouer les cartes du sorcier
        newSorcier(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.SORCIER, 0), 0, "S");
    }

    private void newSorcier(int[] positions, int[] cartesJouees, int nbSorcier, int nbJouer, String combi){
        remettreLesPositions(positions);
        if(nbSorcier > nbJouer){
            int numCarte = jeu.plateau().paquet.trouverEle(jeu.joueurCourant(), Element.SORCIER, nbJouer);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[numCarte];
            int tmpS = positions[4];
            switch (carte.deplacement()) {
                case UN:
                    combi = combi + "1";
                    break;
                case DEUX:
                    combi = combi + "2";
                    break;
                case TROIS:
                    combi = combi + "3";
                    break;
                default:
                    break;
            }
            String tmp = combi;
            cartesJouees[numCarte] = 1;
            nbJouer++;
            int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(Element.SORCIER, carte.deplacement());
            int j = 0;
            while (j < 17) {
                if (deplacementPossibles[j] == 1) {
                    if(j - 8 < positions[4]){
                        combi = combi + "G";
                    }
                    else{
                        combi = combi + "D";
                    }
                    if(!hashTable.containsKey(combi)){
                        positions[4] = j - 8;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                        res.insereQueue(couple);
                        newSorcier(positions, cartesJouees, nbSorcier, nbJouer, combi);
                        hashTable.put(combi, true);
                        positions[4] = tmpS;
                    }
                    combi = tmp;
                }
                j++;
            }
            // On ne la joue pas
            cartesJouees[numCarte] = 0;
            newSorcier(positions, cartesJouees, nbSorcier, nbJouer, combi);
        }
    }

    private void newGardes(int[] positions, int[] cartesJouees, int nbGardes, int nbJouer, int nbUnPlusUnFait, String combi){
        remettreLesPositions(positions);
        if(nbGardes > nbJouer){
            int numCarte = jeu.plateau().paquet.trouverEle(jeu.joueurCourant(), Element.GARDES, nbJouer);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[numCarte];
            int tmpGG = positions[1];
            int tmpGD = positions[2];
            cartesJouees[numCarte] = 1;
            nbJouer++;
            switch (carte.deplacement()) {
                case UN:
                    combi = combi + "1";
                    break;
                case UN_PLUS_UN:
                    combi = combi + "2";
                    break;
                case RAPPROCHE:
                    combi = combi + "R";
                    break;
                default:
                    break;
            }
            String tmp = combi;
            int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(Element.GARDES, carte.deplacement());
            int j = 0;
            boolean deja = false;
            boolean dejaD = false;
            boolean dejaG = false;
            while (j < 17) {
                if (deplacementPossibles[j] == 1) {
                    if (carte.deplacement() == Deplacement.RAPPROCHE && !deja) {
                        if(!hashTable.containsKey(combi)){
                            positions[1] = positions[0] - 1;
                            positions[2] = positions[0] + 1;
                            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                            res.insereQueue(couple);
                            newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                            positions[1] = tmpGG;
                            positions[2] = tmpGD;
                            deja = true;
                            hashTable.put(combi, true);
                        }
                    }

                    if (carte.deplacement() == Deplacement.UN_PLUS_UN) {

                        if (nbUnPlusUnFait < 9) {
                            nbUnPlusUnFait++;
                            // Garde Gauche, 2 deplacement
                            if ((positions[1] - 2 == j - 8 || positions[1] + 2 == j - 8) && (positions[0] > j - 8)) {
                                if(positions[1] - 2 == j - 8){
                                    combi = combi + "GG2";
                                }
                                else{
                                    combi = combi + "GD2";
                                }
                                if(!hashTable.containsKey(combi)){
                                    positions[1] = j - 8;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                    res.insereQueue(couple);
                                    newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                    positions[1] = tmpGG;
                                    nbUnPlusUnFait--;
                                    hashTable.put(combi, true);
                                }
                                combi = tmp;
                            }

                            // Garde Droit, 2 deplacement
                            if ((positions[2] - 2 == j - 8 || positions[2] + 2 == j - 8) && (positions[0] < j - 8)) {
                                if(positions[2] - 2 == j - 8){
                                    combi = combi + "DG2";
                                }
                                else{
                                    combi = combi + "DD2";
                                }
                                if(!hashTable.containsKey(combi)){
                                    positions[2] = j - 8;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                    res.insereQueue(couple);
                                    newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                    positions[2] = tmpGD;
                                    nbUnPlusUnFait--;
                                    hashTable.put(combi, true);
                                }
                                combi = tmp;
                            }

                            // Gardes, 1+1 à gauche
                            if ( (positions[1] - 1 == j - 8 || positions[2] - 1 == j - 8) && !dejaG) {
                                combi = combi + "BG";
                                if(!hashTable.containsKey(combi)){
                                    positions[1] = positions[1] - 1;
                                    positions[2] = positions[2] - 1;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                    res.insereQueue(couple);
                                    newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                    positions[1] = tmpGG;
                                    positions[2] = tmpGD;
                                    nbUnPlusUnFait--;
                                    hashTable.put(combi, true);
                                    dejaG = true;
                                }
                                combi = tmp;
                            }

                            // Gardes, 1+1 à droite
                            if ( (positions[1] + 1 == j - 8 || positions[2] + 1 == j - 8) && !dejaD ) {
                                combi = combi + "BD";
                                if(!hashTable.containsKey(combi)){
                                    positions[1] = positions[1] + 1;
                                    positions[2] = positions[2] + 1;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                    res.insereQueue(couple);
                                    newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                    positions[1] = tmpGG;
                                    positions[2] = tmpGD;
                                    nbUnPlusUnFait--;
                                    hashTable.put(combi, true);
                                    dejaG = true;
                                }
                                combi = tmp;
                            }
                        }
                    }
                    if (carte.deplacement() == Deplacement.UN) {
                        // Le deplacement se fait sur le garde gauche
                        if (positions[1] - 1 == j - 8 || positions[1] + 1 == j - 8) {
                            if(positions[1] - 1 == j - 8){
                                combi = combi + "GG";
                            }
                            else{
                                combi = combi + "GD";
                            }
                            if(!hashTable.containsKey(combi)){
                                positions[1] = j - 8;
                                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                res.insereQueue(couple);
                                newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                hashTable.put(combi, true);
                                positions[1] = tmpGG;
                            }
                            combi = tmp;
                        }
                        // Le déplacement se fait sur le garde droit
                        else {
                            if(positions[2] - 1 == j - 8){
                                combi = combi + "DG";
                            }
                            else{
                                combi = combi + "DD";
                            }
                            if(!hashTable.containsKey(combi)){
                                positions[2] = j - 8;
                                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                                res.insereQueue(couple);
                                newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
                                hashTable.put(combi, true);
                                positions[2] = tmpGD;
                            }
                            combi = tmp;
                        }
                    }
                }
                j++;
            }
            // On ne la joue pas
            cartesJouees[numCarte] = 0;
            newGardes(positions, cartesJouees, nbGardes, nbJouer, nbUnPlusUnFait, combi);
        }
    }

    private void newFou(int[] positions, int[] cartesJouees, int nbFou, int nbJouer, String combi){
        remettreLesPositions(positions);
        if(nbFou > nbJouer){
            int numCarte = jeu.plateau().paquet.trouverEleInverse(jeu.joueurCourant(), Element.FOU, nbJouer);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[numCarte];
            cartesJouees[numCarte] = 1;
            nbJouer++;
            switch (carte.deplacement()) {
                case UN:
                    combi = combi + "1";
                    break;
                case DEUX:
                    combi = combi + "2";
                    break;
                case TROIS:
                    combi = combi + "3";
                    break;
                case QUATRE:
                    combi = combi + "4";
                    break;
                case CINQ:
                    combi = combi + "5";
                    break;
                case MILIEU:
                    combi = combi + "M";
                    break;
                default:
                    break;
            }
            String tmp = combi;
            int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(Element.FOU, carte.deplacement());
            int j = 0;
            while (j < 17) {
                if (deplacementPossibles[j] == 1) {
                    int numPerso = 8;
                    if (jeu.personnageManipulerParLeFou == Element.GARDES) {
                        if (jeu.obtenirPositionElement(Element.ROI) > j - 8) {
                            numPerso = numeroPerso(Element.GARDE_GAUCHE);
                            combi = combi + "G";
                        } else {
                            numPerso = numeroPerso(Element.GARDE_DROIT);
                            combi = combi + "D";
                        }
                    } else {
                        numPerso = numeroPerso(jeu.personnageManipulerParLeFou);
                    }
                    int tmpF = positions[numPerso];
                    if(j - 8 < tmpF){
                        combi = combi + "G";
                    }
                    else{
                        combi = combi + "D";
                    }
                    if(!hashTable.containsKey(combi)){
                        positions[numPerso] = j - 8;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(positions, cartesJouees);
                        res.insereQueue(couple);
                        newFou(positions, cartesJouees, nbFou, nbJouer, combi);
                        hashTable.put(combi, true);
                        positions[numPerso] = tmpF;
                    }
                    combi = tmp;
                }
                j++;
            }   

            // On ne joue pas la carte
            cartesJouees[numCarte] = 0;
            newFou(positions, cartesJouees, nbFou, nbJouer, combi);
        }
    }

    private void calculFou(int[] positions, int[] cartesJouees) {

        // Le pouvoir du fou
        if (jeu.estPouvoirFouActivable()) {
            jeu.personnageManipulerParLeFou(Element.ROI);
            newFou(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.FOU, 0), 0, "FR");
            jeu.personnageManipulerParLeFou(Element.GARDES);
            newFou(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.FOU, 0), 0, "FG");
            jeu.personnageManipulerParLeFou(Element.SORCIER);
            newFou(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.FOU, 0), 0, "FS");
            jeu.personnageManipulerParLeFou(Element.FOU);
        }

        // Jouer les cartes du fou
        newFou(positions, cartesJouees, jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.FOU, 0), 0, "FF");
    }

    private void calculDeplaceRoi(int[] base, int[] cartes, int nbRoi, int nbJouee) {
        remettreLesPositions(base);
        int tmpR = base[0];
        int tmpGG = base[1];
        int tmpGD = base[2];

        // Jouer deux cartes pour bouger la cour
        if (nbRoi >= 2) {
            if ( (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8
                    || jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) && 
                    (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8
                    && jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) ) {

                // Deplacement à gauche possible
                if (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8) {

                    // On met les nouvelle positions
                    base[0] = base[0] + 1;
                    base[1] = base[1] + 1;
                    base[2] = base[2] + 1;

                    // On note les cartes utilisées
                    int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte1] = 1;
                    nbJouee++;
                    nbRoi--;
                    int carte2 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte2] = 1;
                    nbJouee++;
                    nbRoi--;

                    // On ajoute ce plateau à la liste
                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                    res.insereQueue(couple);

                    // On va tester de continuer les déplacements
                    calculDeplaceRoi(base, cartes, nbRoi, nbJouee);

                    // On remet comme avant
                    base[0] = tmpR;
                    base[1] = tmpGG;
                    base[2] = tmpGD;
                    cartes[carte1] = 0;
                    cartes[carte2] = 0;
                    nbJouee = nbJouee - 2;
                    nbRoi = nbRoi + 2;
                }

                // Deplacement à droite possible
                if (jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) {
                    // On met les nouvelle positions
                    base[0] = base[0] - 1;
                    base[1] = base[1] - 1;
                    base[2] = base[2] - 1;

                    // On note les cartes utilisées
                    int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte1] = 1;
                    nbJouee++;
                    nbRoi--;
                    int carte2 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte2] = 1;
                    nbJouee++;
                    nbRoi--;

                    // On ajoute ce plateau à la liste
                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                    res.insereQueue(couple);

                    // On va tester de continuer les déplacements
                    calculDeplaceRoi(base, cartes, nbRoi, nbJouee);

                    // On remet comme avant
                    base[0] = tmpR;
                    base[1] = tmpGG;
                    base[2] = tmpGD;
                    cartes[carte1] = 0;
                    cartes[carte2] = 0;
                    nbJouee = nbJouee - 2;
                    nbRoi = nbRoi + 2;
                }
            }
        }

        // Jouer une carte pour bouger le roi
        if (nbRoi >= 1) {

            // Deplacement à gauche possible
            if (base[1] != (base[0] - 1)) {

                // On note la nouvelle position
                base[0] = base[0] - 1;

                // On note la carte jouée
                int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                cartes[carte1] = 1;
                nbJouee++;
                nbRoi--;

                // On ajoute ce plateau à la liste
                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                res.insereQueue(couple);

                // On va tester de continuer les déplacements
                calculDeplaceRoi(base, cartes, nbRoi, nbJouee);

                // On remet comme avant
                base[0] = tmpR;
                cartes[carte1] = 0;
                nbJouee--;
                nbRoi++;
            }

            // Deplacement à droite possible
            if (base[2] != (base[0] + 1)) {

                // On note la nouvelle position
                base[0] = base[0] + 1;

                // On note la carte jouée
                int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                cartes[carte1] = 1;
                nbJouee++;
                nbRoi--;

                // On ajoute ce plateau à la liste
                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                res.insereQueue(couple);

                // On va tester de continuer les déplacements
                calculDeplaceRoi(base, cartes, nbRoi, nbJouee);

                // On remet comme avant
                base[0] = tmpR;
                cartes[carte1] = 0;
                nbJouee--;
                nbRoi++;
            }
        }
    }
}
