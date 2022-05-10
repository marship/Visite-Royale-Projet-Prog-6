package Vue;

import Modele.Plateau;
import Pattern.Observateur;

import javax.swing.*;
import java.awt.*;

public class PlateauGraphique extends JComponent implements Observateur {

    Plateau plateau;
    int largeurCase, hauteurCase;

    public PlateauGraphique(Plateau j) {
        plateau = j;
        plateau.ajouteObservateur(this);
    }

    @Override
    public void paintComponent(Graphics g) {

        /*
        Graphics2D dessinable = (Graphics2D) g;

        

        int lignes = plateau.hauteur();
        int colonnes = plateau.largeur();

        largeurCase = largeur() / colonnes;
        hauteurCase = hauteur() / lignes;

        dessinable.clearRect(0, 0, largeur(), hauteur());

        if (!plateau.enCours()) {
            dessinable.drawString("Fin", 20, hauteur() / 2);
        }

        // Grille
        for (int i = 1; i < lignes; i++) {
            dessinable.drawLine(0, i * hauteurCase, largeur(), i * hauteurCase);
            dessinable.drawLine(i * largeurCase, 0, i * largeurCase, hauteur());
        }

        // Coups
        for (int i = 0; i < lignes; i++)
            for (int j = 0; j < colonnes; j++)
                switch (plateau.valeur(i, j)) {
                    case 0:
                        dessinable.drawOval(j * largeurCase, i * hauteurCase, largeurCase, hauteurCase);
                        break;
                    case 1:
                        dessinable.drawLine(j * largeurCase, i * hauteurCase, (j + 1) * largeurCase, (i + 1) * hauteurCase);
                        dessinable.drawLine(j * largeurCase, (i + 1) * hauteurCase, (j + 1) * largeurCase, i * hauteurCase);
                        break;
                }
                */
    }

    int largeur() {
        return getWidth();
    }

    int hauteur() {
        return getHeight();
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