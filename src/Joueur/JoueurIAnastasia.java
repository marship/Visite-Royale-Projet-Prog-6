package Joueur;

import Global.Configuration;
import Global.Element;
import Modele.Carte;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.TroupleAtteindrePlateau;
import Structures.Sequence;
import Vue.Evaluation;

public class JoueurIAnastasia extends Joueur {

    public JoueurIAnastasia(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
	}

    @Override
	public boolean tempsEcoule() {

        Sequence<Carte> pioche = jeu.plateau().paquet.copieSequence(jeu.plateau().paquet.pioche());
        ListePlateaux lP = new ListePlateaux(jeu.plateau());

        Sequence<TroupleAtteindrePlateau> liste = lP.constructionListePlateau();

        jeu.plateau().paquet.pioche = pioche;

        Evaluation eval = new Evaluation(jeu.plateau());
		Double noteMax = eval.note(jeu.joueurCourant());
        double noteCourrente;

        TroupleAtteindrePlateau winner = new TroupleAtteindrePlateau(null, null, null);
        TroupleAtteindrePlateau peutEtreWinner = new TroupleAtteindrePlateau(null, null, null);

        while(!liste.estVide()){
            peutEtreWinner = liste.extraitTete();
            eval = new Evaluation(peutEtreWinner.gPlateau());
            noteCourrente = eval.note(jeu.joueurCourant());
            if(noteCourrente > noteMax){
                winner = peutEtreWinner;
                noteMax = noteCourrente;
            }
        }

        jeu = new Jeu(jeu.plateau());
        int i = 0;
        while(i < 8){
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if(carte.personnage() == winner.gPerso() && winner.gCartes()[i].personnage() != Element.VIDE){
                jeu.poserCarte(i);
            }
            i++;
        }

        return true;
    }
}
