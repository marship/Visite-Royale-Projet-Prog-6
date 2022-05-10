package Modele;

public class Personnage {
    int nomPersonnage;
    int positionPersonnage;
    boolean capaciteSpecial;
    
    Personnage(int nom, int position, boolean capacite){
        nomPersonnage = nom;
        positionPersonnage = position;
        capaciteSpecial = capacite;
    }

    public void deplacerPersonnage(int d){
        positionPersonnage = positionPersonnage + d;
    }

    public int nomPersonnage(){
        return nomPersonnage;
    }

    public int positionPersonnage(){
        return positionPersonnage;
    }

    public boolean capaciteSpecial(){
        return capaciteSpecial;
    }
}
