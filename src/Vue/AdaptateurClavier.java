package Vue;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {

    CollecteurEvenements collecteurEvenements;

    AdaptateurClavier(CollecteurEvenements cEvenements) {
        collecteurEvenements = cEvenements;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                System.out.println("Sorcier");
                collecteurEvenements.commande("Sorcier");
                break;
            case KeyEvent.VK_F:
                System.out.println("Fou");
                collecteurEvenements.commande("Fou");
                break;
            case KeyEvent.VK_D:
                System.out.println("Roi");
                collecteurEvenements.commande("Roi");
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("Gauche");
                collecteurEvenements.commande("left");
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Droite");
                collecteurEvenements.commande("right");
                break;
            case KeyEvent.VK_Q:
                System.out.println("Quit");
                collecteurEvenements.commande("quit");
                break;
            case KeyEvent.VK_U:
                collecteurEvenements.commande("annule");
                break;
            case KeyEvent.VK_R:
                collecteurEvenements.commande("refaire");
                break;
            case KeyEvent.VK_ESCAPE:
                collecteurEvenements.commande("fullscreen");
                break;
            case KeyEvent.VK_SPACE:
                collecteurEvenements.commande("ia");
                break;
            case KeyEvent.VK_ENTER:
                collecteurEvenements.commande("Fin");
                break;
            case KeyEvent.VK_BACK_SPACE:
                collecteurEvenements.commande("Retour");
                break;
            case KeyEvent.VK_W:
                collecteurEvenements.commande("pause");
                break;
            case KeyEvent.VK_X:
                collecteurEvenements.commande("visible");
                break;
            default:
                break;
        }
    }

}
