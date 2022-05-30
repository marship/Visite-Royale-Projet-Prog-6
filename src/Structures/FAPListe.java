package Structures;

public class FAPListe<Bill extends Comparable<Bill>> extends FAP<Bill> {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    SequenceListe<Bill> sequenceListe;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public FAPListe() {
        sequenceListe = new SequenceListe<>();
        super.sequence = sequenceListe;
    }

    // ===================
    // ===== INSERER =====
    // ===================
    public void insere(Bill element) {

        Maillon<Bill> courant;
        Maillon<Bill> precedent;
        precedent = null;
        courant = sequenceListe.tete;
        while ((courant != null) && (element.compareTo(courant.element) > 0)) {
            precedent = courant;
            courant = courant.suivant;
        }
        while ((courant != null) && (element.compareTo(courant.element) == 0)) {
            precedent = courant;
            courant = courant.suivant;
        }
        Maillon<Bill> maillon = new Maillon<>();
        maillon.element = element;
        maillon.suivant = courant;
        if (precedent == null) {
            sequenceListe.tete = maillon;
        } else {
            precedent.suivant = maillon;
        }
        if (courant == null) {
            sequenceListe.queue = maillon;
        }
    }
}