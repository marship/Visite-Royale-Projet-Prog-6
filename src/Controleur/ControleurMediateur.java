package Controleur;

import Global.Element;
import Global.InfoJeu;
import Joueur.Joueur;
import Joueur.JoueurHumain;
import Joueur.JoueurIAAleatoire;
import Joueur.JoueurIAExperte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements {

    static final int TEMPS_ATTENTE = 50;
    static final int NOMBRE_JOUEUR = 2;
    static final int NOMBRE_TYPE_JOUEUR = 3;
    static InfoJeu ETAT_JEU = InfoJeu.DEBUT_TOUR;

    Jeu jeu;
    InterfaceUtilisateur interfaceUtilisateur;

    Joueur[][] joueurs;
    int[] typeJoueur;
    int joueurCourant;

    int carteActuelle; // Sert pour se souvenir de quelle carte est choisie pour le moment

    int decompteTimer;

    @Override
    public InfoJeu getInfoJeu() {
        return ETAT_JEU;
    }

    public ControleurMediateur(Jeu j) {
        jeu = j;
        joueurs = new Joueur[NOMBRE_JOUEUR][NOMBRE_TYPE_JOUEUR];
        typeJoueur = new int[NOMBRE_TYPE_JOUEUR];
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][0] = new JoueurHumain(i, jeu);
            joueurs[i][1] = new JoueurIAAleatoire(i, jeu);
            joueurs[i][2] = new JoueurIAExperte(i, jeu);
            typeJoueur[i] = 0;
        }
        carteActuelle = 8;
    }

    @Override
    public void changerJoueurCourant(int numeroJoueur, int typeDuJoueur) {
        System.out.println("Nouveau type " + typeJoueur + " pour le joueur " + numeroJoueur);
        typeJoueur[numeroJoueur] = typeDuJoueur;
    }

    @Override
    public void clicPlateau(int coupY) {
        if(carteActuelle != 8){
            if(joueurs[joueurCourant][typeJoueur[joueurCourant]].jeu(coupY, carteActuelle)){
                carteActuelle = 8;
            }
        }
        else{
            Element el = jeu.obtenirElementPosition(coupY);
            switch (el) {
                case ROI:
                    // TODO
                    break;
                case FOU:
                    // TODO
                    break;
                case SORCIER:
                    // TODO
                    break;
            
                default:
                    break;
            }
        }
    }

    @Override
    public void clicCarte(int coupY) {
        switch (ETAT_JEU) {
            case DEBUT_TOUR:
            case APRES_UNE_CARTE:
                if (carteActuelle == coupY) {
                    carteActuelle = 8;
                } else {
                    carteActuelle = coupY;
                }
                break;

            default:
                break;
        }

    }

    int recalculerPositionClic(int x, int y) { // TODO
        int choix = 0;
        switch (ETAT_JEU) {
            case DEBUT_TOUR:

                break;

            default:
                break;
        }
        return choix;

    }

    @Override
    public void traqueSouris(int coupX, int coupY) {
    }

    @Override
    public void tictac() {

    }

    public void changerJoueurCourant() {

    }

    @Override
    public boolean commande(String commande) {
        switch (commande) {
            case "Jouer":
                interfaceUtilisateur.afficherPanel("Jouer");
                break;
            case "MenuPrincipal":
                interfaceUtilisateur.afficherPanel("MenuPrincipal");
                break;
            case "Charger":
                break;
            case "Regles":
                break;
            case "Options":
                interfaceUtilisateur.afficherPanel("Options");
                break;
            case "Quitter":
                System.exit(0);
                break;
            case "FinDeTour":
                interfaceUtilisateur.miseAJourFinDeTour();
                break;
            case "MenuEnJeu":
                break;
            case "JouerCarte":
                break;
            case "PouvoirFou":
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void ajouteInterfaceUtilisateur(InterfaceUtilisateur interfaceU) {
        interfaceUtilisateur = interfaceU;
    }

    @Override
    public void choix(int choix) {
        // TODO Auto-generated method stub

    }

}
