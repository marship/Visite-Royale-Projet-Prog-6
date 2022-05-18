package Vue;

import Modele.Jeu;
import Modele.Plateau;
import Modele.Carte;
import Pattern.Observateur;
import Structures.Sequence;

import javax.swing.*;

import Global.Configuration;
import Global.Element;
import Global.InfoPlateau;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class PlateauGraphique extends JPanel implements Observateur {

    static final int EPAISSEUR_BORDURE = 3;

    Jeu jeu;
    Plateau plateau;
    Graphics2D dessinable;

    // ==========================
    // ===== IMAGES PLATEAU =====
    // ==========================
    ImagePlateau imagePlateau, imagePlateauGauche, imagePlateauDroit, imageBandeauTour, imageCadreCartesPosees, imageCadrePiocheDefausse;

    // =========================
    // ===== IMAGES JETONS =====
    // =========================
    ImagePlateau imageJetonGrandeCouronne, imageJetonPetiteCouronne;
    ImagePlateau imageJetonGardeGauche, imageJetonGardeDroit, imageJetonRoi, imageJetonFou, imageJetonSorcier;

    // =========================
    // ===== IMAGES CARTES =====
    // =========================
    ImagePlateau imageCarteErreur, imageCarteVide, imageDosCarte;
    ImagePlateau imageCarteRoi;
    ImagePlateau imageCarteFouUn, imageCarteFouDeux, imageCarteFouTrois, imageCarteFouQuatre, imageCarteFouCinq,
            imageCarteFouM;
    ImagePlateau imageCarteSorcierUn, imageCarteSorcierDeux, imageCarteSorcierTrois;
    ImagePlateau imageCarteGardesUn, imageCarteGardesUnPlusUn, imageCarteGardesRaproche;

    // =============================================
    // ===== INFO POSITIONS ELEMENTS GRAPHIQUE =====
    // =============================================
    int taillePlateau = 0;
    int largeurFenetre, hauteurFenetre = 0;
    int largeurCasePlateau, hauteurCasePlateau = 0;
    int debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau, quartHauteurPlateau = 0;
    int debutCartesX, debutCartesY, largeurCarte, hauteurCarte = 0;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public PlateauGraphique(Jeu j) {
        chargementDesImages();
        jeu = j;
        jeu.ajouteObservateur(this);
        // plateau = jeu.plateau();
    }

    // ===========================
    // ===== PAINT COMPONENT =====
    // ===========================
    @Override
    public void paintComponent(Graphics g) {

        dessinable = (Graphics2D) g;

        largeurFenetre = getWidth();
        hauteurFenetre = getHeight();

        taillePlateau = jeu.obtenirInfoPlateau(InfoPlateau.TAILLE_DU_PLATEAU);

        debutPlateauY = 5 * hauteurFenetre / 28;
        largeurPlateau = largeurFenetre;
        hauteurPlateau = 3 * hauteurFenetre / 7;

        largeurCarte = largeurFenetre / 16;
        hauteurCarte = hauteurFenetre / 7;

        largeurCasePlateau = largeurPlateau / taillePlateau;
        hauteurCasePlateau = hauteurPlateau;
        quartHauteurPlateau = hauteurCasePlateau / 4;

        dessinable.clearRect(0, 0, largeurFenetre, hauteurFenetre);
        dessinable.setColor(new Color(23, 74, 11)); //TODO choisir couleur
        dessinable.fillRect(0, 0, largeurFenetre, hauteurFenetre);

        tracerPlateau();
        afficherCartesAutreJoueur();
        afficherZoneCartesJouees();
        afficherCartesJoueurCourant();
    }

    // =======================
    // ===== MISE A JOUR =====
    // =======================
    @Override
    public void miseAJour() {
        repaint();
    }

    // ===========================
    // ===== TRACER ELEMENTS =====
    // ===========================
    public void tracerPlateau() {

        if(jeu.joueurCourant() == 1){
            tracerImage(imagePlateauDroit, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);
        }
        else{
            tracerImage(imagePlateauGauche, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);
        }

        if(jeu.getEtatCouronne()){
            tracerImageElement(Element.COURONNE, imageJetonGrandeCouronne);
        }
        else{
            tracerImageElement(Element.COURONNE, imageJetonPetiteCouronne);
        }
        tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGauche);
        tracerImageElement(Element.ROI, imageJetonRoi);
        tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroit);
        tracerImageElement(Element.FOU, imageJetonFou);
        tracerImageElement(Element.SORCIER, imageJetonSorcier);
    }

    void tracerImageElement(Element element, ImagePlateau imageElement) {
        int hauteurElement = debutPlateauY;
        switch (element) {
            case COURONNE:
                break;
            case FOU:
                hauteurElement = hauteurElement + quartHauteurPlateau;
                break;
            case GARDE_GAUCHE:
            case GARDE_DROIT:
            case ROI:
                hauteurElement = hauteurElement + 2 * quartHauteurPlateau;
                break;
            case SORCIER:
                hauteurElement = hauteurElement + 3 * quartHauteurPlateau;
                break;
            default:
                break;
        }
        tracerImage(imageElement, positionJeton(jeu.obtenirPositionElement(element)), hauteurElement, largeurCasePlateau, quartHauteurPlateau);
    }

    public void afficherCartesJoueurCourant() {
        
        if(jeu.estPartieTerminee()){
            return;
        }

        Carte[] cartesJoueurCourant = jeu.recupererMainJoueur(jeu.joueurCourant());
        ImagePlateau image;

        debutCartesX = largeurFenetre / 16;
        debutCartesY = 6 * hauteurFenetre / 7;

        for (int i = 0; i < cartesJoueurCourant.length; i++) {
            switch (cartesJoueurCourant[i].personnage()) {
                case ROI:
                    image = imageCarteRoi;
                    break;
                case FOU:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteFouUn;
                            break;
                        case DEUX:
                            image = imageCarteFouDeux;
                            break;
                        case TROIS:
                            image = imageCarteFouTrois;
                            break;
                        case QUATRE:
                            image = imageCarteFouQuatre;
                            break;
                        case CINQ:
                            image = imageCarteFouCinq;
                            break;
                        case MILIEU:
                            image = imageCarteFouM;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case SORCIER:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteSorcierUn;
                            break;
                        case DEUX:
                            image = imageCarteSorcierDeux;
                            break;
                        case TROIS:
                            image = imageCarteSorcierTrois;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case GARDES:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteGardesUn;
                            break;
                        case UN_PLUS_UN:
                            image = imageCarteGardesUnPlusUn;
                            break;
                        case RAPPROCHE:
                            image = imageCarteGardesRaproche;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case VIDE:
                    image = imageCarteVide;
                    break;
                default:
                    image = imageCarteErreur;
                    break;
            }
            tracerImage(image, (4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
        }
    }

    public void afficherCartesAutreJoueur() {

        debutCartesX = largeurFenetre / 16;
        debutCartesY = 0;

        for (int i = 0; i < 8; i++) {
            tracerImage(imageDosCarte, (4 + i) * debutCartesX, 0, largeurCarte, hauteurCarte);
        }
    }

    public void afficherZoneCartesJouees() {

        ImagePlateau image;
        debutCartesX = largeurFenetre / 16;
        debutCartesY = 18 * hauteurFenetre / 28;
        tracerImage(imageCadreCartesPosees, 4*debutCartesX, debutCartesY, 8*largeurCarte, hauteurCarte);
        Sequence <Carte> cartesJouees = Configuration.instance().nouvelleSequence();; 
        cartesJouees = jeu.plateau().paquet.copieSequence(jeu.plateau().paquet.tourActuel());
        Carte carte;
        int i = 0;
        while (!cartesJouees.estVide()) {
            carte = cartesJouees.extraitTete();
            switch (carte.personnage()) {
                case ROI:
                    image = imageCarteRoi;
                    break;
                case FOU:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteFouUn;
                            break;
                        case DEUX:
                            image = imageCarteFouDeux;
                            break;
                        case TROIS:
                            image = imageCarteFouTrois;
                            break;
                        case QUATRE:
                            image = imageCarteFouQuatre;
                            break;
                        case CINQ:
                            image = imageCarteFouCinq;
                            break;
                        case MILIEU:
                            image = imageCarteFouM;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case SORCIER:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteSorcierUn;
                            break;
                        case DEUX:
                            image = imageCarteSorcierDeux;
                            break;
                        case TROIS:
                            image = imageCarteSorcierTrois;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case GARDES:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteGardesUn;
                            break;
                        case UN_PLUS_UN:
                            image = imageCarteGardesUnPlusUn;
                            break;
                        case RAPPROCHE:
                            image = imageCarteGardesRaproche;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case VIDE:
                    image = imageCarteVide;
                    break;
                default:
                    image = imageCarteErreur;
                    break;
            }
            tracerImage(image, (4+i)*debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            i++;
        }
    }

    public int positionJeton(int positionElement) {
        return (positionElement + taillePlateau / 2) * largeurCasePlateau;
    }

    // ==================
    // ===== IMAGES =====
    // ==================
    private ImagePlateau chargeImage(String nomImage) {
        System.out.println("Chargement de l'image : " + nomImage);
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImage(in);
    }

    public void tracerImage(ImagePlateau image, int x, int y, int largeurCase, int hauteurCase) {
        dessinable.drawImage(image.image(), x, y, largeurCase, hauteurCase, null);
    }
    //TODO charger images depuis le fichier de configuration
    private void chargementDesImages() {
        imagePlateau = chargeImage("Plateau");
        imagePlateauDroit = chargeImage("Previsualisation_Droite");
        imagePlateauGauche = chargeImage("Previsualisation_Gauche");

        imageJetonGrandeCouronne = chargeImage("Jeton_Grande_CouronneV2");
        imageJetonPetiteCouronne = chargeImage("Jeton_Petite_CouronneV2");

        imageJetonGardeGauche = chargeImage("Jeton_Garde_Gauche");
        imageJetonGardeDroit = chargeImage("Jeton_Garde_Droit");
        imageJetonRoi = chargeImage("Jeton_Roi");
        imageJetonFou = chargeImage("Jeton_Fou");
        imageJetonSorcier = chargeImage("Jeton_Sorcier");

        imageCarteErreur = chargeImage("Carte_Erreur");
        imageCarteVide = chargeImage("Carte_Vide");

        imageDosCarte = chargeImage("Dos_2");

        imageCarteRoi = chargeImage("Roi_1");

        imageCarteFouUn = chargeImage("Fou_1");
        imageCarteFouDeux = chargeImage("Fou_2");
        imageCarteFouTrois = chargeImage("Fou_3");
        imageCarteFouQuatre = chargeImage("Fou_4");
        imageCarteFouCinq = chargeImage("Fou_5");
        imageCarteFouM = chargeImage("Fou_M");

        imageCarteSorcierUn = chargeImage("Sorcier_1");
        imageCarteSorcierDeux = chargeImage("Sorcier_2");
        imageCarteSorcierTrois = chargeImage("Sorcier_3");

        imageCarteGardesUn = chargeImage("Garde_1");
        imageCarteGardesUnPlusUn = chargeImage("Garde_1plus1");
        imageCarteGardesRaproche = chargeImage("Garde_Rapproche");

        imageBandeauTour = chargeImage("Bandeau_Tour");
        imageCadreCartesPosees = chargeImage("Cadre_Cartes_Posees");
        imageCadrePiocheDefausse = chargeImage("Cadre_Pioche_Defausse");
    }

    // ============================
    // ===== TRACER RECTANGLE =====
    // ============================
    public void tracerRectangle(int x, int y, int largeurCarte, int hauteurCarte) {
        dessinable.setStroke(new BasicStroke(EPAISSEUR_BORDURE));
        if (masquerPrevisualisation()) {
            dessinable.setColor(Color.BLACK);
        } else {
            dessinable.setColor(Color.BLACK);  // YELLOW
        }
        dessinable.drawRect(x * debutZoneCartesX(), y * debutZoneCartesY(), largeurCarte(), hauteurCarte());
        System.out.println("x = " + x * debutZoneCartesX() + ", y = " + debutZoneCartesY() + ", larg = " + largeurCarte() + ", haut = " + hauteurCarte());
    }

    public boolean masquerPrevisualisation() {
        return masquerPrevisualisationNonAutorisee();
    }

    /*
    public boolean masquerPrevisualisationDebut() {
        return jeu.estAuDebut();
    }
    */

    public boolean masquerPrevisualisationNonAutorisee() {
        return !jeu.actionAutoriser();
    }

    // ===================================================
    // ===== INFO POSITION ELEMENT GRAPHIQUE PLATEAU =====
    // ===================================================
    public int largeurCasePlateau() {
        return largeurCasePlateau;
    }

    public int hauteurCasePlateau() {
        return hauteurCasePlateau;
    }

    public int debutPlateauX() {
        return debutPlateauX;
    }

    public int debutPlateauY() {
        return debutPlateauY;
    }

    public int largeurPlateau() {
        return largeurPlateau;
    }

    public int hauteurPlateau() {
        return hauteurPlateau + debutPlateauY;
    }

    public int debutZoneCartesX() {
        return 4 * debutCartesX;
    }

    public int debutZoneCartesY() {
        return debutCartesY;
    }

    public int finZoneCartesX() {
        return debutZoneCartesX() + 8 * largeurCarte;
    }

    public int finZoneCartesY() {
        return debutCartesY + hauteurCarte;
    }

    public int largeurCarte() {
        return largeurCarte;
    }

    public int hauteurCarte() {
        return hauteurCarte;
    }

    public int quartHauteurPlateau(){
        return quartHauteurPlateau;
    }
}