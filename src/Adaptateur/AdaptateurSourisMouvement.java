package Adaptateur;

import java.awt.event.MouseMotionListener;

import Vue.CollecteurEvenements;
import Vue.PlateauGraphique;

import java.awt.event.MouseEvent;

public class AdaptateurSourisMouvement implements MouseMotionListener {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    CollecteurEvenements collecteurEvenements;
    PlateauGraphique plateauGraphique;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurSourisMouvement(PlateauGraphique pGraphique, CollecteurEvenements cEvenements) {
        plateauGraphique = pGraphique;
        collecteurEvenements = cEvenements;
    }

    // ===========================
    // ===== DEPLACER SOURIS =====
    // ===========================
    @Override
    public void mouseMoved(MouseEvent e) {

        int positionSourisX = e.getX();
        int positionSourisY = e.getY();

        if (survolerZone(positionSourisX, positionSourisY, false)) {
            positionSourisX = recalculerPositionCarte(positionSourisX);
            collecteurEvenements.passerSurCarte(positionSourisX);
        } else {
            collecteurEvenements.passerSurCarte(8);
        }

        if (survolerZone(positionSourisX, positionSourisY, true)) {
            positionSourisX = recalculerPositionCase(positionSourisX);
            collecteurEvenements.passerSurCase(positionSourisX);
        } else {
            collecteurEvenements.passerSurCase(-1);
        }
    }

    // ====================
    // ===== SURVOLER =====
    // ====================
    boolean survolerZone(int positionX, int positionY, boolean zonePlateau) {
        if (zonePlateau) {
            return ((positionX >= plateauGraphique.debutPlateauX()) && (positionX <= plateauGraphique.largeurPlateau()) && (positionY >= plateauGraphique.debutPlateauY()) && (positionY <= plateauGraphique.hauteurPlateau()));
        } else {
            return ((positionX >= plateauGraphique.debutZoneCartesX()) && (positionX <= plateauGraphique.finZoneCartesX()) && (positionY >= plateauGraphique.debutZoneCartesY()) && (positionY <= plateauGraphique.finZoneCartesY()));
        }
    }

    // ======================
    // ===== RECALCULER =====
    // ======================
    int recalculerPositionCarte(int posSourisX) {
        return posSourisX / plateauGraphique.largeurCarte() - 4;
    }

    int recalculerPositionCase(int posSourisX) {
        return posSourisX / plateauGraphique.largeurCasePlateau();
    }

    // =======================================
    // ===== MAINTENIR + DEPLACER SOURIS =====
    // =======================================
    @Override
    public void mouseDragged(MouseEvent e) {
        int positionSourisX = e.getX();
        int positionSourisY = e.getY();

        if (survolerZone(positionSourisX, positionSourisY, false)) {
            positionSourisX = recalculerPositionCarte(positionSourisX);
            collecteurEvenements.passerSurCarte(positionSourisX);
        } else {
            collecteurEvenements.passerSurCarte(8);
        }

        if (survolerZone(positionSourisX, positionSourisY, true)) {
            positionSourisX = recalculerPositionCase(positionSourisX);
            collecteurEvenements.passerSurCase(positionSourisX);
        } else {
            collecteurEvenements.passerSurCase(-1);
        }
    }
}