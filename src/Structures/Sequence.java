package Structures;

public interface Sequence<Toto>{

    // ===================
    // ===== INSERER =====
    // ===================
    void insereTete(Toto element);
    void insereQueue(Toto element);

    // ====================
    // ===== EXTRAIRE =====
    // ====================
    Toto extraitTete();

    // =================
    // ===== VIDER =====
    // =================
    boolean estVide();

    // ==================
    // ===== TAILLE =====
    // ==================
    int taille();

    // =====================
    // ===== ITERATEUR =====
    // =====================
    Iterateur<Toto> iterateur();
}