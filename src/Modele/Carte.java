package Modele;

import Global.Deplacement;
import Global.Element;

public class Carte {

    Element personnage;
    Deplacement deplacement;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    Carte(Element perso, Deplacement deplace) {
        personnage = perso;
        deplacement = deplace;
    }

    // ========================
    // ===== INFORMATIONS =====
    // ========================
    public Element personnage() {
        return personnage;
    }

    public Deplacement deplacement() {
        return deplacement;
    }

    public boolean estIdentique(Carte comparaison) {
        return (personnage == comparaison.personnage()) && (deplacement == comparaison.deplacement());
    }

    // =====================
    // ===== AFFICHAGE =====
    // =====================
    public String toString() {
        return "" + personnage.name() + " " + deplacement.name();
    }
}
