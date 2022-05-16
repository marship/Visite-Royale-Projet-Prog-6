package Joueur;

import Modele.Jeu;

class JoueurHumain extends Joueur {

	JoueurHumain(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
	}

	@Override
	boolean jeu(int coupX, int coupY) {
		// A adapter selon le jeu,
		// Un coup peut être constitué de plusieurs passages par cette fonction, ex :
		// - selection d'un pièce + surlignage des coups possibles
		// - selection de la destination
		// Autrement dit une machine à état peut aussi être gérée par un objet de cette
		// classe. Dans le cas du morpion, un clic suffit.
        /*
		if (plateau.libre(coupX, coupY)) {
			plateau.jouer(coupX, coupY);
			return true;
		} else {
			return false;
		}
        */
        return false;
	}
}
