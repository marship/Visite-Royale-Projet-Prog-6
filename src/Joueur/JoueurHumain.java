package Joueur;

import Global.Deplacement;
import Global.Element;
import Global.InfoJeu;
import Modele.Carte;
import Modele.Jeu;

public class JoueurHumain extends Joueur {

	static final InfoJeu MENU_PRICIPAL = InfoJeu.MENU_PRINCIPAL;
	static final InfoJeu OPTIONS = InfoJeu.OPTIONS_MENU;
	static final InfoJeu DEBUT_TOUR = InfoJeu.DEBUT_TOUR;
	static final InfoJeu CHOIX_GARDE = InfoJeu.CHOIX_GARDE;
	static final InfoJeu CHOIX_FOU = InfoJeu.CHOIX_FOU;
	static final InfoJeu CHOIX_SORCIER = InfoJeu.CHOIX_SORCIER;
	static final InfoJeu CHOIX_ROI = InfoJeu.CHOIX_ROI;
	static final InfoJeu APRES_UNE_CARTE = InfoJeu.APRES_UNE_CARTE;
	static final InfoJeu JOUER_UNE_CARTE = InfoJeu.JOUER_UNE_CARTE;

	public JoueurHumain(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
	}

	@Override
	public boolean jeu(int caseChoisie, int positionCarteDansLaMain) {

		Carte carteChoisie = jeu.recupererMainJoueur(numeroJoueurCourant)[positionCarteDansLaMain];

		if (jeu.listeCarteJouable()[positionCarteDansLaMain] == 0) {
			return false;
		}

		if (carteChoisie.personnage() == Element.FOU) {
			if (jeu.personnageManipulerParLeFou == Element.ROI) {
				if (jeu.estPouvoirFouActivable()) {
					int[] a = jeu.listeDeplacementPossiblesAvecCarte(Element.FOU,
							carteChoisie.deplacement());
					if (a[caseChoisie + 8] == 0) {
						return false;
					}
				}
			}
			int[] a = jeu.listeDeplacementPossiblesAvecCarte(Element.FOU,
					carteChoisie.deplacement());
			if (a[caseChoisie + 8] == 0) {
				return false;
			}
		} else {
			if (jeu.listeDeplacementPossiblesAvecCarte(carteChoisie.personnage(),
					carteChoisie.deplacement())[caseChoisie + 8] == 0) {
				return false;
			}
		}

		if (carteChoisie.personnage() == Element.GARDES) {
			if (carteChoisie.deplacement() == Deplacement.RAPPROCHE) {
				jeu.rapproche(positionCarteDansLaMain);
			}
			if (carteChoisie.deplacement() == Deplacement.UN_PLUS_UN) {
				if (((caseChoisie - 2 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE))
						|| (caseChoisie + 2 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE)))
						&& jeu.obtenirPositionElement(Element.ROI) > caseChoisie) {
					jeu.jouerCarte(Element.GARDE_GAUCHE, caseChoisie, positionCarteDansLaMain);
				} else {
					if (((caseChoisie - 2 == jeu.obtenirPositionElement(Element.GARDE_DROIT))
							|| (caseChoisie + 2 == jeu.obtenirPositionElement(Element.GARDE_DROIT)))
							&& jeu.obtenirPositionElement(Element.ROI) < caseChoisie) {
						jeu.jouerCarte(Element.GARDE_DROIT, caseChoisie, positionCarteDansLaMain);
					} else {
						if ((caseChoisie - 1 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE))
								|| (caseChoisie - 1 == jeu.obtenirPositionElement(Element.GARDE_DROIT))) {
							jeu.unPlusUn(1, positionCarteDansLaMain);
						} else {
							jeu.unPlusUn(0, positionCarteDansLaMain);
						}
					}
				}
			}
			if (carteChoisie.deplacement() == Deplacement.UN) {
				if ((caseChoisie - 1 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE))
						|| (caseChoisie + 1 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE))) {
					jeu.jouerCarte(Element.GARDE_GAUCHE, caseChoisie, positionCarteDansLaMain);
				} else {
					jeu.jouerCarte(Element.GARDE_DROIT, caseChoisie, positionCarteDansLaMain);
				}
			}
			return true;
		}

		if (carteChoisie.personnage() == Element.FOU) {
			if (jeu.personnageManipulerParLeFou == Element.GARDES) {
				jeu.personnageManipulerParLeFou(Element.GARDE_GAUCHE);
				int[] a = jeu.listeDeplacementPossiblesAvecCarte(Element.FOU, carteChoisie.deplacement());
				if (a[caseChoisie + 8] == 1) {
					jeu.jouerCarte(Element.GARDE_GAUCHE, caseChoisie, positionCarteDansLaMain);
				} else {
					jeu.jouerCarte(Element.GARDE_DROIT, caseChoisie, positionCarteDansLaMain);
				}
				jeu.personnageManipulerParLeFou(Element.GARDES);
			} else {
				jeu.jouerCarte(jeu.personnageManipulerParLeFou, caseChoisie, positionCarteDansLaMain);
			}
		} else {
			jeu.jouerCarte(carteChoisie.personnage(), caseChoisie, positionCarteDansLaMain);
		}
		return true;
	}
}
