package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {

    static final int MILIEU_PLATEAU = 8;

    PlateauGraphique plateauGraphique;
    CollecteurEvenements collecteurEvenements;

    AdaptateurSouris(PlateauGraphique pGraphique, CollecteurEvenements cEvenements) {
        plateauGraphique = pGraphique;
        collecteurEvenements = cEvenements;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int clicX = e.getX();
        int clicY = e.getY();

        if (estClicSurPlateau(clicX, clicY)) {

            clicX = conversionCoordonnee(clicX, true);
            clicY = conversionCoordonnee(clicY, false);

            collecteurEvenements.clicPlateau(clicX, clicY);

        } else {
            if (clicX >= plateauGraphique.debutZoneCartesX() && clicX <= plateauGraphique.finZoneCartesX() && clicY >= plateauGraphique.debutZoneCartesY() && clicY <= plateauGraphique.finZoneCartesY()) {
                clicX = e.getX() / plateauGraphique.largeurCarte() - 4;
                collecteurEvenements.clicCarte(clicX);
            }
        }
    }

    boolean estClicSurPlateau(int clicX, int clicY) {
        return (((clicX >= plateauGraphique.debutPlateauX()) && (clicX <= plateauGraphique.largeurPlateau())) && ((clicY >= plateauGraphique.debutPlateauY()) && (clicY <= plateauGraphique.hauteurPlateau())));
    }

    int conversionCoordonnee(int clic, Boolean estCoordonneeX) {
        if (estCoordonneeX) {
            return (clic / plateauGraphique.largeurCasePlateau() - MILIEU_PLATEAU);
        } else {
            return (clic / plateauGraphique.quartHauteurPlateau() - 1);
        }
    }
}
