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

        if (clicZone(clicSourisX, clicSourisY, "plateau")) {
            clicSourisX = conversionCoordonnee(clicSourisX, COORDONNEE_PLATEAU_X);
            clicSourisY = conversionCoordonnee(clicSourisY, COORDONNEE_PLATEAU_Y);
            collecteurEvenements.clicPlateau(clicSourisX, clicSourisY);

        } else if (clicZone(clicSourisX, clicSourisY, "carte")) {
            clicSourisX = conversionCoordonnee(clicSourisX, COORDONNEE_MAIN_X);
            collecteurEvenements.clicCarte(clicSourisX);
        }
        else if (clicZone(clicSourisX, clicSourisY, "annuler")) {
            collecteurEvenements.commande("Annuler");
        }
    }

    // ================
    // ===== CLIC =====
    // ================
    boolean clicZone(int clicX, int clicY, String nomZone) {
        switch(nomZone){
            case "plateau": 
                return ((clicX >= plateauGraphique.debutPlateauX()) && (clicX <= plateauGraphique.largeurPlateau()) && (clicY >= plateauGraphique.debutPlateauY()) && (clicY <= plateauGraphique.hauteurPlateau()));
            case "carte":
                return ((clicX >= plateauGraphique.debutZoneCartesX()) && (clicX <= plateauGraphique.finZoneCartesX()) && (clicY >= plateauGraphique.debutZoneCartesY()) && (clicY <= plateauGraphique.finZoneCartesY()));
            case "annuler":
                return ((clicX >= plateauGraphique.debutBoutonAnnulerX()) && (clicX <= plateauGraphique.largeurBoutonAnnuler()) && (clicY >= plateauGraphique.debutBoutonAnnulerY()) && (clicY <= plateauGraphique.hauteurBoutonAnnuler()));
            default:
                return false;
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