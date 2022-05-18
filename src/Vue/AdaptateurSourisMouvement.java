package Vue;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class AdaptateurSourisMouvement implements MouseMotionListener {

    PlateauGraphique plateauGraphique;
    CollecteurEvenements collecteurEvenements;

    public AdaptateurSourisMouvement(PlateauGraphique pGraphique, CollecteurEvenements cEvenements) {
        plateauGraphique = pGraphique;
        collecteurEvenements = cEvenements;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Pas Nécéssaire
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int coupX = e.getX();
        int coupY = e.getY();
        if (coupX >= plateauGraphique.debutPlateauX() && coupX <= plateauGraphique.largeurPlateau()
                && coupY >= plateauGraphique.debutPlateauY() && coupY <= plateauGraphique.hauteurPlateau()) {
            coupX = e.getX() / plateauGraphique.largeurCasePlateau() - 8;
            coupY = e.getY() / plateauGraphique.quartHauteurPlateau() - 1;
            collecteurEvenements.traquePlateau(coupX, coupY);
        } else {
            if (coupX >= plateauGraphique.debutZoneCartesX() && coupX <= plateauGraphique.finZoneCartesX()
                    && coupY >= plateauGraphique.debutZoneCartesY() && coupY <= plateauGraphique.finZoneCartesY()) {
                coupX = e.getX() / plateauGraphique.largeurCarte() - 4;
                collecteurEvenements.traqueCarte(coupX, coupY);

                collecteurEvenements.setDebutZoneCartesX(plateauGraphique.debutZoneCartesX());
                collecteurEvenements.setDebutZoneCartesY(plateauGraphique.debutZoneCartesY());
                collecteurEvenements.setValeurHauteurPrevisualisation(plateauGraphique.finZoneCartesY() - plateauGraphique.debutZoneCartesY());
                collecteurEvenements.setValeurLargeurPrevisualisation(plateauGraphique.finZoneCartesX() - plateauGraphique.debutZoneCartesX());
            }
        }
    }
}
