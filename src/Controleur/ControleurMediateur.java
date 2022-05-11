package Controleur;

import Modele.Plateau;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {
	
    static final int TEMPS_ATTENTE = 50;

    Plateau plateau;
    InterfaceGraphique interfaceGraphique;

	// Joueur[][] joueurs;
	int [] typeJoueur;
	int joueurCourant;

	int decompteTimer;

    public ControleurMediateur() {

	}

	public ControleurMediateur(Plateau p) {

	}

	@Override
	public void clicSouris(int coupX, int coupY) {

	}

    @Override
    public void tictac() {
		
	}

    public void changerJoueurCourant() {
        
    }

    @Override
    public void changerJoueurCourant(int numeroJoueur, int typeJoueur) {
        
    }

	

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "Jouer":
                break;
            case "Charger":
                break;
            case "Regles":
                break;
            case "Options":
                break;
            case "Quitter":
                System.exit(0);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void fixerInterfaceGraphique(InterfaceGraphique interfaceGraphique) {

    }
}
