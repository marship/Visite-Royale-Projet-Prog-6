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
    int largeurCase, hauteurCase;
    int taillePlateau = 0;
    Graphics2D dessinable;
    ImagePlateau imagePlateau, imageCouronne, imageGarde, imageRoi, imageFou, imageSorcier, imagePioche, imageCarte, imageCarteErreur;
    int largeurFenetre = 0;
    int hauteurFenetre = 0;


    public PlateauGraphique(Jeu j) {
        imagePlateau = chargeImage("plateau");
        imageCouronne = chargeImage("couronne");
        imageGarde = chargeImage("garde");
        imageRoi = chargeImage("roi");
        imageFou = chargeImage("fou");
        imageSorcier = chargeImage("sorcier");
        //imagePioche = chargeImage("pioche");
        imageCarte = chargeImage("carte");
        imageCarteErreur = chargeImage("carteErreur");

        jeu = j;
        //plateau = jeu.plateau();
    }

    @Override
    public void paintComponent(Graphics g) {

        // Graphics 2D est le vrai type de l'objet passé en paramètre
        // Le cast permet d'avoir acces a un peu plus de primitives de dessin
        dessinable = (Graphics2D) g;
        
        // On reccupere quelques infos provenant de la partie JComponent
        largeurFenetre = getWidth();
        hauteurFenetre = getHeight();
        taillePlateau = jeu.obtenirInfoPlateau(InfoPlateau.TAILLE_DU_PLATEAU);
        // On efface tout
        dessinable.clearRect(0, 0, largeurFenetre, hauteurFenetre);
        tracerPlateau();
        afficherCartesJoueurCourant();        
    }
    public void tracerPlateau(){

        largeurCase = largeurFenetre/taillePlateau;

        hauteurCase = 4*hauteurFenetre/7;
        System.out.println(hauteurFenetre);
        int hauteurLigneCouronne = hauteurFenetre/7;
        int hauteurLigneFou = hauteurLigneCouronne + hauteurCase/4;
        int hauteurLigneCortege = hauteurLigneCouronne + 2*hauteurCase/4;
        int hauteurLigneSorcier = hauteurLigneCouronne + 3*hauteurCase/4;

        tracerImage(imagePlateau, 0, hauteurFenetre/7, largeurFenetre, 4*hauteurFenetre/7);

        int Couronne = positionJeton(jeu.obtenirPositionElement(Element.COURONNE));
        tracerImage(imageCouronne, Couronne, hauteurLigneCouronne, largeurCase, hauteurCase/4);

        int Gg = positionJeton(jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        tracerImage(imageGarde, Gg, hauteurLigneCortege, largeurCase, hauteurCase/4);

        int Roi = positionJeton(jeu.obtenirPositionElement(Element.ROI));
        tracerImage(imageRoi, Roi, hauteurLigneCortege, largeurCase, hauteurCase/4);

        int Gd = positionJeton(jeu.obtenirPositionElement(Element.GARDE_DROIT));
        tracerImage(imageGarde, Gd, hauteurLigneCortege, largeurCase, hauteurCase/4);
        
        int Fou = positionJeton(jeu.obtenirPositionElement(Element.FOU));
        tracerImage(imageFou, Fou, hauteurLigneFou, largeurCase, hauteurCase/4);

        int Sorcier = positionJeton(jeu.obtenirPositionElement(Element.SORCIER));
        tracerImage(imageSorcier, Sorcier, hauteurLigneSorcier, largeurCase, hauteurCase/4);        
    }

    public void afficherCartesJoueurCourant(){
        Carte [] cartesJoueurCourant = jeu.recupererMainJoueur(jeu.joueurCourant());
        for(int i = 0; i<cartesJoueurCourant.length; i++){
            switch(cartesJoueurCourant[i].personnage()){
                case ROI:
                    tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                    break;
                case FOU:
                    switch(cartesJoueurCourant[i].deplacement()){
                        case UN:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case DEUX:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case TROIS:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case QUATRE:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case CINQ:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case MILIEU:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        default:
                            tracerImage(imageCarteErreur, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                    }
                    break;
                case SORCIER:
                    switch(cartesJoueurCourant[i].deplacement()){
                        case UN:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case DEUX:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case TROIS:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        default:
                            tracerImage(imageCarteErreur, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                    }
                    break;
                case GARDES:
                    switch(cartesJoueurCourant[i].deplacement()){
                        case UN:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        case UN_PLUS_UN:
                            tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                            break;
                        default:
                            tracerImage(imageCarteErreur, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                    }
                case VIDE:
                    tracerImage(imageCarte, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
                    break;
                default:
                    tracerImage(imageCarteErreur, (i+1)*largeurFenetre/16, 6*hauteurFenetre/7, largeurFenetre/16, hauteurFenetre/7);
            }
        }
    }

    public int positionJeton(int positionElement){
        return (positionElement+taillePlateau/2) * largeurCase;
    }

    private ImagePlateau chargeImage(String nomImage) {
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImage(in);
    }

    
    public void tracerImage(ImagePlateau image, int x, int y, int largeurCase, int hauteurCase) {
        dessinable.drawImage(image.image(), x, y, largeurCase, hauteurCase, null);
    }

    public int largeurCase() {
        return largeurCase;
    }

    public int hauteurCase() {
        return hauteurCase;
    }

    @Override
    public void miseAJour() {
    }
}