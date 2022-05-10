package Structures;

public class Couple<Valeur, Priorite extends Comparable<Priorite>> implements Comparable<Couple<Valeur, Priorite>> {

    Valeur valeur;
    Priorite priorite;

    public Couple(Valeur valeurCouple, Priorite prioriteCouple) {
        valeur = valeurCouple;
        priorite = prioriteCouple;
    }

    public int compareTo(Couple<Valeur, Priorite> couple) {
        return priorite.compareTo(couple.priorite);
    }

    public String toString() {
        return "(" + valeur + ", " + priorite + ")";
    }

    public boolean equals(Couple<Double, Integer> couple) {
        return (valeur == couple.valeur) && (priorite == couple.priorite);
    }

    public Valeur e(){
        return valeur;
    }

    public Priorite p(){
        return priorite;
    }
}
