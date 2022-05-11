package Modele;

import Global.Configuration;
import Global.Element;
import Pattern.Observable;

public class Plateau extends Observable {

    // ===============================
    // ===== INFORMATION PLATEAU =====
    // ===============================
    static final int TAILLE_DU_PLATEAU = 16;
    static final int EXTREMITE_GAUCHE_DU_PLATEAU = -8;
    static final int EXTREMITE_DROITE_DU_PLATEAU = 8;

    // ========================================
    // ===== POSITIONS INITIALES ELEMENTS =====
    // ========================================
    static final int POSITION_BASE_GARDE_GAUCHE = -2;
    static final int POSITION_BASE_GARDE_DROIT = 2;
    static final int POSITION_BASE_ROI = 0;
    static final int POSITION_BASE_FOU = -1;
    static final int POSITION_BASE_SORCIER = 1;

    // ==================
    // ===== JOUEUR =====
    // ==================
    static final int JOUEUR_GAUCHE = 0;
    static final int JOUEUR_DROIT = 1;

    // ==============================
    // ===== INFORMATION PARTIE =====
    // ==============================
    boolean partieEnCours = false;
    boolean partieTerminee = false;
    boolean tourJoueurGauche = false;
    boolean tourJoueurDroit = false;
    int joueurCourant = JOUEUR_DROIT;

    // ====================
    // ===== ELEMENTS =====
    // ====================
    Couronne couronne;
    Personnage gardeGauche;
    Personnage gardeDroit;
    Personnage roi;
    Personnage fou;
    Personnage sorcier;
    Paquet paquet;

    // ============================
    // ===== VALEURS ELEMENTS =====
    // ============================
    static final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    static final Element GARDE_DROIT = Element.GARDE_DROIT;
    static final Element ROI = Element.ROI;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;

    // @TODO JEU ???

    public Plateau() {
        reset();
    }

    public void reset() {

        partieTerminee = false;
        partieEnCours = true;

        couronne = new Couronne();
        gardeGauche = new Personnage(GARDE_GAUCHE, POSITION_BASE_GARDE_GAUCHE, false);
        gardeDroit = new Personnage(GARDE_DROIT, POSITION_BASE_GARDE_DROIT, false);
        roi = new Personnage(ROI, POSITION_BASE_ROI, false);
        fou = new Personnage(FOU, POSITION_BASE_FOU, true);
        sorcier = new Personnage(SORCIER, POSITION_BASE_SORCIER, true);

        paquet = new Paquet();

        metAJour();
    }

    boolean estPartieTerminee() {
        return partieTerminee;
    }

    boolean estPartieEnCours() {
        return partieEnCours;
    }

