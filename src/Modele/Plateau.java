package Modele;

import Global.Configuration;
import Global.Element;
// import Structures.Iterateur;
// import Structures.Sequence;
import Structures.Sequence;

public class Plateau extends Historique<Coup> implements Cloneable {

    // ===============================
    // ===== INFORMATION PLATEAU =====
    // ===============================
    static final int TAILLE_DU_PLATEAU = 17;
    static final int CENTRE_DU_PLATEAU = 0;
    static final int EXTREMITE_GAUCHE_DU_PLATEAU = -8;
    static final int EXTREMITE_DROITE_DU_PLATEAU = 8;
    static final int ENTREE_CHATEAU_GAUCHE = -6;
    static final int ENTREE_CHATEAU_DROIT = 6;

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
    public int joueurCourant = JOUEUR_DROIT;
    public int joueurGagnant = AUCUN_GAGNANT;
    public boolean victoirePioche = false;

    // ====================
    // ===== ELEMENTS =====
    // ====================
    public Personnage gardeGauche;
    public Personnage gardeDroit;
    public Personnage roi;
    public Personnage fou;
    public Personnage sorcier;
    public Couronne couronne;
    public Paquet paquet;

    // ============================
    // ===== VALEURS ELEMENTS =====
    // ============================
    static final Element VIDE = Element.VIDE;
    static final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    static final Element GARDE_DROIT = Element.GARDE_DROIT;
    static final Element ROI = Element.ROI;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;

    // ===========================
    // ===== VALEURS GAGNANT =====
    // ===========================
    static final int AUCUN_GAGNANT = 0;
    static final int ROI_GAGNANT = 1;
    static final int COURONNE_GAGNANTE = 2;
    static final int MEULE_GAGNANTE_GAUCHE = 3;
    static final int MEULE_GAGNANTE_DROITE = 4;

    Sequence<Plateau> passe, futur;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Plateau() {
        initialisation();
    }

    public void initialisation() {
        joueurCourant = JOUEUR_DROIT;

        gardeGauche = new Personnage(GARDE_GAUCHE, POSITION_BASE_GARDE_GAUCHE);
        gardeDroit = new Personnage(GARDE_DROIT, POSITION_BASE_GARDE_DROIT);
        roi = new Personnage(ROI, POSITION_BASE_ROI);
        fou = new Personnage(FOU, POSITION_BASE_FOU);
        sorcier = new Personnage(SORCIER, POSITION_BASE_SORCIER);
        couronne = new Couronne();

        paquet = new Paquet();
    }

    // =================
    // ===== CLONE =====
    // =================
    @Override
    public Plateau clone() {
        Plateau clone = null;
        try {
            clone = (Plateau) super.clone();

            clone.gardeGauche = new Personnage(gardeGauche.nomPersonnage, gardeGauche.positionPersonnage);
            clone.gardeDroit = new Personnage(gardeDroit.nomPersonnage, gardeDroit.positionPersonnage);
            clone.roi = new Personnage(roi.nomPersonnage, roi.positionPersonnage);
            clone.fou = new Personnage(fou.nomPersonnage, fou.positionPersonnage);
            clone.sorcier = new Personnage(sorcier.nomPersonnage, sorcier.positionPersonnage);
            clone.couronne = new Couronne();
            clone.couronne.definirEtatCouronne(couronne.etatCouronne());
            clone.couronne.positionnerCouronne(couronne.positionCouronne());
            clone.paquet = paquet.clone();
            return clone;

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne serieux avec le clone");
            System.exit(1);
        }
        return clone;
    }

    // ====================
    // ===== VICTOIRE =====
    // ====================
    int estGagnant() {
        if(couronne.positionCouronne() >= EXTREMITE_DROITE_DU_PLATEAU - 1){
            joueurGagnant = JOUEUR_DROIT;
            return COURONNE_GAGNANTE;
        }
        if(couronne.positionCouronne() <= EXTREMITE_GAUCHE_DU_PLATEAU + 1){
            joueurGagnant = JOUEUR_GAUCHE;
            return COURONNE_GAGNANTE;
        }
        if(roi.positionPersonnage() >= EXTREMITE_DROITE_DU_PLATEAU - 1){
            joueurGagnant = JOUEUR_DROIT;
            return ROI_GAGNANT;
        }
        if(roi.positionPersonnage() <= EXTREMITE_GAUCHE_DU_PLATEAU + 1){
            joueurGagnant = JOUEUR_GAUCHE;
            return ROI_GAGNANT;
        }
        return AUCUN_GAGNANT;
    }

