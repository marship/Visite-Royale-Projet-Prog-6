package Vue;

import Modele.Jeu;
import Modele.Plateau;
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
    ImagePlateau imagePlateau;
    int largeur;
    int hauteur;


    public PlateauGraphique(Jeu j) {
        imagePlateau = chargeImage("plateau");
        jeu = j;
        //plateau = jeu.plateau();
    }

    @Override
    public void paintComponent(Graphics g) {

        // Graphics 2D est le vrai type de l'objet passé en paramètre
        // Le cast permet d'avoir acces a un peu plus de primitives de dessin
        dessinable = (Graphics2D) g;
        
        // On reccupere quelques infos provenant de la partie JComponent
        largeur = getWidth();
        hauteur = getHeight();
        // On efface tout
        dessinable.clearRect(0, 0, largeur, hauteur);
        tracerPlateau();
                
    }
    public void tracerPlateau(){

        tracerImage(imagePlateau, 100, 100, 100, 100);
        //int Gg = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        //int Gd = jeu.obtenirPositionElement(Element.GARDE_DROIT);
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
        repaint();
    }
}