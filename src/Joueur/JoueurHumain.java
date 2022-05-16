package Joueur;

import Global.InfoJeu;
import Modele.Jeu;

public class JoueurHumain extends Joueur {

	public JoueurHumain(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
    }

	@Override
	void jeu(InfoJeu informationJeu, int choixAction) {
		// A adapter selon le jeu,
		// Un coup peut être constitué de plusieurs passages par cette fonction, ex :
		// - selection d'un pièce + surlignage des coups possibles
		// - selection de la destination
		// Autrement dit une machine à état peut aussi être gérée par un objet de cette
		// classe. Dans le cas du morpion, un clic suffit.

        switch (informationJeu) {
            // TODO
        
            default:
                break;
        }

	}
}
