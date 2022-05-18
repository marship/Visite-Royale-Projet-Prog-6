package Controleur;

import java.util.Random;

import Global.Configuration;
import Global.Element;
import Global.InfoJeu;
import Joueur.Joueur;
import Joueur.JoueurHumain;
import Joueur.JoueurIAAleatoire;
import Joueur.JoueurIAExperte;
import Modele.Jeu;
import Pattern.Observable;
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

    final int lenteurAttente = 200;

    int decompteTimer = lenteurAttente;

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
        }
        typeJoueur[0] = 0;
        typeJoueur[1] = 0;
        carteActuelle = 8;
        joueurCourant = jeu.joueurCourant();
    }

    @Override
    public void changerJoueurCourant(int numeroJoueur, int typeDuJoueur) {
        System.out.println("Nouveau type " + typeJoueur + " pour le joueur " + numeroJoueur);
        typeJoueur[numeroJoueur] = typeDuJoueur;
    }

    @Override
    public void clicPlateau(int coupX, int coupY) {
        if (carteActuelle != 8) {
            if (joueurs[joueurCourant][typeJoueur[joueurCourant]].jeu(coupX, carteActuelle)) {
                carteActuelle = 8;
            }
        } else {
            Element el = Element.VIDE;
            switch (coupY) {
                case 2:
                    if (jeu.obtenirPositionElement(Element.FOU) == coupX) {
                        el = Element.FOU;
                    }
                    break;
                case 3:
                    if (jeu.obtenirPositionElement(Element.ROI) == coupX) {
                        el = Element.ROI;
                    }
                    if (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) == coupX) {
                        el = Element.GARDE_GAUCHE;
                    }
                    if (jeu.obtenirPositionElement(Element.GARDE_DROIT) == coupX) {
                        el = Element.GARDE_DROIT;
                    }
                    break;
                case 4:
                    if (jeu.obtenirPositionElement(Element.SORCIER) == coupX) {
                        el = Element.SORCIER;
                    }
                    break;
            }
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
                            finDeTour();
                        }
                        break;
                    case GARDE_GAUCHE:
                        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
                            jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
                            finDeTour();
                        }
                        break;
                    case GARDE_DROIT:
                        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
                            jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
                            finDeTour();
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
                if (coupX == jeu.obtenirPositionElement(Element.ROI) - 1 && (possible == 1 || possible == 0)) {
                    int[] cartes = new int[2];
                    cartes[0] = jeu.plateau().paquet.trouverRoi(joueurCourant, 0);
                    cartes[1] = jeu.plateau().paquet.trouverRoi(joueurCourant, 1);
                    jeu.deplacerCour(0, cartes);
                }
                if (coupX == jeu.obtenirPositionElement(Element.ROI) + 1 && (possible == 2 || possible == 0)) {
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
                        if (jeu.estPouvoirFouActivable() && jeu.dernierTypeDePersonnageJouer == Element.VIDE) {
                            ETAT_JEU = InfoJeu.CHOIX_FOU;
                        }
                        break;
                    case SORCIER:
                        if (jeu.dernierTypeDePersonnageJouer == Element.VIDE) {
                            ETAT_JEU = InfoJeu.CHOIX_SORCIER;
                        }
                        break;
                    default:
                        break;
                }
                return;
            }
        }
    }

    @Override
    public void clicCarte(int coupX) {
        switch (ETAT_JEU) {
            case DEBUT_TOUR:
            case APRES_UNE_CARTE:
                if (carteActuelle == coupX) {
                    carteActuelle = 8;
                } else {
                    if (jeu.listeCarteJouable()[coupX] != 0) {
                        carteActuelle = coupX;
                        System.out.println(jeu.recupererMainJoueur(joueurCourant)[coupX].personnage() + " " + jeu.recupererMainJoueur(joueurCourant)[coupX].deplacement());
                    } else {
                        Configuration.instance().logger().info("Carte non identique");
                    }
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
                    finDeTour();
                } else {
                    // Sinon on indique au joueur qui ne réagit pas au temps (humain) qu'on
                    // l'attend.
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
                interfaceUtilisateur.afficherPanneau("Jouer");
                break;
            case "MenuPrincipal":
                interfaceUtilisateur.afficherPanneau("MenuPrincipal");
                break;
            case "Charger":
                break;
            case "Regles":
                break;
            case "Options":
                interfaceUtilisateur.afficherPanneau("Options");
                break;
            case "Quitter":
                System.exit(0);
                break;
            case "FinDeTour":
                finDeTour();
                break;
            case "MenuEnJeu":
                break;
            case "JouerCarte":
                break;
            case "PouvoirFou":
                break;
            case "pause":
                jeu.changerEtatPartie();
                break;
            default:
                return false;
        }
        return true;
    }

    private void finDeTour() {
        if (jeu.dernierTypeDePersonnageJouer != Element.VIDE || jeu.teleportationFaite == true) {
            jeu.finDeTour();
            ETAT_JEU = InfoJeu.DEBUT_TOUR;
            changerJoueurCourant();
            interfaceUtilisateur.miseAJourFinDeTour();
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
