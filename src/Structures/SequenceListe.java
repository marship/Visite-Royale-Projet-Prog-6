package Structures;

public class SequenceListe<Titi> implements Sequence<Titi> {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    Maillon<Titi> tete, queue;

    // ===================
    // ===== INSERER =====
    // ===================
    public void insereTete(Titi element) {

        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = tete;

        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            tete = nouveau;
        }
    }

    public void insereQueue(Titi element) {

        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = null;

        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            queue.suivant = nouveau;
            queue = nouveau;
        }
    }

    // ====================
    // ===== EXTRAIRE =====
    // ====================
    public Titi extraitTete() {

        if (tete == null) {
            throw new RuntimeException("Sequence vide !");
        }
        Titi resultat = tete.element;
        tete = tete.suivant;
        return resultat;
    }

    // =================
    // ===== VIDER =====
    // =================
    public boolean estVide() {
        return tete == null;
    }

    // ==================
    // ===== TAILLE =====
    // ==================
    public int taille() {
        int resultat = 0;
        Maillon<Titi> courant = tete;
        while (courant != null) {
            resultat = resultat + 1;
            courant = courant.suivant;
        }
        return resultat;
    }

    // =====================
    // ===== ITERATEUR =====
    // =====================
    public Iterateur<Titi> iterateur() {
        return new IterateurListe<>(this);
    }

    // =====================
    // ===== AFFICHAGE =====
    // =====================
    public String toString() {

        String resultat = "Sequence liste : [ ";
        Maillon<Titi> courant = tete;

        while (courant != null) {
            resultat = resultat + courant.element + " ";
            courant = courant.suivant;
        }
        resultat = resultat + "]";
        
        return resultat;
    }
}