package Modele;

import Global.Element;

public class Personnage {
    Element typePersonnage;
    int positionPersonnage;
    boolean capaciteSpecial;

    Personnage(Element tPersonnage, int pPersonnage, boolean cSpecial) {
        typePersonnage = tPersonnage;
        positionPersonnage = pPersonnage;
        capaciteSpecial = cSpecial;
    }

    public void deplacerPersonnage(int deplacement) {
        positionPersonnage = positionPersonnage + deplacement;
    }

    public Element typePersonnage() {
        return typePersonnage;
    }

    public int positionPersonnage() {
        return positionPersonnage;
    }

    public void positionnerPersonnage(int nouvellePositionCouronne) {
        positionPersonnage = nouvellePositionCouronne;
    }

    public boolean capaciteSpecial() {
        return capaciteSpecial;
    }
}
