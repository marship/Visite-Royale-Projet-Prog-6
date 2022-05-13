package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

public class ControleurMediateur implements CollecteurEvenements {
	
    static final int TEMPS_ATTENTE = 50;

    Jeu jeu;
    InterfaceGraphique interfaceGraphique;

	// Joueur[][] joueurs;
	int [] typeJoueur;
	int joueurCourant;

	int decompteTimer;

	public ControleurMediateur(Jeu j) {

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
                interfaceGraphique.afficher_panel("Jouer");
                break;
            case "MenuPrincipal":
                interfaceGraphique.afficher_panel("MenuPrincipal");
            break;
            case "Charger":
                break;
            case "Regles":
                break;
            case "Options":
                interfaceGraphique.afficher_panel("Options");
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
    public void getInterfaceGraphique(InterfaceGraphique interfaceG) {
        interfaceGraphique = interfaceG;
    }
}
