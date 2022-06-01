package Joueur;

import java.util.Random;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.Carte;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Structures.Sequence;

// Amel
public class JoueurIAExperte extends Joueur {

    Element jouee = Element.VIDE;
    int baseCouronne;

    public JoueurIAExperte(int numeroJoueurCourant, Jeu jeu) {
        super(numeroJoueurCourant, jeu);
    }

    void mettreLesPositions(int[] positions) {
        jeu.plateau().couronne.positionnerCouronne(baseCouronne);
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    double counterStrikePositif(int[] cartes){
        double coeff = 1;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        int i = 0;
        if(jeu.typeDePersonnageJouerAuDernierTour == Element.VIDE){
            jeu.typeDePersonnageJouerAuDernierTour = Element.SORCIER;
        }
        while(i < 8){
            if(cartes[i] == 1 && (main[i].personnage() == jeu.typeDePersonnageJouerAuDernierTour)){
                coeff = 1.2;
            }
            i++;
        }
        return coeff;
    }

    double counterStrikeNegatif(int[] cartes){
        double coeff = 1;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        int i = 0;
        while(i < 8){
            if(cartes[i] == 1 && (main[i].personnage() == jeu.typeDePersonnageJouerAuDernierTour)){
                coeff = 0.8;
            }
            i++;
        }
        return coeff;
    }

    double poidsDesCartesPositif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        boolean dejaMilieu = false;
        boolean dejaRapproche = false;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        while (i < 8) {
            if (cartes[i] == 1) {
                if(main[i].deplacement() == Deplacement.MILIEU){
                    if(!dejaMilieu){
                        dejaMilieu = true;
                        coeff = coeff * 0.7;
                    }
                    else{
                        return -100;
                    }
                }
                if(main[i].deplacement() == Deplacement.RAPPROCHE){
                    if(!dejaRapproche){
                        dejaRapproche = true;
                        coeff = coeff * 0.7;
                    }
                    else{
                        return -100;
                    }
                }
                if(main[i].deplacement() == Deplacement.CINQ){
                    coeff = coeff * 0.7;
                }
            }
            i++;
        }
        return coeff;
    }

    double poidsDesCartesNegatif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        boolean dejaMilieu = false;
        boolean dejaRapproche = false;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        while (i < 8) {
            if (cartes[i] == 1) {
                if(main[i].deplacement() == Deplacement.MILIEU){
                    if(!dejaMilieu){
                        dejaMilieu = true;
                        coeff = coeff * 1.3;
                    }
                    else{
                        return 100;
                    }
                }
                if(main[i].deplacement() == Deplacement.RAPPROCHE){
                    if(!dejaRapproche){
                        dejaRapproche = true;
                        coeff = coeff * 1.3;
                    }
                    else{
                        return 100;
                    }
                }
                if(main[i].deplacement() == Deplacement.CINQ){
                    coeff = coeff * 1.3;
                }
            }
            i++;
        }
        return coeff;
    }

    double coeffCartesPositif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        int nb = 0;
        while (i < 8) {
            if (cartes[i] == 1) {
                nb++;
            }
            i++;
        }
        switch (nb) {
            case 0:
                coeff = 1.6;
                break;

            case 1:
                coeff = 1.3;
                break;

            case 2:
                coeff = 1.6;
                break;

            case 3:
                coeff = 1.9;
                break;

            case 4:
                coeff = 2.2;
                break;

            default:
                coeff = 2.5;
                break;
        }
        return coeff;
    }

    double coeffCartesNegatif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        int nb = 0;
        while (i < 8) {
            if (cartes[i] == 1) {
                nb++;
            }
            i++;
        }
        switch (nb) {
            case 0:
                coeff = 1;
                break;

            case 1:
                coeff = 1.3;
                break;

            case 2:
                coeff = 1;
                break;

            case 3:
                coeff = 0.7;
                break;

            case 4:
                coeff = 0.4;
                break;

            default:
                coeff = 0.1;
                break;
        }
        return coeff;
    }

    void poserLesCartes(int[] cartes){
        int i = 0;
        while(i < 8){
            if(cartes[i] == 1){
                jouee = jeu.recupererMainJoueur(jeu.joueurCourant())[i].personnage();
                jeu.poserCarte(i);
            }
            i++;
        }
        if(jouee == Element.VIDE){
            jeu.teleportationFaite = true;
        }
    }

    @Override
    public boolean tempsEcoule() {

        ListePlateaux lP = new ListePlateaux(jeu);

        Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();
        Sequence<CoupleAtteindrePlateau> lesWINNER = Configuration.instance().nouvelleSequence();

        int[] positions = new int[5];
        positions[0] = jeu.obtenirPositionElement(Element.ROI);
        positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        positions[3] = jeu.obtenirPositionElement(Element.FOU);
        positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

        baseCouronne = jeu.getPositionCouronne();

        Evaluation eval = new Evaluation(jeu.plateau().clone());
        double noteMax = -10000;
        double noteCourrente;

        CoupleAtteindrePlateau winner = new CoupleAtteindrePlateau(positions, positions);
        CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);

        while (!liste.estVide()) {
            test = liste.extraitTete();
            mettreLesPositions(test.positions());
            jeu.deplacerCouronne(jeu.plateau().valeurDeplacementCouronne());
            eval = new Evaluation(jeu.plateau().clone());
            noteCourrente = eval.note(jeu.joueurCourant());
            if(noteCourrente < 0){
                noteCourrente = noteCourrente * coeffCartesNegatif(test.cartes()) * poidsDesCartesNegatif(test.cartes()) * counterStrikeNegatif(test.cartes());
            } 
            else{
                noteCourrente = noteCourrente * coeffCartesPositif(test.cartes()) * poidsDesCartesPositif(test.cartes()) * counterStrikePositif(test.cartes());
            }
            if (noteCourrente == noteMax) {
                lesWINNER.insereQueue(test);
            }
            if (noteCourrente > noteMax) {
                while(!lesWINNER.estVide()){
                    lesWINNER.extraitTete();
                }
                lesWINNER.insereQueue(test);
                noteMax = noteCourrente;
            }
        }

        Random r = new Random();
        int i = r.nextInt(lesWINNER.taille() + 1);
        do {
            winner = lesWINNER.extraitTete();
            i--;
        } while (i > 0);

        mettreLesPositions(winner.positions());
        poserLesCartes(winner.cartes());

        jeu.majDernierTypeDePersonnageJouer(jouee);

        return true;
    }
}
