package Vue;

import Global.Configuration;
import Global.InfoJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MenuGraphique extends JPanel {
    
    Image imageMenuPrincipal, imageOptions, imageOptionsJeu, imageSelectionJoueurs, imageRegles, imageCredits, imageCharger;
    
    Graphics2D dessinable;
    int largeurFenetre;
    int hauteurFenetre;
    InfoJeu etatCourant;


    public MenuGraphique(InfoJeu etat) throws IOException{
        chargerImages();
        this.etatCourant = etat;

    }

    @Override
    public void paintComponent(Graphics g) {

        dessinable = (Graphics2D) g;

        largeurFenetre = getWidth();
        hauteurFenetre = getHeight();

        tracerMenu(etatCourant);

    }

    public void tracerMenu(InfoJeu etat){

        switch(etat){
            case MENU_PRINCIPAL :
                tracerImage(imageMenuPrincipal, 0, 0, largeurFenetre, hauteurFenetre);
                break;
            case OPTIONS_MENU :
                tracerImage(imageOptions, 0, 0, largeurFenetre, hauteurFenetre);
                break; 
            case SELECTION_JOUEURS :
                tracerImage(imageSelectionJoueurs, 0, 0, largeurFenetre, hauteurFenetre);
                break;
            case OPTIONS_JEU :
                tracerImage(imageOptionsJeu, 0, 0, largeurFenetre, hauteurFenetre);
                break;
            default :
                break;

        }

    }

    public void tracerImage(Image image, int x, int y, int largeurCase, int hauteurCase) {
        dessinable.drawImage(image, x, y, largeurCase, hauteurCase, null);
    }

    public void chargerImages() throws IOException{

        imageMenuPrincipal = chargeImage("Background_Menu");
        imageOptions = chargeImage("Background_Options");
        imageOptionsJeu = chargeImage("Background_Options");
        imageSelectionJoueurs = chargeImage("Background_Selection");

        //imageRegles = chargeImage("Jeton_Petite_CouronneV2");
        //imageCredits = chargeImage("Jeton_Petite_CouronneV2");
        //imageCharger = chargeImage("Jeton_Petite_CouronneV2");

    }

    private Image chargeImage(String nomImage) throws IOException {
        System.out.println("Chargement de l'image : " + nomImage);
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        BufferedImage test = ImageIO.read(in);
        return test;
    }

}
