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
        // System.out.println(coupX + ", " + coupY);
        collecteurEvenements.traqueSouris(coupX, coupY);
    }
    
}
