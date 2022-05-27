package Controleur;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Global.Configuration;
import Global.Element;
import Global.InfoJeu;
import Joueur.Joueur;
import Joueur.JoueurHumain;
import Joueur.JoueurIAAleatoire;
import Joueur.JoueurIAAleatoireIntelligente;
import Joueur.JoueurIARandom;
import Joueur.JoueurIAnastasia;
import Joueur.JoueurIAmel;
import Joueur.JoueurIAmelie;
import Modele.Coup;
import Modele.Jeu;
import Modele.Plateau;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements {

    // ==================
    // ===== JOUEUR =====
    // ==================
    static final int NOMBRE_JOUEUR = 2;
    static final int NOMBRE_TYPE_JOUEUR = 7;

    static final int JOUEUR_GAUCHE = 0;
    static final int JOUEUR_DROIT = 1;

    static final int JOUEUR_HUMAIN = 0;
    static final int JOUEUR_IAALEATOIRE = 1;
    static final int JOUEUR_IAALEATOIRE_TOTALE = 2;
    static final int JOUEUR_IAALEATOIRE_INTELLIGENTE = 3;
    static final int JOUEUR_IAEXPERTE = 4;
    static final int JOUEUR_AMEL = 5;
    static final int JOUEUR_AMELIE = 6;

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

    // ====================
    // ===== ETAT JEU =====
    // ====================
    static InfoJeu ETAT_JEU = InfoJeu.DEBUT_TOUR;
    InfoJeu preOptions;

    Jeu jeu;
    InterfaceUtilisateur interfaceUtilisateur;

    Plateau plateauDebutTour;

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

        plateauDebutTour = jeu.plateau().clone();

        // String nomFichierAudio = "gangstas-paradise-medieval";
        // String nomFichierAudio = "the-weeknd-medieval";
        // lancerAudioMusique(nomFichierAudio);

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

            switch(typeDuJoueur){
                case JOUEUR_HUMAIN :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurHumain(numeroJoueur, jeu);
                    break;
                case JOUEUR_IAALEATOIRE :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIAAleatoire(numeroJoueur, jeu);
                    break;
                case JOUEUR_IAALEATOIRE_TOTALE :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIARandom(numeroJoueur, jeu);
                    break;
                case JOUEUR_IAALEATOIRE_INTELLIGENTE :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIAAleatoireIntelligente(numeroJoueur, jeu);
                    break;
                case JOUEUR_IAEXPERTE :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIAnastasia(numeroJoueur, jeu);
                    break;
                case JOUEUR_AMEL :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIAmel(numeroJoueur, jeu);
                    break;
                case JOUEUR_AMELIE :
                    joueurs[numeroJoueur][typeDuJoueur] = new JoueurIAmelie(numeroJoueur, jeu);
                    break;
            }
            
        }
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

    // TODO NETTOYAGE VVVVVVVV

    void gestionHistorique(Plateau plat) {
        Coup coup = jeu.creerCoup(plat);
        if (coup != null) {
            jeu.jouerCoup(coup);
        } else {
            System.out.println("Plateau d'historique null !!!");
        }
    }

    void jouerCoup(Coup coup) {
        jeu.jouerCoup(coup);
    }

    void annule() {
        if(jeu.actionAutoriser()){
            jeu.annulerTour();
            jeu.annule();
            jeu.fixerPositions();
            joueurCourant = jeu.joueurCourant();
            plateauDebutTour = jeu.plateau().clone();
        }
    }

    void refaire() {
        if(jeu.actionAutoriser()){
            jeu.annulerTour();
            jeu.refaire();
            jeu.fixerPositions();
            joueurCourant = jeu.joueurCourant();
            plateauDebutTour = jeu.plateau().clone();
        }
    }

    @Override
    public void clicCarte(int coupX) {
        switch (ETAT_JEU) {
            case DEBUT_TOUR:
            case APRES_UNE_CARTE:
                if (!jeu.teleportationFaite) {
                    int type = typeJoueur[joueurCourant];
                    if (type == JOUEUR_HUMAIN) {
                        if (jeu.carteActuelle() == coupX) {
                            jeu.changeCarteActuelle(8);
                        } else {
                            if (jeu.listeCarteJouable()[coupX] != 0) {
                                jeu.changeCarteActuelle(coupX);
                            } else {
                                Configuration.instance().logger().info("Carte non identique");
                            }
                        }
                    } else {
                        jeu.changeCarteActuelle(8);
                    }
                }
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
                if (attenteCarte) {
                    finDeTour();
                    decompteTimer = LENTEUR_ATTENTE;
                    attenteCarte = false;
                } else {
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
                    initInfoJoueurs();
                    //jeu.plateau().initialisation();
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
            case "Pause":
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
                jeu.nonFinPartie();
                if (!jeu.estPartieEnCours()) {
                    jeu.changerEtatPartie();
                }
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
            case "Visible":
                jeu.mainJoueurSecondaireVisible();
                break;
            case "Annuler":
                annule();
                break;
            case "Refaire":
                refaire();
                break;
            case "AideIA":
                aideIA();
                break;
            default:
                return false;
        }
        return true;
    }

    private void aideIA() {
        if(jeu.actionAutoriser()){
            jeu.annulerTour();
            changerJoueurCourant(jeu.joueurCourant(), JOUEUR_AMEL);
            joueurs[joueurCourant][JOUEUR_AMEL].tempsEcoule();
            changerJoueurCourant(jeu.joueurCourant(), JOUEUR_HUMAIN);
        }
    }

    private void initInfoJoueurs() {
        int infoJoueurGauche = getInfoJoueur(JOUEUR_GAUCHE);
        System.out.println("Joueur de gauche : " + infoJoueurGauche + " | Joueur de droite : " + infoJoueurGauche);
        int infoJoueurDroite = getInfoJoueur(JOUEUR_DROIT);
        changerJoueurCourant(JOUEUR_GAUCHE, infoJoueurGauche);
        changerJoueurCourant(JOUEUR_DROIT, infoJoueurDroite);
        jeu.initNomJoueurs(interfaceUtilisateur.getNomJoueur(JOUEUR_GAUCHE),
                interfaceUtilisateur.getNomJoueur(JOUEUR_DROIT));
    }

    // TODO rajouter des case pour plus d'IA
    private int getInfoJoueur(int coteJoueur) {
        System.out.println(interfaceUtilisateur.getInfoJoueur(coteJoueur));
        switch (interfaceUtilisateur.getInfoJoueur(coteJoueur)) {
            case ("Humain"):
                return JOUEUR_HUMAIN;
            case ("IAtrèsfacile"):
                return JOUEUR_IAALEATOIRE;
            case ("IAfacile"):
                return JOUEUR_IAALEATOIRE_TOTALE;
            case ("IAnormale"):
                return JOUEUR_IAALEATOIRE_INTELLIGENTE;
            case ("IAdifficile"):
                return JOUEUR_IAEXPERTE;
            case ("IAexperte"):
                return JOUEUR_AMEL;
            case ("IAtriche"):
                return JOUEUR_AMELIE;

            default:
                return -1;
        }

    };

    private void finDeTour() {
        if (jeu.dernierTypeDePersonnageJouer != Element.VIDE || jeu.teleportationFaite == true) {
            
            gestionHistorique(plateauDebutTour);

            jeu.finDeTour();
            jeu.changerEtatJeu(InfoJeu.DEBUT_TOUR);

            plateauDebutTour = jeu.plateau().clone();

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

    public void passerSurCase(int coupX){
        jeu.choisirPasserSurCase(coupX);
    }

    @Override
    public void passerSurCarte(int coupX) {
        int type = typeJoueur[joueurCourant];
        if (type == JOUEUR_HUMAIN) {
            jeu.choisirPasserSurCarte(coupX);
        }
        else{
            jeu.choisirPasserSurCarte(8);
        }
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