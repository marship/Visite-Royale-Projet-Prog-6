package Controleur;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Audio.LecteurAudio;
import Global.Configuration;
import Global.Element;
import Global.InfoJeu;
import Joueur.Joueur;
import Joueur.JoueurHumain;
import Joueur.JoueurIAAleatoire;
import Joueur.JoueurIAAleatoireIntelligente;
import Joueur.JoueurIAExperte;
import Joueur.JoueurIARandom;
import Joueur.JoueurIAnastasia;
import Joueur.JoueurIAmel;
import Modele.Jeu;
import Modele.Plateau;
import Modele.PlateauHistorique;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements {

    // ==================
    // ===== JOUEUR =====
    // ==================
    static final int NOMBRE_JOUEUR = 2;
    static final int NOMBRE_TYPE_JOUEUR = 6;

    static final int JOUEUR_GAUCHE = 0;
    static final int JOUEUR_DROIT = 1;

    static final int JOUEUR_HUMAIN = 0;
    static final int JOUEUR_IAALEATOIRE = 1;
    static final int JOUEUR_IAALEATOIRE_TOTALE = 2;
    static final int JOUEUR_IAALEATOIRE_INTELLIGENTE = 3;
    static final int JOUEUR_IAEXPERTE = 4;
    static final int JOUEUR_AMEL = 5;

    // ===================
    // ===== PLATEAU =====
    // ===================
    static final int LIGNE_PLATEAU_FOU = 2;
    static final int LIGNE_PLATEAU_COUR = 3;
    static final int LIGNE_PLATEAU_SORCIER = 4;

    // =====================
    // ===== DIRECTION =====
    // =====================
    static final int GAUCHE = 0;
    static final int DROITE = 1;

    // =================
    // ===== TIMER =====
    // =================
    static final int LENTEUR_ATTENTE = 100;

    // =================
    // ===== AUDIO =====
    // =================
    int optionAudio = 0;

    // ====================
    // ===== ETAT JEU =====
    // ====================
    static InfoJeu ETAT_JEU = InfoJeu.DEBUT_TOUR;
    InfoJeu preOptions;

    Jeu jeu;
    InterfaceUtilisateur interfaceUtilisateur;

    Joueur[][] joueurs;
    int[] typeJoueur;
    int joueurCourant;

    int decompteTimer = LENTEUR_ATTENTE;
    int carteActuelle; // Quelle carte est choisie actuellement
    boolean attenteCarte;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public ControleurMediateur(Jeu j) {
        jeu = j;
        joueurs = new Joueur[NOMBRE_JOUEUR][NOMBRE_TYPE_JOUEUR];
        typeJoueur = new int[NOMBRE_TYPE_JOUEUR];

        // String nomFichierAudio = "the-weeknd-medieval";
        String nomFichierAudio = "gangstas-paradise-medieval";
        //lancerAudio(nomFichierAudio);

        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][JOUEUR_HUMAIN] = new JoueurHumain(i, jeu);
            joueurs[i][JOUEUR_IAALEATOIRE] = new JoueurIAAleatoire(i, jeu);
            joueurs[i][JOUEUR_IAALEATOIRE_TOTALE] = new JoueurIARandom(i, jeu);
            joueurs[i][JOUEUR_IAALEATOIRE_INTELLIGENTE] = new JoueurIAAleatoireIntelligente(i, jeu);
            joueurs[i][JOUEUR_IAEXPERTE] = new JoueurIAnastasia(i, jeu);
            joueurs[i][JOUEUR_AMEL] = new JoueurIAmel(i, jeu);
        }

        changerJoueurCourant(JOUEUR_GAUCHE, JOUEUR_HUMAIN);
        changerJoueurCourant(JOUEUR_DROIT, JOUEUR_AMEL);

        joueurCourant = jeu.joueurCourant();
        attenteCarte = false;
    }

    // ==================
    // ===== JOUEUR =====
    // ==================
    @Override
    public void changerJoueurCourant(int numeroJoueur, int typeDuJoueur) {
        if (jeu.numeroJoueurValide(numeroJoueur)) {
            Configuration.instance().logger().info("Type de joueur : " + typeJoueur + " | Joueur : " + numeroJoueur);
            typeJoueur[numeroJoueur] = typeDuJoueur;
        }
    }

    // =====================
    // ===== INFOS JEU =====
    // =====================
    @Override
    public InfoJeu getInfoJeu() {
        return ETAT_JEU;
    }

    // ================
    // ===== CLIC =====
    // ================
    @Override
    public void clicPlateau(int clicX, int clicY) {
        if (jeu.carteActuelle() != 8) {
            if (joueurs[joueurCourant][typeJoueur[joueurCourant]].jeu(clicX, jeu.carteActuelle())) {
                jeu.changeCarteActuelle(8);
            }
        } else {
            Element elementChoisi = selectionElementPlateau(clicX, clicY);
            switch (ETAT_JEU) {
                case CHOIX_FOU:
                    switch (elementChoisi) {
                        case ROI:
                        case SORCIER:
                            jeu.personnageManipulerParLeFou(elementChoisi);
                            break;
                        case GARDE_GAUCHE:
                        case GARDE_DROIT:
                            jeu.personnageManipulerParLeFou(Element.GARDES);
                            break;
                        default:
                            jeu.personnageManipulerParLeFou(Element.FOU);
                            break;
                    }
                    jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                    ETAT_JEU = InfoJeu.DEBUT_TOUR;
                    break;
                case CHOIX_SORCIER:
                    switch (elementChoisi) {
                        case ROI:
                        case GARDE_GAUCHE:
                        case GARDE_DROIT:
                            teleportationElement(elementChoisi);
                            break;
                        default:
                            break;
                    }
                    jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                    ETAT_JEU = InfoJeu.DEBUT_TOUR;
                    return;
                case CHOIX_ROI:
                    int possible = jeu.positionsPourCour();
                    if ((clicX == jeu.obtenirPositionElement(Element.ROI) - 1)
                            && ((possible == 1) || (possible == 0))) {
                        selectionRoi(GAUCHE);
                    } else if ((clicX == jeu.obtenirPositionElement(Element.ROI) + 1)
                            && ((possible == 2) || (possible == 0))) {
                        selectionRoi(DROITE);
                    }
                    jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                    ETAT_JEU = InfoJeu.DEBUT_TOUR;
                    return;
                case DEBUT_TOUR:
                    switch (elementChoisi) {
                        case ROI:
                            if (jeu.plateau().paquet.nombreCartesElement(joueurCourant, Element.ROI, 0) >= 2
                                    && (jeu.dernierTypeDePersonnageJouer == Element.ROI
                                            || jeu.dernierTypeDePersonnageJouer == Element.VIDE)
                                    && !jeu.teleportationFaite) {
                                jeu.changerEtatJeu(InfoJeu.CHOIX_ROI);
                                ETAT_JEU = InfoJeu.CHOIX_ROI;
                            }
                            break;
                        case FOU:
                            if (jeu.estPouvoirFouActivable() && jeu.dernierTypeDePersonnageJouer == Element.VIDE
                                    && !jeu.teleportationFaite) {
                                jeu.changerEtatJeu(InfoJeu.CHOIX_FOU);
                                ETAT_JEU = InfoJeu.CHOIX_FOU;
                            }
                            break;
                        case SORCIER:
                            if (jeu.dernierTypeDePersonnageJouer == Element.VIDE) {
                                jeu.changerEtatJeu(InfoJeu.CHOIX_SORCIER);
                                ETAT_JEU = InfoJeu.CHOIX_SORCIER;
                            }
                            break;
                        default:
                            break;
                    }
                    return;
                default:
                    Configuration.instance().logger().warning("Echec de la selection !!");
                    break;
            }
        }
    }

    private void selectionRoi(int direction) {
        int[] cartes = new int[2];
        cartes[0] = jeu.plateau().paquet.trouverRoi(joueurCourant, 0);
        cartes[1] = jeu.plateau().paquet.trouverRoi(joueurCourant, 1);
        jeu.deplacerCour(direction, cartes);
    }

    void teleportationElement(Element element) {
        if (jeu.estPouvoirSorcierActivable(element)) {
            jeu.teleportationPouvoirSorcier(element);
        }
    }

    private Element selectionElementPlateau(int selectionPlateauX, int selectionPlateauY) {
        Element elementSelectione = Element.VIDE;
        switch (selectionPlateauY) {
            case LIGNE_PLATEAU_FOU:
                if (jeu.obtenirPositionElement(Element.FOU) == selectionPlateauX) {
                    elementSelectione = Element.FOU;
                }
                return elementSelectione;
            case LIGNE_PLATEAU_COUR:
                if (jeu.obtenirPositionElement(Element.ROI) == selectionPlateauX) {
                    elementSelectione = Element.ROI;
                } else if (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) == selectionPlateauX) {
                    elementSelectione = Element.GARDE_GAUCHE;
                } else if (jeu.obtenirPositionElement(Element.GARDE_DROIT) == selectionPlateauX) {
                    elementSelectione = Element.GARDE_DROIT;
                }
                return elementSelectione;
            case LIGNE_PLATEAU_SORCIER:
                if (jeu.obtenirPositionElement(Element.SORCIER) == selectionPlateauX) {
                    elementSelectione = Element.SORCIER;
                }
                return elementSelectione;
            default:
                return elementSelectione;
        }
    }

    // SON
    void lancerAudio(String nomFichierAudio) {
        try {
            LecteurAudio lecteurAudio = new LecteurAudio(nomFichierAudio);
            lecteurAudio.play();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            Configuration.instance().logger().severe("Erreur au lancement de l'audio !!");
            e.printStackTrace();
        }
    }

    void optionAudio(LecteurAudio lecteurAudio, int option) {
        try {
            lecteurAudio.gotoChoice(option);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    // TODO NETTOYAGE VVVVVVVV

    void plateauHistorique(Plateau p) {
        PlateauHistorique plateauHistorique = jeu.determinerPlateauHistorique(p);
        if (plateauHistorique != null) {
            sauvegarderPlateauHistorique(plateauHistorique);
        }
    }

    void sauvegarderPlateauHistorique(PlateauHistorique pHistorique) {
        jeu.sauvegarderPlateauHistorique(pHistorique);
    }

    void annule() {
        /*
         * jeu.annule();
         * interfaceGraphique.miseAJourTableauScore();
         * interfaceGraphique.miseAJourCouleurJoueurCourant(1, 0, false);
         * jeu.nbCoupMoins();
         * interfaceGraphique.miseAJourNbCoup();
         */
    }

    void refaire() {
        /*
         * jeu.refaire();
         * jeu.nbCoupPlus();
         * interfaceGraphique.miseAJourNbCoup();
         */
    }

    @Override
    public void clicCarte(int coupX) {
        switch (ETAT_JEU) {
            case DEBUT_TOUR:
            case APRES_UNE_CARTE:
                if (!jeu.teleportationFaite) {
                    if (jeu.carteActuelle() == coupX) {
                        jeu.changeCarteActuelle(8);
                    } else {
                        if (jeu.listeCarteJouable()[coupX] != 0) {
                            jeu.changeCarteActuelle(coupX);
                        } else {
                            Configuration.instance().logger().info("Carte non identique");
                        }
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
                // System.out.println("X: " + positionSourisX + ", Y: " + positionSourisY);
                // gestionPrevisualisationCoup(positionSourisX, positionSourisY);
                break;
            default:
                break;
        }
    }

    @Override
    public void tictac() {
        if (jeu.actionAutoriser()) {
            if (decompteTimer == 0) {
                int type = typeJoueur[joueurCourant];
                // Lorsque le temps est écoulé on le transmet au joueur courant.
                // Si un coup a été joué (IA) on change de joueur.
                if(attenteCarte){
                    finDeTour();
                    decompteTimer = LENTEUR_ATTENTE;
                    attenteCarte = false;
                }
                else{
                    if (joueurs[joueurCourant][type].tempsEcoule()) {
                        attenteCarte = true;
                        decompteTimer = LENTEUR_ATTENTE * 2;
                    } else {
                        // Sinon on indique au joueur qui ne réagit pas au temps (humain) qu'on
                        // l'attend.
                        decompteTimer = LENTEUR_ATTENTE;
                    }
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
                ETAT_JEU = InfoJeu.SELECTION_JOUEURS;
                interfaceUtilisateur.afficherPanneau("SelectionJoueur");
                break;
            case "MenuPrincipal":
                interfaceUtilisateur.afficherPanneau("MenuPrincipal");
                break;
            case "Valider":
                if (!jeu.estPartieEnCours()) {
                    jeu.plateau().initialisation();
                    interfaceUtilisateur.afficherPanneau("Plateau");
                }
                jeu.changerEtatPartie();
                jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                interfaceUtilisateur.afficherPanneau("Plateau");
                break;
            case "Charger":
                charge();
                jeu.changerEtatPartie();
                jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                interfaceUtilisateur.afficherPanneau("Plateau");
                break;
            case "Regles":
                break;
            case "Options":
                jeu.changerEtatJeu(InfoJeu.OPTIONS_MENU);
                ETAT_JEU = InfoJeu.OPTIONS_MENU;
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
            case "OptionsJeu":
                preOptions = ETAT_JEU;
                jeu.changerEtatJeu(InfoJeu.OPTIONS_JEU);
                ETAT_JEU = InfoJeu.OPTIONS_JEU;
                interfaceUtilisateur.afficherPanneau("OptionsJeu");
                break;

            case "Recommencer":
                jeu.plateau().initialisation();
                jeu.fixerPositions();
                joueurCourant = jeu.joueurCourant();
                jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                jeu.changeCarteActuelle(8);
                jeu.majDernierTypeDePersonnageJouer(Element.VIDE);
                interfaceUtilisateur.afficherPanneau("Plateau");
                break;
            case "SauvegarderQuitter":
                jeu.sauvegarder(typeJoueur[0], typeJoueur[1]);
                // ETAT_JEU = InfoJeu.MENU_PRINCIPAL;
                // interfaceUtilisateur.afficherPanneau("MenuPrincipal");
                break;
            case "RetourJeu":
                jeu.changerEtatJeu(preOptions);
                ETAT_JEU = preOptions;
                interfaceUtilisateur.afficherPanneau("Plateau");
                break;
            case "AnnulerTour":
                jeu.annulerTour();
                jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);
                jeu.changeCarteActuelle(8);
                ETAT_JEU = InfoJeu.DEBUT_TOUR;
                break;
            case "visible":
                jeu.mainJoueurSecondaireVisible();
                break;
            default:
                return false;
        }
        return true;
    }

    private void finDeTour() {
        if (jeu.dernierTypeDePersonnageJouer != Element.VIDE || jeu.teleportationFaite == true) {
            jeu.finDeTour();
            jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);

            plateauHistorique(jeu.plateau());
            System.out.println("Taille de l'historique : " + jeu.tailleHistoirique());

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

    @Override
    public void passerSurCarte(int coupX) {
        jeu.choisirPasserSurCarte(coupX);
    }

    public void charge() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "Sauvegardes");
        int returnVal = chooser.showOpenDialog(interfaceUtilisateur.fenetre());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Vous n'avez rien selectionne");
            return;
        }
        int[] type = jeu.charger(chooser.getSelectedFile().getPath());
        typeJoueur[0] = type[0];
        typeJoueur[1] = type[1];
    }
}
