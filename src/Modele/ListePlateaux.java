package Modele;

import java.util.Arrays;

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

    public ListePlateaux(Jeu j) {

        jeu = j;
        baseRoi = jeu.obtenirPositionElement(Element.ROI);
        baseGardeGauche = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        baseGardeDroit = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        baseFou = jeu.obtenirPositionElement(Element.FOU);
        baseSorcier = jeu.obtenirPositionElement(Element.SORCIER);
        
    }

    void remettreLesPositions(int[] positions){
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    int numeroPerso(Element perso){
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

        Sequence<CoupleAtteindrePlateau> res = Configuration.instance().nouvelleSequence();

        res = calculSorcier(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        res = calculFou(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        //res = calculRoi(res);
        //res = calculGardes(res);

        remettreLesPositions(positions);

        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculGardes(Sequence<CoupleAtteindrePlateau> res) {
        return null;
    }

    private Sequence<CoupleAtteindrePlateau> calculRoi(Sequence<CoupleAtteindrePlateau> res) {
        return null;
    }

    private Sequence<CoupleAtteindrePlateau> calculSorcier(Sequence<CoupleAtteindrePlateau> res, int[] positions, int[] cartesJouees) {

        int[] nouv;
        // Le pouvoir du sorcier
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[1] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }
        if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[0] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[2] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }

        // Jouer les cartes du sorcier
        res = calculNormal(res, positions, cartesJouees, Element.SORCIER, 4);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculFou(Sequence<CoupleAtteindrePlateau> res, int[] positions, int[] cartesJouees) {
        
        // Le pouvoir du fou
        if(jeu.estPouvoirFouActivable()){
            jeu.personnageManipulerParLeFou(Element.ROI);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.GARDES);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.SORCIER);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.FOU);
        }

        // Jouer les cartes du fou
        res = calculNormal(res, positions, cartesJouees, Element.FOU, 3);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculNormal(Sequence<CoupleAtteindrePlateau> res, int[] base, int[] cartes, Element perso, int numPerso) {
        int i = 0;
        while(i < 8){
            remettreLesPositions(base);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if(carte.personnage() == perso && cartes[i] != 1){
                int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(perso, carte.deplacement());
                int j = 0;
                while(j < 17){
                    if(deplacementPossibles[j] == 1){
                        int tmp = base[numPerso];
                        base[numPerso] = j - 8;
                        cartes[i] = 1;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                        res.insereQueue(couple);
                        calculNormal(res, base, cartes, perso, numPerso);
                        base[numPerso] = tmp;
                        cartes[i] = 0;
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculPouvoirFou(Sequence<CoupleAtteindrePlateau> res, int[] base, int[] cartes, Element perso, int numPerso) {
        int i = 0;
        while(i < 8){
            remettreLesPositions(base);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if(carte.personnage() == perso && cartes[i] != 1){
                int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(perso, carte.deplacement());
                int j = 0;
                while(j < 17){
                    if(deplacementPossibles[j] == 1){
                        int num = 8;
                        if(jeu.personnageManipulerParLeFou == Element.GARDES){
                            if(jeu.obtenirPositionElement(Element.ROI) > j - 8){
                                num = numeroPerso(Element.GARDE_GAUCHE);
                            }
                            else{
                                num = numeroPerso(Element.GARDE_DROIT);
                            }
                        }
                        else{
                            num = numeroPerso(jeu.personnageManipulerParLeFou);
                        }
                        int tmp = base[num];
                        base[num] = j - 8;
                        cartes[i] = 1;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                        res.insereQueue(couple);
                        calculPouvoirFou(res, base, cartes, perso, numPerso);
                        base[num] = tmp;
                        cartes[i] = 0;
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }
}
