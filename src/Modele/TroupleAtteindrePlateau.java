package Modele;

import Global.Element;

public class TroupleAtteindrePlateau {
    
    Plateau plateau;
    Carte[] cartesJouees;
    Element persoJouer;

    public TroupleAtteindrePlateau(Plateau p, Carte[] listeCartes, Element perso){
        plateau = p;
        cartesJouees = listeCartes;
        persoJouer = perso;
    }

    public Plateau gPlateau(){
        return plateau;
    }

    public Carte[] gCartes(){
        return cartesJouees;
    }

    public Element gPerso(){
        return persoJouer;
    }

}
