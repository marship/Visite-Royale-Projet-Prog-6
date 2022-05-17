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

    final int lenteurAttente = 50;

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
        if (carteActuelle != 8) {
            if (joueurs[joueurCourant][typeJoueur[joueurCourant]].jeu(coupY, carteActuelle)) {
                carteActuelle = 8;
            }
        } else {
            Element el = jeu.obtenirElementPosition(coupY);
            if (ETAT_JEU == InfoJeu.CHOIX_FOU) {
                switch (el) {
                    case ROI:
                    case SORCIER:
                        jeu.personnageManipulerParLeFou(el);
                        break;
                    case GARDE_GAUCHE:
                    case GARDE_DROIT:
                        jeu.personnageManipulerParLeFou(Element.GARDES);
                        break;
                    default:
                        jeu.personnageManipulerParLeFou(Element.FOU);
                        break;
                }
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                return;
            }
            if (ETAT_JEU == InfoJeu.CHOIX_SORCIER) {
                switch (el) {
                    case ROI:
                        if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
                            jeu.teleportationPouvoirSorcier(Element.ROI);
                            jeu.finDeTour();
                            changerJoueurCourant();
                        }
                        break;
                    case GARDE_GAUCHE:
                        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
                            jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
                            jeu.finDeTour();
                            changerJoueurCourant();
                        }
                        break;
                    case GARDE_DROIT:
                        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
                            jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
                            jeu.finDeTour();
                            changerJoueurCourant();
                        }
                        break;
                    default:
                        break;
                }
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                return;
            }
            if (ETAT_JEU == InfoJeu.CHOIX_ROI) {
                int possible = jeu.positionsPourCour();
                if (coupY - 8 == jeu.obtenirPositionElement(Element.ROI) - 1 && (possible == 1 || possible == 0)) {
                    int[] cartes = new int[2];
                    cartes[0] = jeu.plateau().paquet.trouverRoi(joueurCourant, 0);
                    cartes[1] = jeu.plateau().paquet.trouverRoi(joueurCourant, 1);
                    jeu.deplacerCour(0, cartes);
                }
                if (coupY - 8 == jeu.obtenirPositionElement(Element.ROI) + 1 && (possible == 1 || possible == 0)) {
                    int[] cartes = new int[2];
                    cartes[0] = jeu.plateau().paquet.trouverRoi(joueurCourant, 0);
                    cartes[1] = jeu.plateau().paquet.trouverRoi(joueurCourant, 1);
                    jeu.deplacerCour(1, cartes);
                }
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                return;
            }
            if (ETAT_JEU == InfoJeu.DEBUT_TOUR) {
                switch (el) {
                    case ROI:
                        if (jeu.plateau().paquet.nombreCartesElement(joueurCourant, Element.ROI, 0) >= 2
                                && (jeu.dernierTypeDePersonnageJouer == Element.ROI
                                        || jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
                            ETAT_JEU = InfoJeu.CHOIX_ROI;
                        }
                        break;
                    case FOU:
                        if (jeu.estPouvoirFouActivable()) {
                            ETAT_JEU = InfoJeu.CHOIX_FOU;
                        }
                        break;
                    case SORCIER:
                        ETAT_JEU = InfoJeu.CHOIX_SORCIER;
                        break;
                    default:
                        break;
                }
                return;
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

    @Override
    public void traqueSouris(int coupX, int coupY) {
    }

    @Override
    public void tictac() {
        if (jeu.actionAutoriser()) {
            if (decompteTimer == 0) {
                int type = typeJoueur[joueurCourant];
                // Lorsque le temps est écoulé on le transmet au joueur courant.
                // Si un coup a été joué (IA) on change de joueur.
                if (joueurs[joueurCourant][type].tempsEcoule()) {
                    changerJoueurCourant();
                } else {
                    // Sinon on indique au joueur qui ne réagit pas au temps (humain) qu'on
                    // l'attend.
                    System.out.println("On vous attend, joueur " + joueurs[joueurCourant][type].numeroJoueurCourant());
                    decompteTimer = lenteurAttente;
                }
            } else {
                decompteTimer--;
            }
        }
    }

    public void changerJoueurCourant() {
        joueurCourant = (joueurCourant + 1) % joueurs.length;
        decompteTimer = lenteurAttente;
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
                finDeTour();
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

    private void finDeTour() {
        if(jeu.dernierTypeDePersonnageJouer != Element.VIDE){
            jeu.finDeTour();
            ETAT_JEU = InfoJeu.DEBUT_TOUR;
            changerJoueurCourant();
        }
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
