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

		System.out.println("#############################################################################");
		do {
			int choix = random.nextInt(100);
			// Jouer une carte
			if (choix < 50) {
				System.out.println("CHOIX CARTE");
				jouerCarte();
			}
			// Jouer deux roi
			if (choix >= 50 && choix < 65) {
				System.out.println("CHOIX ROI");
				jouerDeuxRoi();
			}
			// Activer le fou
			if (choix >= 65 && choix < 85) {
				if (nbActions == 0) {
					System.out.println("CHOIX FOU");
					activerFou();
				}
			}
			// Activer le sorcier
			if (choix >= 85) {
				if (nbActions == 0) {
					System.out.println("CHOIX SORCIER");
					activerSorcier();
				}
			}
		} while (!fin());
		nbActions = 0;
		System.out.println("#############################################################################");
		return true;
	}

	private void activerSorcier() {
		int kiTP = random.nextInt(10);
		if (kiTP < 2) {
			if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
				jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
				System.out.println("TP GARDE GAUCHE");
				nbActions = 10;
			}
		}
		if (kiTP >= 2 && kiTP < 5) {
			if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
				jeu.teleportationPouvoirSorcier(Element.ROI);
				System.out.println("TP ROI");
				nbActions = 10;
			}
		}
		if (kiTP >= 5 && kiTP < 7) {
			if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
				jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
				System.out.println("TP GARDE DROIT");
				nbActions = 10;
			}
		}
	}

	private void activerFou() {
		if (jeu.estPouvoirFouActivable()) {
			int choix = random.nextInt(10);
			if (choix < 2) {
				jeu.personnageManipulerParLeFou(Element.GARDE_GAUCHE);
				System.out.println("FOU GARDE GAUCHE");
			}
			if (choix >= 2 && choix < 4) {
				jeu.personnageManipulerParLeFou(Element.ROI);
				System.out.println("FOU ROI");
			}
			if (choix >= 4 && choix < 6) {
				jeu.personnageManipulerParLeFou(Element.GARDE_DROIT);
				System.out.println("FOU GARDE DROIT");
			}
			if (choix >= 6 && choix < 8) {
				jeu.personnageManipulerParLeFou(Element.SORCIER);
				System.out.println("FOU SORCIER");
			}
			if (choix >= 8) {
				jeu.personnageManipulerParLeFou(Element.FOU);
				System.out.println("FOU FOU");
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
				System.out.println("LA COUR A GAUCHE");
				nbActions++;
			}
			if (direction == 1 && (jeu.positionsPourCour() == 2 || jeu.positionsPourCour() == 0)) {
				jeu.deplacerCour(1, cartes);
				System.out.println("LA COUR A DROITE");
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
				if(carte.personnage() == Element.FOU){
					el = jeu.personnageManipulerParLeFou;
				}
				if (carte.deplacement() == Deplacement.RAPPROCHE) {
					jeu.rapproche(choix);
					System.out.println("DEPLACEMENT RAPPROCHE");
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
								System.out.println("DEPLACEMENT UN PLUS UN GAUCHE");
								nbActions++;
							}
						} else {
							if (jeu.validationDeplacement(Element.GARDE_GAUCHE, 1)
									&& jeu.validationDeplacement(Element.GARDE_DROIT, 1)) {
								jeu.unPlusUn(direction, choix);
								System.out.println("DEPLACEMENT UN PLUS UN DROIT");
								nbActions++;
							}
						}
					} else {
						int direction = random.nextInt(2);
						if (direction == 0) {
							if (jeu.validationDeplacement(el, -2)) {
								jeu.jouerCarte(el, jeu.obtenirPositionElement(el) - 2, choix);
								System.out.println("DEPLACEMENT - DEUX " + el);
								nbActions++;
							}
						} else {
							if (jeu.validationDeplacement(el, 2)) {
								jeu.jouerCarte(el, jeu.obtenirPositionElement(el) + 2, choix);
								System.out.println("DEPLACEMENT DEUX " + el);
								nbActions++;
							}
						}
					}
				}
				if (carte.deplacement() == Deplacement.MILIEU) {
					if (jeu.validationDeplacement(el, -jeu.obtenirPositionElement(el))) {
						jeu.jouerCarte(el, 0, choix);
						System.out.println("DEPLACEMENT MILIEU " + el);
						nbActions++;
					}
				} else {
					int direction = random.nextInt(2);
					if (direction == 0) {
						if (jeu.validationDeplacement(el, -carte.deplacement().getValeurDeplacement())) {
							jeu.jouerCarte(el,
									jeu.obtenirPositionElement(el) - carte.deplacement().getValeurDeplacement(), choix);
									System.out.println("DEPLACEMENT -" + carte.deplacement() + " " + el);
							nbActions++;
						}
					} else {
						if (jeu.validationDeplacement(el, carte.deplacement().getValeurDeplacement())) {
							jeu.jouerCarte(el,
									jeu.obtenirPositionElement(el) + carte.deplacement().getValeurDeplacement(), choix);
									System.out.println("DEPLACEMENT " + carte.deplacement() + " " + el);
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
			System.out.println("FAUSSE FIN");
			return false;
		}
		if (nbActions == 10) {
			System.out.println("FIN SORCIER");
			return true;
		}
		int fin = random.nextInt(100);
		System.out.println("FIN POSSIBLE");
		return fin < 40;
	}
}