    public int joueurGagnant() {
        return joueurGagnant;
    }

    // ====================
    // ===== COURONNE =====
    // ====================
    void deplacerCouronne(int deplacementCouronne) {
        int nouvellePositionCouronne = couronne.positionCouronne() + deplacementCouronne;
        if (nouvellePositionCouronne > EXTREMITE_DROITE_DU_PLATEAU) {
            couronne.positionnerCouronne(EXTREMITE_DROITE_DU_PLATEAU);
        } else if (nouvellePositionCouronne < EXTREMITE_GAUCHE_DU_PLATEAU) {
            couronne.positionnerCouronne(EXTREMITE_GAUCHE_DU_PLATEAU);
        } else {
            couronne.positionnerCouronne(nouvellePositionCouronne);
        }
    }

    void changerEtatCouronne() {
        couronne.changerEtatCouronne();
    }

    public int valeurDeplacementCouronne() {
        int valeurDeplacementCouronne = 0;
        switch (joueurCourant) {
            case JOUEUR_GAUCHE:
                if ((gardeGauche.positionPersonnage() < CENTRE_DU_PLATEAU) && (roi.positionPersonnage() < CENTRE_DU_PLATEAU) && (gardeDroit.positionPersonnage() < CENTRE_DU_PLATEAU)) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne - 1;
                }
                if (gardeGauche.positionPersonnage() < ENTREE_CHATEAU_GAUCHE) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne - 1;
                }
                if (roi.positionPersonnage() < ENTREE_CHATEAU_GAUCHE) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne - 1;
                }
                if (fou.positionPersonnage() < ENTREE_CHATEAU_GAUCHE) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne - 1;
                }
                if (sorcier.positionPersonnage() < ENTREE_CHATEAU_GAUCHE) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne - 1;
                }
                break;
            case JOUEUR_DROIT:
                if ((gardeGauche.positionPersonnage() > CENTRE_DU_PLATEAU) && (roi.positionPersonnage() > CENTRE_DU_PLATEAU) && (gardeDroit.positionPersonnage() > CENTRE_DU_PLATEAU)) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
                if (gardeGauche.positionPersonnage() > ENTREE_CHATEAU_DROIT) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
                if (gardeDroit.positionPersonnage() > ENTREE_CHATEAU_DROIT) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
                if (roi.positionPersonnage() > ENTREE_CHATEAU_DROIT) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
                if (fou.positionPersonnage() > ENTREE_CHATEAU_DROIT) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
                if (sorcier.positionPersonnage() > ENTREE_CHATEAU_DROIT) {
                    valeurDeplacementCouronne = valeurDeplacementCouronne + 1;
                }
            default:
                break;
        }
        return valeurDeplacementCouronne;
    }

    // ==================
    // ===== JOUEUR =====
    // ==================
    boolean numeroJoueurValide(int numeroJoueurVoulu) {
        if (numeroJoueurVoulu == JOUEUR_DROIT || numeroJoueurVoulu == JOUEUR_GAUCHE) {
            return true;
        } else {
            Configuration.instance().logger().warning("Numero de joueur invalide !!");
            return false;
        }
    }

    public void changerJoueurCourant() {
        if (joueurCourant == JOUEUR_DROIT) {
            joueurCourant = JOUEUR_GAUCHE;
        } else {
            joueurCourant = JOUEUR_DROIT;
        }
    }

    public int joueurCourant() {
        return joueurCourant;
    }

    // ====================
    // ===== ELEMENTS =====
    // ====================
    void echangerFouSorcier() {
        fou.positionnerPersonnage(POSITION_BASE_SORCIER);
        sorcier.positionnerPersonnage(POSITION_BASE_FOU);
    }

    // ================================
    // ========== HISTORIQUE ==========
    // ================================
    public Coup creerCoup(Plateau plat) {
        Coup resultat = new Coup();
        if (joueurGagnant() == AUCUN_GAGNANT) {
            resultat.plateauCoup(plat);
            return resultat;
        } else {
            return null;
        }
    }

    public void aucunGagnant(){
        joueurGagnant = AUCUN_GAGNANT;
        victoirePioche = false;
    }

    public void jouerCoup(Coup coup) {
        coup.fixerPlateau(this);
        nouveau(coup);
    }

    void jouerCoupPlateau(Plateau plato) {
        transfertPlateau(plato);
    }

    void transfertPlateau(Plateau p) {

        joueurGagnant = p.joueurGagnant;
        joueurCourant = p.joueurCourant();

        gardeGauche.positionnerPersonnage(p.gardeGauche.positionPersonnage());
        gardeDroit.positionnerPersonnage(p.gardeDroit.positionPersonnage());
        roi.positionnerPersonnage(p.roi.positionPersonnage());
        fou.positionnerPersonnage(p.fou.positionPersonnage());
        sorcier.positionnerPersonnage(p.sorcier.positionPersonnage());
        
        couronne.positionnerCouronne(p.couronne.positionCouronne());
        couronne.definirEtatCouronne(p.couronne.etatCouronne());

        remplacerPioche(p);
        remplacerDefausse(p);
        remplacerMains(p);
        
        remplacerTourActuel();
    }

    public void remplacerPioche(Plateau p) {
        while(!paquet.pioche.estVide()) {
            paquet.pioche.extraitTete();
        }
        Sequence<Carte> liste = Configuration.instance().nouvelleSequence();
        while(!p.paquet.pioche.estVide()){
            liste.insereQueue(p.paquet.pioche.extraitTete());
        }
        while(!liste.estVide()){
            Carte carte = liste.extraitTete();
            paquet.pioche.insereQueue(carte);
            p.paquet.pioche.insereQueue(carte);
        }
    }

    public void remplacerDefausse(Plateau p) {
        while(!paquet.defausse.estVide()){
            paquet.defausse.extraitTete();
        }
        Sequence<Carte> liste = Configuration.instance().nouvelleSequence();
        while(!p.paquet.defausse.estVide()){
            liste.insereQueue(p.paquet.defausse.extraitTete());
        }
        while(!liste.estVide()){
            Carte carte = liste.extraitTete();
            paquet.defausse.insereQueue(carte);
            p.paquet.defausse.insereQueue(carte);
        }
    }

    public void remplacerMains(Plateau p) {
        int i = 0;
        while(i < 8 ){
            paquet.mainJoueurs[0][i] = p.paquet.mainJoueur(0)[i];
            paquet.mainJoueurs[1][i] = p.paquet.mainJoueur(1)[i];
            i++;
        }
    }

    public void remplacerTourActuel() {
        while(!paquet.tourActuel.estVide()){
            paquet.tourActuel.extraitTete();
        }
    }

    /*
    Plateau determinerPlateauHistorique(Plateau plateau) {
        Plateau plateauHistorique = new Plateau();
        plateauHistorique.transfertPlateau(plateau);
        return plateauHistorique;
    }

    public int tailleHistorique() {
        
        int tailleHistorique = 0;
        Sequence<PlateauHistorique> sequencePlateau = Configuration.instance().nouvelleSequence();
        while(!passe.estVide()) {
            sequencePlateau.insereQueue(passe.extraitTete());
            tailleHistorique ++;
        }
        while(!sequencePlateau.estVide()) {
            passe.insereQueue(sequencePlateau.extraitTete());
        }
        
        sequencePlateau = Configuration.instance().nouvelleSequence();
        while(!futur.estVide()) {
            sequencePlateau.insereQueue(futur.extraitTete());
            tailleHistorique ++;
        } 
        while(!sequencePlateau.estVide()) {
            futur.insereQueue(sequencePlateau.extraitTete());
        }
        
        return tailleHistorique;
    }
    */

    // =====================
    // ===== AFFICHER  =====
    // =====================
    public void afficherPlateau() {
        afficher("");

        // CHIFFRES
        afficher("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        String ligneChiffre = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i < 0) {
                ligneChiffre = ligneChiffre + "| " + i;
            } else {
                ligneChiffre = ligneChiffre + "| " + i + " ";
            }

        }
        ligneChiffre = ligneChiffre + "|";
        afficher(ligneChiffre);

        // COUR
        afficher("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
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
        afficher(ligneElement);

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
        afficher(ligneFou);

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
        afficher(ligneSorcier);

        // COURONNE
        afficher("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        String ligneCouronne = "";
        for (int i = EXTREMITE_GAUCHE_DU_PLATEAU; i < EXTREMITE_DROITE_DU_PLATEAU + 1; i++) {
            if (i == couronne.positionCouronne()) {
                if(couronne.etatCouronne()){
                    ligneCouronne = ligneCouronne + "| C ";
                }
                else{
                    ligneCouronne = ligneCouronne + "| c ";
                }
                
            } else {
                ligneCouronne = ligneCouronne + "|   ";
            }
        }
        ligneCouronne = ligneCouronne + "|";
        afficher(ligneCouronne);
        afficher("+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+");
        afficher("");
    }

    private void afficher(String string) {
        System.out.println(string);
    }
}
