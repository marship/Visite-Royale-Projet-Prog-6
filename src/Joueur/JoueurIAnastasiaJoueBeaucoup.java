package Joueur;

import java.util.Random;

import Global.Configuration;
import Global.Element;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Structures.Sequence;

public class JoueurIAnastasiaJoueBeaucoup extends Joueur {

    Element jouee = Element.VIDE;

    public JoueurIAnastasiaJoueBeaucoup(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
	}

    void mettreLesPositions(int[] positions){
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    double coeffCartes(int[] cartes){
        double coeff = 1;
        int i = 0;
        int nb = 0;
        while(i < 8){
            if(cartes[i] == 1){
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

    void poserLesCartes(int[] cartes){
        int i = 0;
        while(i < 8){
            if(cartes[i] == 1){
                jouee = jeu.recupererMainJoueur(jeu.joueurCourant())[i].personnage();
                jeu.poserCarte(i);
            }
            i++;
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

        Evaluation eval = new Evaluation(jeu.plateau().clone());
		double noteMax = -10000;
        double noteCourrente;

        CoupleAtteindrePlateau winner = new CoupleAtteindrePlateau(positions, positions);
        CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);
        
        while(!liste.estVide()){
            test = liste.extraitTete();
            mettreLesPositions(test.positions());
            eval = new Evaluation(jeu.plateau().clone());
            noteCourrente = eval.note(jeu.joueurCourant()) * coeffCartes(test.cartes());
            if(noteCourrente == noteMax){
                lesWINNER.insereQueue(test);
            }
            if(noteCourrente > noteMax){
                lesWINNER = Configuration.instance().nouvelleSequence();
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
