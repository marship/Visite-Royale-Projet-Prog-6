package Modele;

import Pattern.Observable;

public class Plateau extends Observable {

	boolean enCours;
	int[][] plateauDeJeu;
	int libre;
	int joueurCourant;

	public Plateau() {
		reset(3);
	}

	public void reset(int n) {
		plateauDeJeu = new int[n][n];
		libre = n*n;
		enCours = true;
		for (int i = 0; i < plateauDeJeu.length; i++)
			for (int j = 0; j < plateauDeJeu[0].length; j++)
            plateauDeJeu[i][j] = -1;
		joueurCourant = 0;
		metAJour();
	}

	public void jouer(int l, int c) {
		if (enCours && (plateauDeJeu[l][c] == -1)) {
			plateauDeJeu[l][c] = joueurCourant;
			libre--;
			boolean vertical = true, horizontal = true, slash = true, antiSlash = true;
			for (int p = 0; p < plateauDeJeu.length; p++) {
				horizontal = horizontal && (plateauDeJeu[l][p] == joueurCourant);
				vertical = vertical && (plateauDeJeu[p][c] == joueurCourant);
				slash = slash && (plateauDeJeu[p][p] == joueurCourant);
				antiSlash = antiSlash && (plateauDeJeu[p][plateauDeJeu.length - p - 1] == joueurCourant);
			}
			enCours = !(horizontal || vertical || slash || antiSlash) && (libre > 0);
			joueurCourant = 1 - joueurCourant;
			metAJour();
		}
	}

	public boolean libre(int i, int j) {
		return valeur(i, j) == -1;
	}

	public int valeur(int i, int j) {
		return plateauDeJeu[i][j];
	}

	public boolean enCours() {
		return enCours;
	}

	public int largeur() {
		return plateauDeJeu[0].length;
	}

	public int hauteur() {
		return plateauDeJeu.length;
	}

    public void changerJoueurCourant(int numeroJoueur, int typeJoueur) {
    
    }
}
