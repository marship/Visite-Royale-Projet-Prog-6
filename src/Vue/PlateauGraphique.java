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
    public static int taillePlateau = 17;
    Graphics2D dessinable;
    ImagePlateau imagePlateau, imageGarde, imageRoi, imageFou, imageSorcier, imagePioche, imageCarte;
    int largeurFenetre = 0;
    int hauteurFenetre = 0;


    public PlateauGraphique(Jeu j) {
        imagePlateau = chargeImage("plateau");
        imageGarde = chargeImage("garde");
        imageRoi = chargeImage("roi");
        imageFou = chargeImage("fou");
        imageSorcier = chargeImage("sorcier");
        //imagePioche = chargeImage("pioche");
        imageCarte = chargeImage("carte");

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
        // On efface tout
        dessinable.clearRect(0, 0, largeurFenetre, hauteurFenetre);
        tracerPlateau();
                
    }
    public void tracerPlateau(){

        largeurCase = largeurFenetre/jeu.obtenirInfoPlateau(InfoPlateau.TAILLE_DU_PLATEAU);

        //TODO valeur arbitraire à changer
        hauteurCase = hauteurFenetre-400;
        int hauteurLigneUne = 100 + hauteurCase/3;
        int hauteurLigneDeux = 100 + 2*hauteurCase/3;
        int hauteurLigneTrois = 100 + 3*hauteurCase/3;

        tracerImage(imagePlateau, 0, 100, largeurFenetre, hauteurFenetre-300);
        int Gg = positionJeton(jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        tracerImage(imageGarde, Gg, hauteurLigneDeux, largeurCase, hauteurCase/3);
        int Roi = positionJeton(jeu.obtenirPositionElement(Element.ROI));
        tracerImage(imageRoi, Roi, hauteurLigneDeux, largeurCase, hauteurCase/3);
        int Gd = positionJeton(jeu.obtenirPositionElement(Element.GARDE_DROIT));
        tracerImage(imageGarde, Gd, hauteurLigneDeux, largeurCase, hauteurCase/3);
        int Fou = positionJeton(jeu.obtenirPositionElement(Element.FOU));
        tracerImage(imageFou, Fou, hauteurLigneUne, largeurCase, hauteurCase/3);
        int Sorcier = positionJeton(jeu.obtenirPositionElement(Element.SORCIER));
        tracerImage(imageSorcier, Sorcier, hauteurLigneTrois, largeurCase, hauteurCase/3);

        afficherCartesJoueurCourant();
        

    }

    public void afficherCartesJoueurCourant(){
        Carte [] cartesJoueurCourant = jeu.recupererMainJoueur(jeu.joueurCourant());
        for(int i = 0; i<cartesJoueurCourant.length; i++){
            switch(cartesJoueurCourant[i].personnage()){
                case GARDE_GAUCHE:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case GARDE_DROIT:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case ROI:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case FOU:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case SORCIER:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case GARDES:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                case VIDE:
                    tracerImage(imageCarte, (i+1)*40, hauteurFenetre-200, 90, 180);
                    break;
                default:
                    break;
            }
        }
    }

    public int positionJeton(int positionElement){
        return (positionElement+8) * largeurCase;
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