package Adaptateur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Global.Configuration;
import Vue.CollecteurEvenements;
import Vue.PlateauGraphique;

public class AdaptateurSouris extends MouseAdapter {

    // ======================
    // ===== CONSTANTES =====
    // ======================
    static final int MILIEU_PLATEAU = 8;

    static final int AUCUNE_OPTION = 0;
    static final int COORDONNEE_PLATEAU_X = 1;
    static final int COORDONNEE_PLATEAU_Y = 2;
    static final int COORDONNEE_MAIN_X = 3;

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    CollecteurEvenements collecteurEvenements;
    PlateauGraphique plateauGraphique;

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

        int clicSourisX = e.getX();
        int clicSourisY = e.getY();

        if (clicZone(clicSourisX, clicSourisY, true)) {
            clicSourisX = conversionCoordonnee(clicSourisX, COORDONNEE_PLATEAU_X);
            clicSourisY = conversionCoordonnee(clicSourisY, COORDONNEE_PLATEAU_Y);
            collecteurEvenements.clicPlateau(clicSourisX, clicSourisY);

        } else if (clicZone(clicSourisX, clicSourisY, false)) {
            clicSourisX = conversionCoordonnee(clicSourisX, COORDONNEE_MAIN_X);
            collecteurEvenements.clicCarte(clicSourisX);
        }
    }

    // ================
    // ===== CLIC =====
    // ================
    boolean clicZone(int clicX, int clicY, boolean zonePlateau) {
        if (zonePlateau) {
            return ((clicX >= plateauGraphique.debutPlateauX()) && (clicX <= plateauGraphique.largeurPlateau()) && (clicY >= plateauGraphique.debutPlateauY()) && (clicY <= plateauGraphique.hauteurPlateau()));
        } else {
            return ((clicX >= plateauGraphique.debutZoneCartesX()) && (clicX <= plateauGraphique.finZoneCartesX()) && (clicY >= plateauGraphique.debutZoneCartesY()) && (clicY <= plateauGraphique.finZoneCartesY()));
        }
    }

    // ======================
    // ===== CONVERSION =====
    // ======================
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