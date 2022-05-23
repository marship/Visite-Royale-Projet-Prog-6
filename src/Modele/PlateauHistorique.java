package Modele;

import Pattern.Commande;
import Structures.Iterateur;
import Structures.Sequence;
import Global.Configuration;

public class PlateauHistorique extends Commande {

    Sequence<Plateau> sequenceEtatPlateau;
    Plateau plateau;

    public PlateauHistorique() {
        sequenceEtatPlateau = Configuration.instance().nouvelleSequence();
    }

    void fixerPlateau(Plateau p) {
        plateau = p;
    }

    void ajouterEtatPlateau(Plateau p) {
        sequenceEtatPlateau.insereQueue(p.clone());
    }

    Sequence<Plateau> sequenceEtatPlateau() {
        return sequenceEtatPlateau;
    }

    @Override
    public void execute() {
        // appliquer(0, 1);
    }

    @Override
    public void desexecute() {
        // desappliquer(1, 0);
    }

    // Utilisation pour Historique de coups (executer)
    void appliquer(int X, int Y) {
        Iterateur<Plateau> iterateur = sequenceEtatPlateau.iterateur();
        while (iterateur.aProchain()) {
            Plateau p = (Plateau) iterateur.prochain();
            // plateau.jouerCoupPlateau(p.positionX, p.positionY);
        }
    }

    // Utilisation pour Historique de coups (desexecute)
    void desappliquer(int X, int Y) {
        Iterateur<Plateau> iterateur = sequenceEtatPlateau.iterateur();
        while (iterateur.aProchain()) {
            Plateau p = (Plateau) iterateur.prochain();
            // plateau.dejouerCoupPlateau(p.positionX, p.positionY);
        }
    } 
}