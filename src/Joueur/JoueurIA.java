package Joueur;

import java.util.Random;
import Modele.Plateau;

class JoueurIA extends Joueur {

	Random random;

	JoueurIA(int numeroJoueurCourant, Plateau plateau) {
		super(numeroJoueurCourant, plateau);
		random = new Random();
	}

	@Override
	boolean tempsEcoule() {
		// Pour cette IA, on selectionne aléatoirement une case libre
        /*
		int coupX, coupY;

		coupX = random.nextInt(plateau.hauteur());
		coupY = random.nextInt(plateau.largeur());
		while (!plateau.libre(coupX, coupY)) {
			coupX = random.nextInt(plateau.hauteur());
			coupY = random.nextInt(plateau.largeur());
		}
		plateau.jouer(coupX, coupY);
        */
		return true;
	}
}