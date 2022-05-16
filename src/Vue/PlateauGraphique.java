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

    int taillePlateau = 0;
    Graphics2D dessinable;
    ImagePlateau imagePlateau, imageCouronne, imageGardeG, imageGardeD, imageRoi, imageFou, imageSorcier, imagePioche, imageCarte, imageCarteErreur, imageCarteVide;
    ImagePlateau imageFouUn, imageFouDeux, imageFouTrois, imageFouQuatre, imageFouCinq, imageFouM;
    ImagePlateau imageSorcierUn, imageSorcierDeux, imageSorcierTrois;
    ImagePlateau imageGardesUn, imageGardesUnPlusUn;
    int largeurFenetre = 0;
    int hauteurFenetre = 0;
    
    // TO DO !!!
    int largeurCaseCarte, hauteurCaseCarte = 0;
    int largeurCasePlateau, hauteurCasePlateau = 0;
    int debutPlateauX, debutPlateauY, finPlateauX, finPlateauY, quartHauteurPlateau = 0;

    // TO DO !!!
    int debutCartesX = 0;
    int debutCartesY = 0;
    int finCartesX = 0;
    int finCartesY = 0;


    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public PlateauGraphique(Jeu j) {
        
        imagePlateau = chargeImage("plateau");
        imageCouronne = chargeImage("jeton_Grande_Couronne");
        /*
        imageGardeD = chargeImage("jeton_Garde_Droit");
        imageGardeG = chargeImage("jeton_Garde_Gauche");
        imageRoi = chargeImage("jeton_Roi");
        imageFou = chargeImage("jeton_Fou");
        imageSorcier = chargeImage("jeton_Sorcier");
        imagePioche = chargeImage("dosCarte");
        imageCarteErreur = chargeImage("carteErreur");
        imageCarteVide = chargeImage("carteVide");
        */
/*
        imageFouUn = chargeImage("Fou_1");
        imageFouDeux = chargeImage("Fou_2"); 
        imageFouTrois = chargeImage("Fou_3");
        imageFouQuatre = chargeImage("Fou_4");
        imageFouCinq = chargeImage("Fou_5");
        imageFouM = chargeImage("Fou_M");

        imageSorcierUn = chargeImage("Sorcier_1");
        imageSorcierDeux = chargeImage("Sorcier_2");
        imageSorcierTrois = chargeImage("Sorcier_3");

        imageGardesUn = chargeImage("Garde_1");
        imageGardesUnPlusUn = chargeImage("Garde_1plus1");

        */
        jeu = j;
        //plateau = jeu.plateau();
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

        debutPlateauY = hauteurFenetre / 7;
        finPlateauX = largeurFenetre;
        finPlateauY = 4 * debutPlateauY;
        largeurCasePlateau = finPlateauX / taillePlateau;
        hauteurCasePlateau = finPlateauY;
        quartHauteurPlateau = hauteurCasePlateau / 4;

        dessinable.clearRect(0, 0, largeurFenetre, hauteurFenetre);

        tracerPlateau();
        afficherCartesJoueurCourant();        
    }

    // ===========================
    // ===== TRACER ELEMENTS =====
    // ===========================
    public void tracerPlateau() {

        tracerImage(imagePlateau, debutPlateauX, debutPlateauY, finPlateauX, finPlateauY);

        tracerImageElement(Element.COURONNE, imageCouronne);
        tracerImageElement(Element.GARDE_GAUCHE, imageGardeG);
        tracerImageElement(Element.ROI, imageRoi);
        tracerImageElement(Element.GARDE_DROIT, imageGardeD);
        tracerImageElement(Element.FOU, imageFou);
        tracerImageElement(Element.SORCIER, imageSorcier);
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

        Carte [] cartesJoueurCourant = jeu.recupererMainJoueur(jeu.joueurCourant());
        ImagePlateau image;

        for(int i = 0; i < cartesJoueurCourant.length; i++){

            debutCartesX = (i + 1) * largeurFenetre / 16;
            debutCartesY = 6 * debutPlateauY;
            finCartesX = largeurFenetre / 16;
            finCartesY = debutPlateauY;

            switch(cartesJoueurCourant[i].personnage()) {
                case ROI:
                    image = imageRoi;
                    break;
                case FOU:
                    switch(cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageFouUn;
                            break;
                        case DEUX:
                            image = imageFouDeux;
                            break;
                        case TROIS:
                            image = imageFouTrois;
                            break;
                        case QUATRE:
                            image = imageFouQuatre;
                            break;
                        case CINQ:
                            image = imageFouCinq;
                            break;
                        case MILIEU:
                            image = imageFouM;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case SORCIER:
                    switch(cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageSorcierUn;
                            break;
                        case DEUX:
                            image = imageSorcierDeux;
                            break;
                        case TROIS:
                            image = imageSorcierTrois;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case GARDES:
                    switch(cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageGardesUn;
                            break;
                        case UN_PLUS_UN:
                            image = imageGardesUnPlusUn;
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
            tracerImage(image, debutCartesX, debutCartesY, finCartesX, finCartesY);
        }
    }

    public int positionJeton(int positionElement){
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

    // =======================
    // ===== MISE A JOUR =====
    // =======================
    @Override
    public void miseAJour() {
        // TODO
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

    public int finPlateauX() {
        return finPlateauX;
    }

    public int finPlateauY() {
        return finPlateauY;
    }

    public int debutCartesX() {
        return debutCartesX;
    }

    public int debutCartesY() {
        return debutCartesY;
    }

    public int finCartesX() {
        return finCartesX;
    }

    public int finCartesY() {
        return finCartesY;
    }
}