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

    // ==============================
    // ===== INFORMATION PARTIE =====
    // ==============================
    boolean partieEnCours = false;
    boolean partieTerminee = false;
    boolean tourJoueurGauche = false;
    boolean tourJoueurDroit = false;
    int joueurCourant = 1; // 0 : Joueur Gauche | 1 : Joueur Droit

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

    // @TO DO JEU ???

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

    void jouerCarte() {
        // TO DO !!!
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

    // 0 : Joueur Gauche | 1 : Joueur Droit
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

    // 0 : Joueur Gauche | 1 : Joueur Droit
    public void changerJoueurCourant(int numeroJoueur) {
        joueurCourant = numeroJoueur;
    }

    // Numero : 0 = Joueur Gauche | 1 = Joueur Droit
    // Type : 0 = Joueur Humain | 1 = Joueur IA
    public void changerJoueurCourant(int numeroJoueur, int typeJoueur) {
        // TO DO - cf. IA
    }
}

