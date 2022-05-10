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
        int coupX = e.getX() / plateauGraphique.hauteurCase();
        int coupY = e.getY() / plateauGraphique.largeurCase();
        collecteurEvenements.clicSouris(coupX, coupY);
    }

}
