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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                collecteurEvenements.commande("up");
                break;
            case KeyEvent.VK_DOWN:
                collecteurEvenements.commande("down");
                break;
            case KeyEvent.VK_RIGHT:
                collecteurEvenements.commande("right");
                break;
            case KeyEvent.VK_LEFT:
                collecteurEvenements.commande("left");
                break;
            case KeyEvent.VK_Q:
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
            default:
                break;
        }
    }

}
