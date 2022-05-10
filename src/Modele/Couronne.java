package Modele;

public class Couronne {
    int positionCouronne;
    boolean etatCouronne;

    Couronne() {
        initialiserCouronne();
    }

    public void initialiserCouronne() {
        positionCouronne = 0;
        etatCouronne = true;
    }

    public void deplacerCouronne(int deplacement) {
        positionCouronne = positionCouronne + deplacement;
    }

    public void changerEtatCouronne() {
        etatCouronne = !etatCouronne;
    }

    public int positionCouronne() {
        return positionCouronne;
    }

    public void positionnerCouronne(int nouvellePositionCouronne) {
        positionCouronne = nouvellePositionCouronne;
    }

    public boolean etatCouronne() {
        return etatCouronne;
    }
}
