package Modele;

import Global.Configuration;
import Pattern.Commande;
import Structures.Iterateur;
import Structures.Sequence;

public class Coup extends Commande {

    public Sequence<Plateau> sequencePlateau;
    Plateau plateau;

    Coup() {
        sequencePlateau = Configuration.instance().nouvelleSequence();
    }

    void fixerPlateau(Plateau p) {
        plateau = p;
    }

    void plateauCoup(Plateau p) {
        Plateau plat = p.clone();
        sequencePlateau.insereQueue(plat);
    }

    Sequence<Plateau> slistePlateauCoup() {
        return sequencePlateau;
    }

    @Override
    public void execute() {
        jouerPlateauCoup();
    }

    @Override
    public void desexecute() {
        jouerPlateauCoup();
    }

    private void jouerPlateauCoup() {
        Iterateur<Plateau> iterateur = sequencePlateau.iterateur();
        while (iterateur.aProchain()) {
            Plateau plato = (Plateau) iterateur.prochain();
            plateau.jouerCoupPlateau(plato);
        }
    }
}
