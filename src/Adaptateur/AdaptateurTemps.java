package Adaptateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Vue.CollecteurEvenements;

public class AdaptateurTemps implements ActionListener {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    CollecteurEvenements collecteurEvenements;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurTemps(CollecteurEvenements cEvenements) {
        collecteurEvenements = cEvenements;
    }

    // ========================
    // ===== ACTION TEMPS =====
    // ========================
    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.tictac();
    }
}