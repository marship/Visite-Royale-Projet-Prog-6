package Modele;

import Global.Element;

public class Personnage {
    Element nomPersonnage;
    int positionPersonnage;
    boolean capaciteSpecial;
    
    Personnage(Element nom, int position, boolean capacite){
        nomPersonnage = nom;
        positionPersonnage = position;
        capaciteSpecial = capacite;
    }

    public void deplacerPersonnage(int d){
        positionPersonnage = positionPersonnage + d;
    }

    public Element typePersonnage(){
        return nomPersonnage;
    }

    public int positionPersonnage(){
        return positionPersonnage;
    }

    public boolean capaciteSpecial(){
        return capaciteSpecial;
    }

    public String toString(){
        return "" + nomPersonnage.name() + " : " + positionPersonnage; 
    }

    public void positionnerPersonnage(int nouvellePositionPersonnage) {
        positionPersonnage = nouvellePositionPersonnage;
    }
}
