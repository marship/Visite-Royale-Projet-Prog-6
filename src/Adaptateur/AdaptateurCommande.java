package Adaptateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Audio.Son;
import Vue.CollecteurEvenements;

public class AdaptateurCommande implements ActionListener {

    CollecteurEvenements collecteurEvenements;
    String nomSonAudio = "Son_Bouton";
    String commande;
    Son son;

    public AdaptateurCommande(CollecteurEvenements cEvenements, String com) {
        collecteurEvenements = cEvenements;
        commande = com;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        collecteurEvenements.commande(commande);
        son = new Son();
        son.moyenVolume();
    }
}
