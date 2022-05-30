package Structures;

public class Couple<Valeur, Priorite extends Comparable<Priorite>> implements Comparable<Couple<Valeur, Priorite>> {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    Priorite priorite;
    Valeur valeur;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Couple(Valeur valeurCouple, Priorite prioriteCouple) {
        valeur = valeurCouple;
        priorite = prioriteCouple;
    }

    // ==================================
    // ===== RECUPERATION ATTRIBUTS =====
    // ==================================
    public Valeur element(){
        return valeur;
    }

    public Priorite priorite(){
        return priorite;
    }

    // =======================
    // ===== COMPARAISON =====
    // =======================
    public int compareTo(Couple<Valeur, Priorite> couple) {
        return priorite.compareTo(couple.priorite);
    }

    public boolean equals(Couple<Double, Integer> couple) {
        return (valeur == couple.valeur) && (priorite == couple.priorite);
    }

    // =====================
    // ===== AFFICHAGE =====
    // =====================
    public String toString() {
        return "(" + valeur + ", " + priorite + ")";
    }
}