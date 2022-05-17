package Vue;

import Modele.Jeu;
import Modele.Plateau;
import Modele.Carte;
import Pattern.Observateur;

import javax.swing.*;

import Global.Configuration;
import Global.Element;
import Global.InfoPlateau;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class PlateauGraphique extends JPanel implements Observateur {

    Jeu jeu;
    Plateau plateau;
    Graphics2D dessinable;

    // ==========================
    // ===== IMAGES PLATEAU =====
    // ==========================
    ImagePlateau imagePlateau, imageBandeauTour, imageCadreCartesPosees, imageCadrePiocheDefausse;

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
    int largeurCaseCarte, hauteurCaseCarte = 0;
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
        // TODO
    }

    // ===========================
    // ===== TRACER ELEMENTS =====
    // ===========================
    public void tracerPlateau() {

        tracerImage(imagePlateau, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);

        tracerImageElement(Element.COURONNE, imageJetonGrandeCouronne);
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
        int test = positionJeton(jeu.obtenirPositionElement(element));
        tracerImage(imageElement, test, hauteurElement, largeurCasePlateau, quartHauteurPlateau);
    }

    public void afficherCartesJoueurCourant() {

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
            tracerImage(image, (4+i)*debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
        }
    }

    public void afficherCartesAutreJoueur() {

        debutCartesX = largeurFenetre / 16;
        debutCartesY = 0;

        for (int i = 0; i < 8; i++) {
            tracerImage(imageDosCarte, (4+i)*debutCartesX, 0, largeurCarte, hauteurCarte);
        }
    }

    public void afficherZoneCartesJouees() {

        debutCartesX = largeurFenetre / 16;
        debutCartesY = 18 * hauteurFenetre / 28;
        tracerImage(imageCadreCartesPosees, 4*debutCartesX, debutCartesY, 8*largeurCarte, hauteurCarte);
        /*for (int i = 0; i < 8; i++) {
            tracerImage(imageDosCarte, (4+i)*debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
        }*/
    }

    public int positionJeton(int positionElement) {
        return (positionElement + taillePlateau / 2) * largeurCasePlateau;
    }

    // ==================
    // ===== IMAGES =====
    // ==================
    private ImagePlateau chargeImage(String nomImage) {
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImage(in);
    }

    public void tracerImage(ImagePlateau image, int x, int y, int largeurCase, int hauteurCase) {
        dessinable.drawImage(image.image(), x, y, largeurCase, hauteurCase, null);
    }
    //TODO charger images depuis le fichier de configuration
    //TODO etablir convention pour les noms des images
    private void chargementDesImages() {
        imagePlateau = chargeImage("plateau");

        imageJetonGrandeCouronne = chargeImage("jeton_Grande_Couronne");
        imageJetonPetiteCouronne = chargeImage("jeton_Petite_Couronne");

        imageJetonGardeGauche = chargeImage("jeton_Garde_Gauche");
        imageJetonGardeDroit = chargeImage("jeton_Garde_Droit");
        imageJetonRoi = chargeImage("jeton_Roi");
        imageJetonFou = chargeImage("jeton_Fou");
        imageJetonSorcier = chargeImage("jeton_Sorcier");

        imageCarteErreur = chargeImage("carteErreur");
        imageCarteVide = chargeImage("carteVide");

        imageDosCarte = chargeImage("dosCarte");

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
        imageCarteGardesRaproche = chargeImage("Garde_Raproche");

        imageBandeauTour = chargeImage("BandeauTour");
        imageCadreCartesPosees = chargeImage("cadreCartesPosees");
        imageCadrePiocheDefausse = chargeImage("cadrePiocheDefausse");
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

    public int largeurCaseCarte() {
        return largeurCaseCarte;
    }

    public int hauteurCaseCarte() {
        return hauteurCaseCarte;
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
        return 4*debutCartesX;
    }

    public int debutZoneCartesY() {
        return debutCartesY;
    }

    public int finZoneCartesX() {
        return debutZoneCartesX() + 8 * largeurCarte;
    }

    public int finZoneCartesY() {
        return debutCartesY+hauteurCarte;
    }

    public int largeurCarte() {
        return largeurCarte;
    }

    public int hauteurCarte() {
        return hauteurCarte;
    }
}