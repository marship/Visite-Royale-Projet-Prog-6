package Joueur;

import java.util.Random;

import Global.Deplacement;
import Global.Element;
import Modele.Carte;
import Modele.Jeu;

public class JoueurIAAleatoire extends Joueur {

	Random random;
	int nbActions;

	public JoueurIAAleatoire(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
		random = new Random();
	}

	@Override
	public boolean tempsEcoule() {

		nbActions = 0;

		do {
			int choix = random.nextInt(100);
			// Jouer une carte
			if (choix < 50) {
				jouerCarte();
			}
			// Jouer deux roi
			if (choix >= 50 && choix < 65) {
				jouerDeuxRoi();
			}
			// Activer le fou
			if (choix >= 65 && choix < 85) {
				if (nbActions != 0) {
					activerFou();
				}
			}
			// Activer le sorcier
			if (choix >= 85) {
				if (nbActions != 0) {
					activerSorcier();
				}
			}
		} while (!fin());
		nbActions = 0;
		return true;
	}

	private void activerSorcier() {
		int kiTP = random.nextInt(10);
		if (kiTP < 2) {
			if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
				jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
				nbActions = 10;
			}
		}
		if (kiTP >= 2 && kiTP < 5) {
			if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
				jeu.teleportationPouvoirSorcier(Element.ROI);
				nbActions = 10;
			}
		}
		if (kiTP >= 5 && kiTP < 7) {
			if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
				jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
				nbActions = 10;
			}
		}
	}

	private void activerFou() {
		if (jeu.estPouvoirFouActivable()) {
			int choix = random.nextInt(10);
			if (choix < 2) {
				jeu.personnageManipulerParLeFou(Element.GARDE_GAUCHE);
			}
			if (choix >= 2 && choix < 4) {
				jeu.personnageManipulerParLeFou(Element.ROI);
			}
			if (choix >= 4 && choix < 6) {
				jeu.personnageManipulerParLeFou(Element.GARDE_DROIT);
			}
			if (choix >= 6 && choix < 8) {
				jeu.personnageManipulerParLeFou(Element.SORCIER);
			}
			if (choix >= 8) {
				jeu.personnageManipulerParLeFou(Element.FOU);
			}
		}
	}

	private void jouerDeuxRoi() {
		if (jeu.plateau().paquet.nombreCartesElement(numeroJoueurCourant, Element.ROI, 0) >= 2
				&& (jeu.dernierTypeDePersonnageJouer == Element.ROI
						|| jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
			int direction = random.nextInt(3);
			int[] cartes = new int[2];
			cartes[0] = jeu.plateau().paquet.trouverRoi(numeroJoueurCourant, 0);
			cartes[1] = jeu.plateau().paquet.trouverRoi(numeroJoueurCourant, 1);
			if (direction == 0 && (jeu.positionsPourCour() == 1 || jeu.positionsPourCour() == 0)) {
				jeu.deplacerCour(0, cartes);
				nbActions++;
			}
			if (direction == 1 && (jeu.positionsPourCour() == 2 || jeu.positionsPourCour() == 0)) {
				jeu.deplacerCour(1, cartes);
				nbActions++;
			}
		}
	}

	private void jouerCarte() {
		int choix = random.nextInt(10);
		if (choix < 8) {
			if (jeu.listeCarteJouable()[choix] != 0) {
				Carte carte = jeu.plateau().paquet.mainJoueur(numeroJoueurCourant)[choix];
				Element el = carte.personnage();
				if (carte.personnage() == Element.GARDES) {
					int garde = random.nextInt(2);
					if (garde == 0) {
						el = Element.GARDE_GAUCHE;
					} else {
						el = Element.GARDE_DROIT;
					}
				}
				if (carte.deplacement() == Deplacement.RAPPROCHE) {
					jeu.rapproche(choix);
					nbActions++;
				}
				if (carte.deplacement() == Deplacement.UN_PLUS_UN) {
					int taille = random.nextInt(2);
					if (taille == 1) {
						int direction = random.nextInt(2);
						if (direction == 0) {
							if (jeu.validationDeplacement(Element.GARDE_GAUCHE, -1)
									&& jeu.validationDeplacement(Element.GARDE_DROIT, -1)) {
								jeu.unPlusUn(direction, choix);
								nbActions++;
							}
						} else {
							if (jeu.validationDeplacement(Element.GARDE_GAUCHE, 1)
									&& jeu.validationDeplacement(Element.GARDE_DROIT, 1)) {
								jeu.unPlusUn(direction, choix);
								nbActions++;
							}
						}
					} else {
						int direction = random.nextInt(2);
						if (direction == 0) {
							if (jeu.validationDeplacement(el, -2)) {
								jeu.jouerCarte(el, jeu.obtenirPositionElement(el) - 2, choix);
								nbActions++;
							}
						} else {
							if (jeu.validationDeplacement(el, 2)) {
								jeu.jouerCarte(el, jeu.obtenirPositionElement(el) + 2, choix);
								nbActions++;
							}
						}
					}
				}
				if (carte.deplacement() == Deplacement.MILIEU) {
					if (jeu.validationDeplacement(el, -jeu.obtenirPositionElement(el))) {
						jeu.jouerCarte(el, 0, choix);
						nbActions++;
					}
				} else {
					int direction = random.nextInt(2);
					if (direction == 0) {
						if (jeu.validationDeplacement(el, -carte.deplacement().getValeurDeplacement())) {
							jeu.jouerCarte(el,
									jeu.obtenirPositionElement(el) - carte.deplacement().getValeurDeplacement(), choix);
							nbActions++;
						}
					} else {
						if (jeu.validationDeplacement(el, carte.deplacement().getValeurDeplacement())) {
							jeu.jouerCarte(el,
									jeu.obtenirPositionElement(el) + carte.deplacement().getValeurDeplacement(), choix);
							nbActions++;
						}
					}
				}
			} else {

			}
		}
	}

	private boolean fin() {
		if (nbActions == 0) {
			return false;
		}
		if (nbActions == 10) {
			return true;
		}
		int fin = random.nextInt(100);
		return fin < 40;
	}
}
