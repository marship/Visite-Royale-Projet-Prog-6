package Adaptateur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Global.Configuration;
import Vue.CollecteurEvenements;
import Vue.PlateauGraphique;

public class AdaptateurSouris extends MouseAdapter {

    static final int MILIEU_PLATEAU = 8;

    static final int AUCUNE_OPTION = 0;
    static final int COORDONNEE_PLATEAU_X = 1;
    static final int COORDONNEE_PLATEAU_Y = 2;
    static final int COORDONNEE_MAIN_X = 3;

    PlateauGraphique plateauGraphique;
    CollecteurEvenements collecteurEvenements;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurSouris(PlateauGraphique pGraphique, CollecteurEvenements cEvenements) {
        plateauGraphique = pGraphique;
        collecteurEvenements = cEvenements;
    }

    // =======================
    // ===== CLIC SOURIS =====
    // =======================
    @Override
    public void mousePressed(MouseEvent e) {

        int clicX = e.getX();
        int clicY = e.getY();

        if (estClicSurPlateau(clicX, clicY)) {
            clicX = conversionCoordonnee(clicX, COORDONNEE_PLATEAU_X);
            clicY = conversionCoordonnee(clicY, COORDONNEE_PLATEAU_Y);
            collecteurEvenements.clicPlateau(clicX, clicY);
            
        } else if (estClicSurMain(clicX, clicY)) {
            clicX = conversionCoordonnee(clicX, COORDONNEE_MAIN_X);
            collecteurEvenements.clicCarte(clicX);
        }
    }

    boolean estClicSurPlateau(int clicX, int clicY) {
        return (((clicX >= plateauGraphique.debutPlateauX()) && (clicX <= plateauGraphique.largeurPlateau())) && ((clicY >= plateauGraphique.debutPlateauY()) && (clicY <= plateauGraphique.hauteurPlateau())));
    }

    boolean estClicSurMain(int clicX, int clicY) {
        return (((clicX >= plateauGraphique.debutZoneCartesX()) && (clicX <= plateauGraphique.finZoneCartesX())) && ((clicY >= plateauGraphique.debutZoneCartesY()) && (clicY <= plateauGraphique.finZoneCartesY())));
    }

    int conversionCoordonnee(int clic, int option) {
        switch (option) {
            case COORDONNEE_PLATEAU_X:
                return (clic / plateauGraphique.largeurCasePlateau() - MILIEU_PLATEAU);
            case COORDONNEE_PLATEAU_Y:
                return (clic / plateauGraphique.quartHauteurPlateau() - 1);  
            case COORDONNEE_MAIN_X:
                return (clic / plateauGraphique.largeurCarte() - 4);     
            default:
                Configuration.instance().logger().warning("Option Invalide !!");
                return AUCUNE_OPTION;
        }
    }
}
