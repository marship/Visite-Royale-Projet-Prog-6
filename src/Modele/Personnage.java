package Modele;

public class Personnage {
    String nomPersonnage;
    int positionPersonnage;
    boolean capaciteSpecial;
    
    Personnage(String nom, int position, boolean capacite){
        nomPersonnage = nom;
        positionPersonnage = position;
        capaciteSpecial = capacite;
    }

    public void deplacerPersonnage(int d){
        positionPersonnage = positionPersonnage + d;
    }

    public String nomPersonnage(){
        return nomPersonnage;
    }

    public int positionPersonnage(){
        return positionPersonnage;
    }

    public boolean capaciteSpecial(){
        return capaciteSpecial;
    }
}
