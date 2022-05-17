package Modele;

import Global.Element;

public class Personnage {

    Element nomPersonnage;
    int positionPersonnage;

    /////////////////////////////////////////////////////////////////////////
    
    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    Personnage(Element nom, int position){
        nomPersonnage = nom;
        positionPersonnage = position;
    }

    // =====================
    // ===== POSITIONS =====
    // =====================
    public void deplacerPersonnage(int d){
        positionPersonnage = positionPersonnage + d;
    }

    public int positionPersonnage(){
        return positionPersonnage;
    }

    public void positionnerPersonnage(int nouvellePositionPersonnage) {
        positionPersonnage = nouvellePositionPersonnage;
    }

    // ========================
    // ===== INFORMATIONS =====
    // ========================
    public Element typePersonnage(){
        return nomPersonnage;
    }

    // =====================
    // ===== AFFICHAGE =====
    // =====================
    public String toString(){
        return "" + nomPersonnage.name() + " : " + positionPersonnage; 
    }
}
