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
        if (coupX >= plateauGraphique.debutPlateauX() && coupX <= plateauGraphique.finPlateauX()
                && coupY >= plateauGraphique.debutPlateauY() && coupY <= plateauGraphique.finPlateauY()) {
            coupY = e.getY() / plateauGraphique.largeurCasePlateau();
            collecteurEvenements.clicPlateau(coupY);
        }
        else{
            if (coupX >= plateauGraphique.debutCartesX() && coupX <= plateauGraphique.finCartesX()
                    && coupY >= plateauGraphique.debutCartesY() && coupY <= plateauGraphique.finCartesY()) {
                coupY = e.getY() / plateauGraphique.largeurCaseCarte();
                collecteurEvenements.clicCarte(coupY);
            }
        }
    }
}