    public void deplacerElement(Element element, int deplacementElement) {
        if (!estPartieTerminee()) {
            if (estPartieEnCours()) {
                switch (element) {
                    case COURONNE:
                        if (validationDeplacement(element, deplacementElement)) {
                            deplacerCouronne(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    case GARDE_GAUCHE:
                        if (validationDeplacement(element, deplacementElement)) {
                            gardeGauche.deplacerPersonnage(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    case GARDE_DROIT:
                        if (validationDeplacement(element, deplacementElement)) {
                            gardeDroit.deplacerPersonnage(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    case ROI:
                        if (validationDeplacement(element, deplacementElement)) {
                            roi.deplacerPersonnage(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    case FOU:
                        if (validationDeplacement(element, deplacementElement)) {
                            fou.deplacerPersonnage(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    case SORCIER:
                        if (validationDeplacement(element, deplacementElement)) {
                            sorcier.deplacerPersonnage(deplacementElement);
                        } else {
                            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible !!");
                        }
                        break;

                    default:
                        Configuration.instance().logger().warning("Element " + element.name() + " inconnu !!");
                        break;
                }
            } else {
                Configuration.instance().logger()
                        .info("Deplacement " + element.name() + " impossible, partie en pause !!");
            }
        } else {
            Configuration.instance().logger().info("Deplacement " + element.name() + " impossible, partie terminee !!");
        }
        metAJour();
    }

    private boolean validationDeplacement(Element element, int deplacementElement) {
        int nouvellePosition = 0;
        switch (element) {
            case COURONNE:
                return true;
            case GARDE_GAUCHE:
                nouvellePosition = gardeGauche.positionPersonnage() + deplacementElement;
                if ((nouvellePosition >= EXTREMITE_GAUCHE_DU_PLATEAU)
                        && (nouvellePosition < roi.positionPersonnage())) {
                    return true;
                } else {
                    return false;
                }
            case GARDE_DROIT:
                nouvellePosition = gardeDroit.positionPersonnage() + deplacementElement;
                if ((nouvellePosition <= EXTREMITE_DROITE_DU_PLATEAU)
                        && (nouvellePosition > roi.positionPersonnage())) {
                    return true;
                } else {
                    return false;
                }
            case ROI:
                nouvellePosition = roi.positionPersonnage() + deplacementElement;
                if ((nouvellePosition > gardeGauche.positionPersonnage())
                        && (nouvellePosition < gardeDroit.positionPersonnage())) {
                    return true;
                } else {
                    return false;
                }
            case FOU:
                nouvellePosition = fou.positionPersonnage() + deplacementElement;
                if ((nouvellePosition >= EXTREMITE_GAUCHE_DU_PLATEAU)
                        && (nouvellePosition <= EXTREMITE_DROITE_DU_PLATEAU)) {
                    return true;
                } else {
                    return false;
                }
            case SORCIER:
                nouvellePosition = sorcier.positionPersonnage() + deplacementElement;
                if ((nouvellePosition >= EXTREMITE_GAUCHE_DU_PLATEAU)
                        && (nouvellePosition <= EXTREMITE_DROITE_DU_PLATEAU)) {
                    return true;
                } else {
                    return false;
                }
            default:
                Configuration.instance().logger().warning("Element " + element.name() + " inconnu !!");
                return false;
        }
    }

    private void deplacerCouronne(int deplacementElement) {
        int nouvellePosition = couronne.positionCouronne() + deplacementElement;
        if (nouvellePosition > EXTREMITE_DROITE_DU_PLATEAU) {
            couronne.positionnerCouronne(EXTREMITE_DROITE_DU_PLATEAU);
        } else if (nouvellePosition < EXTREMITE_GAUCHE_DU_PLATEAU) {
            couronne.positionnerCouronne(EXTREMITE_GAUCHE_DU_PLATEAU);
        }
    }

    void changerEtatCouronne() {
        couronne.changerEtatCouronne();
        metAJour();
    }

    void jouerCarte(Element element) {
        // TODO Méthode jouer carte !!!
        Configuration.instance().logger().info("La carte " + element.name() + " a ete joue !!");
        metAJour();
    }

    public void activerPouvoirFou(Element element) {
        if (!estPartieTerminee()) {
            if (estPartieEnCours()) {
                int positionFou = fou.positionPersonnage();
                int positionRoi = roi.positionPersonnage();
                if (joueurCourant == JOUEUR_DROIT) {
                    if (positionFou > positionRoi) {
                        jouerCarte(element);
                    } else {
                        Configuration.instance().logger().info("Pouvoir du Fou inutilisable pour le joueur de droite !!");
                    }
                } else {
                    if (positionFou < positionRoi) {
                        jouerCarte(element);
                    } else {
                        Configuration.instance().logger().info("Pouvoir du Fou inutilisable pour le joueur de gauche !!");
                    }
                }
            }
        }
        metAJour();
    }

    public void activerPouvoirSorcier(Element element) {
        if (!estPartieTerminee()) {
            if (estPartieEnCours()) {
                int teleporter = 0;
                int positionSorcier = sorcier.positionPersonnage();
                switch (element) {
                    case GARDE_GAUCHE:
                        int positionGardeGauche = gardeGauche.positionPersonnage();
                        if (joueurCourant == JOUEUR_DROIT) {
                            teleporter = positionSorcier - positionGardeGauche;
                        } else {
                            teleporter = positionGardeGauche - positionSorcier;
                        }
                        if (validationDeplacement(element, teleporter)) {
                            gardeGauche.deplacerPersonnage(teleporter);
                        } else {
                            Configuration.instance().logger().info("Teleportation " + element.name() + " impossible !!");
                        }
                        break;
                    case GARDE_DROIT:
                        int positionGardeDroit = gardeDroit.positionPersonnage();
                        if (joueurCourant == JOUEUR_DROIT) {
                            teleporter = positionSorcier - positionGardeDroit;
                        } else {
                            teleporter = positionGardeDroit - positionSorcier;
                        }
                        if (validationDeplacement(element, teleporter)) {
                            gardeDroit.deplacerPersonnage(teleporter);
                        } else {
                            Configuration.instance().logger().info("Teleportation " + element.name() + " impossible !!");
                        }
                        break;
                    case ROI:
                        int positionRoi = roi.positionPersonnage();
                        if (joueurCourant == JOUEUR_DROIT) {
                            teleporter = positionSorcier - positionRoi;
                        } else {
                            teleporter = positionRoi - positionSorcier;
                        }
                        if (validationDeplacement(element, teleporter)) {
                            roi.deplacerPersonnage(teleporter);
                        } else {
                            Configuration.instance().logger().info("Teleportation " + element.name() + " impossible !!");
                        }
                        break;
                    default:
                        Configuration.instance().logger().info("Teleportation " + element.name() + " sur le sorcier non autorise !!");
                        break;
                }
            }
        }
        metAJour();
    }

    public void afficherPlateau() {
        System.out.println("");

        // CHIFFRES
        System.out.println("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        String ligneChiffre = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i < 0) {
                ligneChiffre = ligneChiffre + "| " + i;
            } else {
                ligneChiffre = ligneChiffre + "| " + i + " ";
            }

        }
        ligneChiffre = ligneChiffre + "|";
        System.out.println(ligneChiffre);

        // COUR
        System.out.println("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        String ligneElement = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i == gardeGauche.positionPersonnage()) {
                ligneElement = ligneElement + "| Gg";
            } else if (i == gardeDroit.positionPersonnage()) {
                ligneElement = ligneElement + "| Gd";
            } else if (i == roi.positionPersonnage()) {
                ligneElement = ligneElement + "| R ";
            } else {
                ligneElement = ligneElement + "|   ";
            }
        }
        ligneElement = ligneElement + "|";
        System.out.println(ligneElement);

        // FOU
        String ligneFou = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i == fou.positionPersonnage()) {
                ligneFou = ligneFou + "| F ";
            } else {
                ligneFou = ligneFou + "|   ";
            }
        }
        ligneFou = ligneFou + "|";
        System.out.println(ligneFou);

        // SORCIER
        String ligneSorcier = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i == sorcier.positionPersonnage()) {
                ligneSorcier = ligneSorcier + "| S ";
            } else {
                ligneSorcier = ligneSorcier + "|   ";
            }
        }
        ligneSorcier = ligneSorcier + "|";
        System.out.println(ligneSorcier);

        // COURONNE
        System.out.println("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        String ligneCouronne = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i == couronne.positionCouronne()) {
                ligneCouronne = ligneCouronne + "| C ";
            } else {
                ligneCouronne = ligneCouronne + "|   ";
            }
        }
        ligneCouronne = ligneCouronne + "|";
        System.out.println(ligneCouronne);
        System.out.println("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        System.out.println("");
    }

    public void choixPremierJoueur(int premierJoueur) {
        if (premierJoueur != joueurCourant) {
            echangerFouSorcier();
            changerJoueurCourant(premierJoueur);
        }
    }

    private void echangerFouSorcier() {
        fou.positionnerPersonnage(POSITION_BASE_SORCIER);
        sorcier.positionnerPersonnage(POSITION_BASE_FOU);
    }

    public void changerJoueurCourant(int numeroJoueur) {
        if (numeroJoueur == JOUEUR_DROIT) {
            joueurCourant = JOUEUR_DROIT;
        } else if (numeroJoueur == JOUEUR_GAUCHE) {
            joueurCourant = JOUEUR_GAUCHE;
        } else {
            Configuration.instance().logger().info("Numero de joueur inconnu !! Changement de joueur non realise !!");
        }
    }

    // Numero : 0 = Joueur Gauche | 1 = Joueur Droit
    // Type : 0 = Joueur Humain | 1 = Joueur IA
    public void changerJoueurCourant(int numeroJoueur, int typeJoueur) {
        // TODO Méthode changerJoueurCourant IA - cf. IA
    }
}
