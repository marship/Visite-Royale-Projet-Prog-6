package Modele;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Pattern.Observable;

public class Plateau extends Historique<Coup> implements Cloneable {

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
    int joueurCourant = JOUEUR_DROIT;

    // ====================
    // ===== ELEMENTS =====
    // ====================
    Personnage gardeGauche;
    Personnage gardeDroit;
    Personnage roi;
    Personnage fou;
    Personnage sorcier;
    Couronne couronne;
    Paquet paquet;
    Element dernierPersoJouer;
    Personnage queDeplaceFou;

    // ============================
    // ===== VALEURS ELEMENTS =====
    // ============================
    static final Element GARDE_GAUCHE = Element.GARDE_GAUCHE;
    static final Element GARDE_DROIT = Element.GARDE_DROIT;
    static final Element ROI = Element.ROI;
    static final Element FOU = Element.FOU;
    static final Element SORCIER = Element.SORCIER;
    static final Element VIDE = Element.VIDE;

    static final Deplacement UN = Deplacement.UN;

    Plateau() {
        initialisation();
    }

    void initialisation() {
        joueurCourant = JOUEUR_DROIT;

        gardeGauche = new Personnage(GARDE_GAUCHE, POSITION_BASE_GARDE_GAUCHE, false);
        gardeDroit = new Personnage(GARDE_DROIT, POSITION_BASE_GARDE_DROIT, false);
        roi = new Personnage(ROI, POSITION_BASE_ROI, false);
        fou = new Personnage(FOU, POSITION_BASE_FOU, true);
        sorcier = new Personnage(SORCIER, POSITION_BASE_SORCIER, true);
        couronne = new Couronne();
        dernierPersoJouer = VIDE;
        queDeplaceFou = fou;

        paquet = new Paquet();
    }

