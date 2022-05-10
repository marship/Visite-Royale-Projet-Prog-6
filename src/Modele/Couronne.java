package Modele;

public class Couronne {
    int positionCouronne;
    boolean etatCouronne;

    Couronne(){
        initCouronne();
    }

    public void initCouronne(){
        positionCouronne = 0;
        etatCouronne = true;
    }

    public void deplacerCouronne(int d){
        positionCouronne = positionCouronne + d;
    }

    public void changerEtatCouronne(){
        etatCouronne = !etatCouronne;
    }

    public int positionCouronne(){
        return positionCouronne;
    }

    public boolean etatCouronne(){
        return etatCouronne;
    }
}
