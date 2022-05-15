package Joueur;

import java.util.Random;

import Global.Element;
import Modele.Carte;
import Modele.Plateau;

class JoueurIA extends Joueur {

	Random random;
	int nbActions;

	JoueurIA(int numeroJoueurCourant, Plateau plateau) {
		super(numeroJoueurCourant, plateau);
		random = new Random();
	}

	@Override
	boolean tempsEcoule() {
		// Pour cette IA, on selectionne aléatoirement une case libre
        
		int coupX, coupY;
		nbActions = 0;

		do {
			int choix = random.nextInt(100);
			// Jouer une carte
			if(choix < 50){
				jouerCarte();
			}
			// Jouer deux roi
			if(choix >= 50 && choix < 65){
				jouerDeuxRoi();
			}
			// Activer le fou
			if(choix >= 65 && choix < 85){
				activerFou();
			}
			// Activer le sorcier
			if(choix >= 85){
				activerSorcier();
			}
		} while (!fin());
		nbActions = 0;

		return true;
	}

	private void activerSorcier() {
		int kiTP = random.nextInt(10);
		if(kiTP < 2){
			if(estPouvoirSorcierActivable(Element.GARDE_GAUCHE)){
				teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
				nbActions = 10;
			}
		}
		if(kiTP >= 2 && kiTP < 5){
			if(estPouvoirSorcierActivable(Element.ROI)){
				teleportationPouvoirSorcier(Element.ROI);
				nbActions = 10;
			}
		}
		if(kiTP >= 5 && kiTP < 7){
			if(estPouvoirSorcierActivable(Element.GARDE_DROIT)){
				teleportationPouvoirSorcier(Element.GARDE_DROIT);
				nbActions = 10;
			}
		}
	}

	private void activerFou() {
		if(estPouvoirFouActivable()){
			int choix = random.nextInt(10);
			if(choix < 2){
				personnageManipulerParLeFou(Element.GARDE_GAUCHE);
			}
			if(choix >= 2 && choix < 4){
				personnageManipulerParLeFou(Element.ROI);
			}
			if(choix >= 4 && choix < 6){
				personnageManipulerParLeFou(Element.GARDE_DROIT);
			}
			if(choix >= 6 && choix < 8){
				personnageManipulerParLeFou(Element.SORCIER);
			}
			if(choix >= 8){
				personnageManipulerParLeFou(Element.FOU);
			}
		}
	}

	private void jouerDeuxRoi() {
		if(plateau.paquet.nombreCartesElement(numeroJoueurCourant, Element.ROI, 0 ) >= 2){
			int direction = random.nextInt(3);
			int[] cartes = new int[2];
			cartes[0] = plateau.paquet.trouverRoi(numeroJoueurCourant, 0);
			cartes[1] = plateau.paquet.trouverRoi(numeroJoueurCourant, 1);
			if(direction == 0 && (positionsPourCour() == 1 || positionsPourCour() == 0){
				deplacerCour(0, cartes);
				nbActions++;
			}
			if(direction == 1 && (positionsPourCour() == 2 || positionsPourCour() == 0){
				deplacerCour(1, cartes);
				nbActions++;
			}
		}
	}

	private void jouerCarte(){
		int choix = random.nextInt(10);
		if(choix < 8){
			if(listeCarteJouable()[choix] != 0){
				Carte carte = plateau.paquet.mainJoueur(numeroJoueurCourant)[choix];
				Element el = carte.personnage();
				if(carte.personnage() == Element.GARDES){
					int garde = random.nextInt(2);
					if(garde == 0){
						el = Element.GARDE_GAUCHE;
					}
					else{
						el = Element.GARDE_DROIT;
					}
				}
				int[] liste = listeDeplacementPossiblesAvecCarte(el, carte.deplacement());
				
			}
		}


	}

	private boolean fin(){
		if(nbActions == 0){
			return false;
		}
		if(nbActions == 10){
			return true;
		}
		int fin = random.nextInt(100);
		return fin < 40;
	}
}