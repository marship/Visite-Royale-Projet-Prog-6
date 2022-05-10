package Modele;

public class Carte { // A REVOIR PAS SUR DU TRUC
    String personnage;
    String deplacement;
    boolean estGarde;

    Carte(String perso, String deplace){
        personnage = perso;
        deplacement = deplace;
        estGarde = perso.equals("garde");
    }

    public String personnage(){
        return personnage;
    }

    public String deplacement(){
        return deplacement;
    }

    public boolean estGarde(){
        return estGarde;
    }

}
