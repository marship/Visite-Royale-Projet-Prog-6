package Adaptateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Audio.Son;
import Vue.CollecteurEvenements;

public class AdaptateurCommande implements ActionListener {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    CollecteurEvenements collecteurEvenements;
    String commande;
    Son son;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurCommande(CollecteurEvenements cEvenements, String com) {
        collecteurEvenements = cEvenements;
        commande = com;
    }

    // ===========================
    // ===== ACTION COMMANDE =====
    // ===========================
    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.commande(commande);
        jouerSon();
    }

    // ======================
    // ===== SON ACTION =====
    // ======================
    void jouerSon() {
        son = new Son();
    }
}