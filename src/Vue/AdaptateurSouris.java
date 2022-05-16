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
        System.out.println("Clic !");
        int coupX = e.getX() / plateauGraphique.hauteurCase();
        int coupY = e.getY() / plateauGraphique.largeurCase();
        collecteurEvenements.clicSouris(coupX, coupY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int coupX = e.getX();
        int coupY = e.getY();
        collecteurEvenements.traqueSouris(coupX, coupY);
    }

}
