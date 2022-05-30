package Structures;

public abstract class Iterateur<Tata> {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    boolean peutSupprimer;

    // ===================
    // ===== SUIVANT =====
    // ===================
    public abstract boolean aProchain();

    public Object prochain() {
        peutSupprimer = true;
        return null;
    }

    // =====================
    // ===== SUPPRIMER =====
    // =====================
    public void supprime() {
        if (!peutSupprimer) {
            throw new IllegalStateException("Deux suppressions d'affilee");
        }
        peutSupprimer = false;
    }
}