package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {

    PlateauGraphique plateauGraphique;
    CollecteurEvenements collecteurEvenements;

    AdaptateurSouris(PlateauGraphique pGraphique, CollecteurEvenements cEvenements) {
        plateauGraphique = pGraphique;
        collecteurEvenements = cEvenements;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int coupX = e.getX();
        int coupY = e.getY();
        if (coupX >= plateauGraphique.debutPlateauX() && coupX <= plateauGraphique.largeurPlateau()
                && coupY >= plateauGraphique.debutPlateauY() && coupY <= plateauGraphique.hauteurPlateau()) {
            coupX = e.getX() / plateauGraphique.largeurCasePlateau() - 8;
            coupY = e.getY() / plateauGraphique.quartHauteurPlateau() - 1;
            collecteurEvenements.clicPlateau(coupX, coupY);
        } else {
            if (coupX >= plateauGraphique.debutZoneCartesX() && coupX <= plateauGraphique.finZoneCartesX()
                    && coupY >= plateauGraphique.debutZoneCartesY() && coupY <= plateauGraphique.finZoneCartesY()) {
                        
                coupX = e.getX() / plateauGraphique.largeurCarte() - 4;
                collecteurEvenements.clicCarte(coupX);
            }
        }
    }
}
