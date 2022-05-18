package Controleur;

import Global.Configuration;
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

    // ==================
    // ===== JOUEUR =====
    // ==================
    static final int JOUEUR_GAUCHE = 0;
    static final int JOUEUR_DROIT = 1;
    static final int NOMBRE_JOUEUR = 2;
    static final int NOMBRE_TYPE_JOUEUR = 3;

    static final int JOUEUR_HUMAIN = 0;
    static final int JOUEUR_IAALEATOIRE = 1;
    static final int JOUEUR_IAEXPERTE = 2;

    // =================
    // ===== TIMER =====
    // =================
    static final int LENTEUR_ATTENTE = 100;
    
    // ====================
    // ===== ETAT JEU =====
    // ====================
    static InfoJeu ETAT_JEU = InfoJeu.DEBUT_TOUR;

    Jeu jeu;
    InterfaceUtilisateur interfaceUtilisateur;

    Joueur[][] joueurs;
    int[] typeJoueur;
    int joueurCourant;

    int decompteTimer = LENTEUR_ATTENTE;
    int carteActuelle; // Quelle carte est choisie actuellement

    int valeurLargeurPrevisualisation = 0;
    int valeurHauteurPrevisualisation = 0;

    int debutZoneCartesX = 0;
    int debutZoneCartesY = 0;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public ControleurMediateur(Jeu j) {
        jeu = j;
        joueurs = new Joueur[NOMBRE_JOUEUR][NOMBRE_TYPE_JOUEUR];
        typeJoueur = new int[NOMBRE_TYPE_JOUEUR];

        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][JOUEUR_HUMAIN] = new JoueurHumain(i, jeu);
            joueurs[i][JOUEUR_IAALEATOIRE] = new JoueurIAAleatoire(i, jeu);
            joueurs[i][JOUEUR_IAEXPERTE] = new JoueurIAExperte(i, jeu);
        }

        choixTypeJoueur(JOUEUR_GAUCHE, JOUEUR_HUMAIN);
        choixTypeJoueur(JOUEUR_DROIT, JOUEUR_HUMAIN);
        
        carteActuelle = 8;
        joueurCourant = jeu.joueurCourant();
    }

    void choixTypeJoueur(int joueur, int typeDuJoueur) {
        if (jeu.numeroJoueurValide(joueur)) {
            typeJoueur[joueur] = typeDuJoueur;
        }
    }

    @Override
    public InfoJeu getInfoJeu() {
        return ETAT_JEU;
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
    public void traquePlateau(int coupX, int coupY) {
        // TODO Auto-generated method stub
    }

    @Override
    public void traqueCarte(int positionSourisX, int positionSourisY) {
        switch (ETAT_JEU) {
            case DEBUT_TOUR:
            case APRES_UNE_CARTE:
                //System.out.println("X: " + positionSourisX + ", Y: " + positionSourisY);
                // gestionPrevisualisationCoup(positionSourisX, positionSourisY);
                break;
            default:
                break;
        }
    }

    @Override
    public void setDebutZoneCartesX(int dZoneCartesX) {
        valeurHauteurPrevisualisation = dZoneCartesX;
    }

    @Override
    public void setDebutZoneCartesY(int dZoneCartesY) {
        valeurHauteurPrevisualisation = dZoneCartesY;
    }

    @Override
    public void setValeurHauteurPrevisualisation(int vHauteurPrevisualisation) {
        valeurHauteurPrevisualisation = vHauteurPrevisualisation;
    }

    @Override
    public void setValeurLargeurPrevisualisation(int vLargeurPrevisualisation) {
        valeurLargeurPrevisualisation = vLargeurPrevisualisation;
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
                    decompteTimer = LENTEUR_ATTENTE;
                }
            } else {
                decompteTimer--;
            }
        }
    }

    public void changerJoueurCourant() {
        joueurCourant = (joueurCourant + 1) % joueurs.length;
        decompteTimer = LENTEUR_ATTENTE;
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
