package Global;

public enum Deplacement {

    VIDE (-1),
    RAPPROCHE (0),
    UN (1),
    DEUX (2),
    TROIS (3),
    QUATRE (4),
    CINQ (5),
    MILIEU (6),
    UN_PLUS_UN (7) ;

    int valeurDeplacement;

    Deplacement(int valeurDeplacement) {
        this.valeurDeplacement = valeurDeplacement;
    }

    public int getValeurDeplacement() {
        return valeurDeplacement;
    }
}