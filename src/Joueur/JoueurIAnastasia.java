package Joueur;

import Global.Element;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Structures.Sequence;

public class JoueurIAnastasia extends Joueur {

    Element jouee = Element.VIDE;

    public JoueurIAnastasia(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
	}

    void mettreLesPositions(int[] positions){
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
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

        int[] positions = new int[5];
        positions[0] = jeu.obtenirPositionElement(Element.ROI);
        positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        positions[3] = jeu.obtenirPositionElement(Element.FOU);
        positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

        Evaluation eval = new Evaluation(jeu.plateau().clone());
		double noteMax = eval.note(jeu.joueurCourant());
        double noteCourrente;

        CoupleAtteindrePlateau winner = new CoupleAtteindrePlateau(positions, positions);
        CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);
        
        while(!liste.estVide()){
            test = liste.extraitTete();
            mettreLesPositions(test.positions());
            eval = new Evaluation(jeu.plateau().clone());
            noteCourrente = eval.note(jeu.joueurCourant());
            if(noteCourrente > noteMax){
                winner = test;
                noteMax = noteCourrente;
            }
        }

        mettreLesPositions(winner.positions());
        poserLesCartes(winner.cartes());

        jeu.majDernierTypeDePersonnageJouer(jouee);

        return true;
    }
}
