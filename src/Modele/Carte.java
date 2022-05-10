package Modele;

public class Carte { // A REVOIR PAS SUR DU TRUC
    int personnage;
    int deplacement;
    boolean estGarde;

    Carte(int perso, int deplace){
        personnage = perso;
        deplacement = deplace;
        estGarde = (perso == 0);
    }

    public int personnage(){
        return personnage;
    }

    public int deplacement(){
        return deplacement;
    }

    public boolean estGarde(){
        return estGarde;
    }

    public boolean estIdentique(Carte comparaison){
        return (personnage == comparaison.personnage()) && (deplacement == comparaison.deplacement());
    }

}
