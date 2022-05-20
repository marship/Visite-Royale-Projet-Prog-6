package Joueur;

import Modele.Jeu;

// Classe commune à tous les joueurs : IA ou humain
// L'idée est que, en ayant la même interface, tous les joueurs sont traités de la même
// manière par le moteur de jeu. C'est plus simple et permet toutes les combinaisons.

// Tous les joueurs ont donc potentiellement la possibilité de :
// - provoquer une temporisation (utilisé dans une IA)
// - tenir compte d'une temporisation écoulée (utilisé dans une IA)
// - tenir compte d'un coup joué à la souris (utilisé par un joueur humain)
public abstract class Joueur {

	Jeu jeu;
	int numeroJoueurCourant;

	// Le joueur connait son numéro, cela lui permet d'inspecter le plateau en
	// sachant
	// repérer ses pions et évaluer où il en est
	Joueur(int nJoueurCourant, Jeu j) {
		numeroJoueurCourant = nJoueurCourant;
		jeu = j;
	}

	public int numeroJoueurCourant() {
		return numeroJoueurCourant;
	}

	// Méthode appelée pour tous les joueurs une fois le temps écoulé
	// Si un joueur n'est pas concerné, il lui suffit de l'ignorer
	public boolean tempsEcoule() {
		return false;
	}

	// Méthode appelée pour tous les joueurs lors d'un clic sur le plateau
	// Si un joueur n'est pas concerné, il lui suffit de l'ignorer
	public boolean jeu(int caseChoisie, int choixAction) {
        return false;
	}
}
