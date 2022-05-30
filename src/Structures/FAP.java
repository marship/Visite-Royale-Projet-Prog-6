package Structures;

public abstract class FAP<Bob> {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    Sequence<Bob> sequence;

    // ===================
    // ===== INSERER =====
    // ===================
    public abstract void insere(Bob element);

    // ====================
    // ===== EXTRAIRE =====
    // ====================
    public Bob extrait() {
        return sequence.extraitTete();
    }

    // =================
    // ===== VIDER =====
    // =================
    public boolean estVide() {
        return sequence.estVide();
    }
}