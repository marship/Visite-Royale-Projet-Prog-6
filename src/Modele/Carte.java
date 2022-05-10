package Modele;

import Global.Deplacement;
import Global.Element;

// A REVOIR PAS SUR DU TRUC
public class Carte {
    Element personnage;
    Deplacement deplacement;
    boolean estGarde;

    Carte(Element perso, Deplacement deplace) {
        personnage = perso;
        deplacement = deplace;
        estGarde = (perso == Element.GARDE_GAUCHE);
    }

    public Element personnage() {
        return personnage;
    }

    public Deplacement deplacement() {
        return deplacement;
    }

    public boolean estGarde() {
        return estGarde;
    }

    public boolean estIdentique(Carte comparaison) {
        return (personnage == comparaison.personnage()) && (deplacement == comparaison.deplacement());
    }

    public String toString(){
        return "" + personnage.name() + deplacement.name();
    }

}
