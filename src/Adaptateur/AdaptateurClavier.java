package Adaptateur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Global.Configuration;
import Vue.CollecteurEvenements;

public class AdaptateurClavier extends KeyAdapter {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    CollecteurEvenements collecteurEvenements;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurClavier(CollecteurEvenements cEvenements) {
        collecteurEvenements = cEvenements;
    }

    // ==========================
    // ===== PRESSER TOUCHE =====
    // ==========================
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                collecteurEvenements.commande("Quitter");
                break;
            case KeyEvent.VK_SPACE:
                collecteurEvenements.commande("Pause");
                break;
            case KeyEvent.VK_LEFT:
                collecteurEvenements.commande("Annuler");
                break;
            case KeyEvent.VK_RIGHT:
                collecteurEvenements.commande("Refaire");
                break;
            case KeyEvent.VK_V:
                collecteurEvenements.commande("Visible");
                break;
            case KeyEvent.VK_A:
                collecteurEvenements.commande("Aide");
                break;
            default:
                Configuration.instance().logger().info("Touche non assignee !");
                break;
        }
    }
}