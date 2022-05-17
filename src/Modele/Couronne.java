package Modele;

public class Couronne {
    
    int positionCouronne;
    boolean etatCouronne;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    Couronne() {
        initialiserCouronne();
    }

    public void initialiserCouronne() {
        positionCouronne = 0;
        etatCouronne = true;
    }

    // ====================
    // ===== POSITION =====
    // ====================
    public void deplacerCouronne(int deplacement) {
        positionCouronne = positionCouronne + deplacement;
    }

    public int positionCouronne() {
        return positionCouronne;
    }

    public void positionnerCouronne(int nouvellePositionCouronne) {
        positionCouronne = nouvellePositionCouronne;
    }

    // ================
    // ===== ETAT =====
    // ================
    public void changerEtatCouronne() {
        etatCouronne = !etatCouronne;
    }

    public boolean etatCouronne() {
        return etatCouronne;
    }

    public void definirEtatCouronne(Boolean nouvelleEtatCouronne) {
        etatCouronne = nouvelleEtatCouronne;
    }
}