    // ====================
    // ===== COURONNE =====
    // ====================
    void deplacerCouronne(int deplacementElement) {
        int nouvellePositionCouronne = couronne.positionCouronne() + deplacementElement;
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

    // ====================
    // ===== POUVOIRS =====
    // ====================
    public void activerPouvoirSorcier(Element element) {
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

                if (validationTeleportation(element, teleporter)) {
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
                Configuration.instance().logger()
                        .info("Teleportation " + element.name() + " sur le sorcier non autorise !!");
                break;
        }
    }

    private boolean validationTeleportation(Element element, int teleporter) {
        return validationDeplacement(element, teleporter);
    }

    public void activerPouvoirFou(Element element) {
        int positionFou = fou.positionPersonnage();
        int positionRoi = roi.positionPersonnage();
        if (joueurCourant == JOUEUR_DROIT) {
            if (positionFou > positionRoi) {
                jouerCarte(element);
            } else {
                Configuration.instance().logger()
                        .info("Pouvoir du Fou inutilisable pour le joueur de droite !!");
            }
        } else {
            if (positionFou < positionRoi) {
                jouerCarte(element);
            } else {
                Configuration.instance().logger()
                        .info("Pouvoir du Fou inutilisable pour le joueur de gauche !!");
            }
        }
    }

    // =================
    // ===== CLONE =====
    // =================
    @Override
    public Plateau clone() {
        Plateau clone = null;
        try {
            clone = (Plateau) super.clone();

            clone.gardeGauche = new Personnage(gardeGauche.nomPersonnage, gardeGauche.positionPersonnage, gardeGauche.capaciteSpecial);
            clone.gardeDroit = new Personnage(gardeDroit.nomPersonnage, gardeDroit.positionPersonnage, gardeDroit.capaciteSpecial);
            clone.roi = new Personnage(roi.nomPersonnage, roi.positionPersonnage, roi.capaciteSpecial);
            clone.fou = new Personnage(fou.nomPersonnage, fou.positionPersonnage, fou.capaciteSpecial);
            clone.sorcier = new Personnage(sorcier.nomPersonnage, sorcier.positionPersonnage, sorcier.capaciteSpecial);
            clone.couronne = new Couronne();
            clone.couronne.definirEtatCouronne(couronne.etatCouronne());
            clone.couronne.positionnerCouronne(couronne.positionCouronne());
            clone.paquet = new Paquet(); // Modif
            return clone;

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne serieux avec le clone");
            System.exit(1);
        }
        return clone;
    }

    public Plateau(Plateau p) {
        partieTerminee = p.partieTerminee;
        partieEnCours = p.partieEnCours;
        joueurCourant = p.joueurCourant;

        couronne = p.couronne;
        gardeGauche = p.gardeGauche;
        gardeDroit = p.gardeDroit;
        roi = p.roi;
        fou = p.fou;
        sorcier = p.sorcier;

        paquet = p.paquet;
    }

    public void majDeplaceFou(Element element){
        switch (element) {
            case FOU:
                queDeplaceFou = fou;
                break;
            case ROI:
                queDeplaceFou = roi;
                break;
            case GARDES:
                queDeplaceFou = gardeGauche;
                break;

            case SORCIER:
                queDeplaceFou = sorcier;
        
            default:
                break;
        }
    }

    public int[] listeCarteJouable() {
        int nbCartes = paquet.nombreCartesEnMain();
        int i = 0;
        int[] res;
        if (dernierPersoJouer == VIDE) {
            res = initTableau(nbCartes, 1);
        } else {
            res = new int[nbCartes];
            while (i < nbCartes) {
                Carte carte = paquet.mainJoueur(joueurCourant)[i];
                if (carte.personnage() == dernierPersoJouer) {
                    res[i] = 1;
                } else {
                    res[i] = 0;
                }
                i++;
            }
        }
        return res;
    }

    public int[] initTableau(int taille, int defaut) {
        int[] res = new int[taille];
        int i = 0;
        while (i < taille) {
            res[i] = defaut;
            i++;
        }
        return res;
    }

    public void jouerCarte(int position) {
        dernierPersoJouer = paquet.mainJoueur(joueurCourant)[position].personnage();
        paquet.enleverCarte(joueurCourant, position);
    }

    public int positionSelon16(int perso){
        if(perso < 0){
            return 8 - perso;
        }
        if(perso == 0){
            return 8;
        }
        else{
            return 8 + perso;
        }
    }

    public int[] listeDeplacementPossiblesDepuisCarte(int position){
        int[] res = initTableau(TAILLE_DU_PLATEAU, 0);
        Carte carte = paquet.mainJoueur(joueurCourant)[position];
        switch (carte.personnage()) {
            case GARDE_GAUCHE:
                switch (carte.deplacement()) {
                    case UN:
                        if(validationDeplacement(GARDE_GAUCHE, carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeGauche.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_GAUCHE, -carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeGauche.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeDroit.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, -carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeDroit.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        break;
                
                    case UN_PLUS_UN:
                        if(validationDeplacement(GARDE_GAUCHE, 1) && validationDeplacement(GARDE_DROIT, 1)){
                            res[positionSelon16(gardeGauche.positionPersonnage()) + 1] = 1;
                            res[positionSelon16(gardeDroit.positionPersonnage()) + 1] = 1;
                        }
                        if(validationDeplacement(GARDE_GAUCHE, -1) && validationDeplacement(GARDE_DROIT, -1)){
                            res[positionSelon16(gardeGauche.positionPersonnage()) - 1] = 1;
                            res[positionSelon16(gardeDroit.positionPersonnage()) - 1] = 1;
                        }
                        if(validationDeplacement(GARDE_GAUCHE, 2)){
                            res[positionSelon16(gardeGauche.positionPersonnage()) + 2] = 1;
                        }
                        if(validationDeplacement(GARDE_GAUCHE, -2)){
                            res[positionSelon16(gardeGauche.positionPersonnage()) - 2] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, 2)){
                            res[positionSelon16(gardeDroit.positionPersonnage()) + 2] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, -2)){
                            res[positionSelon16(gardeDroit.positionPersonnage()) - 2] = 1;
                        }
                        break;

                    case RAPPROCHE:
                        res[positionSelon16(roi.positionPersonnage()) - 1] = 1;
                        res[positionSelon16(roi.positionPersonnage()) + 1] = 1;
                        break;

                    default:
                        break;
                }
                break;
                
            case ROI:
                if(validationDeplacement(ROI, carte.deplacement().getValeurDeplacement())){
                    res[positionSelon16(roi.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                }
                if(validationDeplacement(ROI, -carte.deplacement().getValeurDeplacement())){
                    res[positionSelon16(roi.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                }
                break;

            case FOU:
                if(queDeplaceFou == gardeGauche){
                    if(carte.deplacement() == Deplacement.MILIEU){
                        if(gardeGauche.positionPersonnage() < 0){
                            if(validationDeplacement(GARDE_GAUCHE, Math.abs(gardeGauche.positionPersonnage()))){
                                res[8] = 1;
                            }
                        }
                        else{
                            if(validationDeplacement(GARDE_GAUCHE, -gardeGauche.positionPersonnage())){
                                res[8] = 1;
                            }
                        }
                        if(gardeDroit.positionPersonnage() < 0){
                            if(validationDeplacement(GARDE_DROIT, Math.abs(gardeDroit.positionPersonnage()))){
                                res[8] = 1;
                            }
                        }
                        else{
                            if(validationDeplacement(GARDE_DROIT, -gardeDroit.positionPersonnage())){
                                res[8] = 1;
                            }
                        }
                    }
                    else{
                        if(validationDeplacement(GARDE_GAUCHE, carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeGauche.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_GAUCHE, -carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeGauche.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeDroit.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(GARDE_DROIT, -carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(gardeDroit.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                        }
                    }
                }
                else{
                    if(carte.deplacement() == Deplacement.MILIEU){
                        if(queDeplaceFou.positionPersonnage() < 0){
                            if(validationDeplacement(queDeplaceFou.typePersonnage(), Math.abs(queDeplaceFou.positionPersonnage()))){
                                res[8] = 1;
                            }
                        }
                        else{
                            if(validationDeplacement(queDeplaceFou.typePersonnage(), -queDeplaceFou.positionPersonnage())){
                                res[8] = 1;
                            }
                        }
                    }
                    else{
                        if(validationDeplacement(queDeplaceFou.typePersonnage(), carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(queDeplaceFou.positionPersonnage()) + carte.deplacement().getValeurDeplacement()] = 1;
                        }
                        if(validationDeplacement(queDeplaceFou.typePersonnage(), -carte.deplacement().getValeurDeplacement())){
                            res[positionSelon16(queDeplaceFou.positionPersonnage()) - carte.deplacement().getValeurDeplacement()] = 1;
                        }
                    }
                }
                break;

            case SORCIER:
                if(validationDeplacement(SORCIER, carte.deplacement().getValeurDeplacement())){
                    res[sorcier.positionPersonnage() + carte.deplacement().getValeurDeplacement()] = 1;
                }
                if(validationDeplacement(SORCIER, -carte.deplacement().getValeurDeplacement())){
                    res[sorcier.positionPersonnage() - carte.deplacement().getValeurDeplacement()] = 1;
                }
                break;
            default:
                break;
        }
        return res;
    }

    public int[] listeDeplacementPossiblesDepuisPerso(int position) {
        return null;
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
                        Configuration.instance().logger()
                                .info("Pouvoir du Fou inutilisable pour le joueur de droite !!");
                    }
                } else {
                    if (positionFou < positionRoi) {
                        jouerCarte(element);
                    } else {
                        Configuration.instance().logger()
                                .info("Pouvoir du Fou inutilisable pour le joueur de gauche !!");
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
                            Configuration.instance().logger()
                                    .info("Teleportation " + element.name() + " impossible !!");
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
                            Configuration.instance().logger()
                                    .info("Teleportation " + element.name() + " impossible !!");
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
                            Configuration.instance().logger()
                                    .info("Teleportation " + element.name() + " impossible !!");
                        }
                        break;
                    default:
                        Configuration.instance().logger()
                                .info("Teleportation " + element.name() + " sur le sorcier non autorise !!");
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
