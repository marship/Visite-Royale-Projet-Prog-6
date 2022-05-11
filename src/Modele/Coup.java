package Modele;

import Pattern.Commande;
import Structures.Iterateur;
import Structures.Sequence;
import java.awt.Point;
import Global.Configuration;

public class Coup extends Commande {

    Sequence<Plateau> sequenceEtatPlateau;
    Plateau plateau;

    Coup() {
        sequenceEtatPlateau = Configuration.instance().nouvelleSequence();
    }

    void fixerPlateau(Plateau p) {
        plateau = p;
    }

    void ajouterEtatPlateau(Plateau p) {
        sequenceEtatPlateau.insereQueue(new Plateau(p));
    }

    Sequence<Plateau> sequenceEtatPlateau() {
        return sequenceEtatPlateau;
    }

    void X() {
        // Iterateur<Point> iterateur = bouchee.iterateur();
        // while (iterateur.aProchain()) {
            // Point pointcourant = (Point) iterateur.prochain();
            // pointcourant.x =
            // pointcourant.y =
        // }
    }

    @Override
    public void execute() {
        X();
    }

    @Override
    public void desexecute() {
        X();
    }

    
    
}