package Global;

public enum InfoPlateau {

    TAILLE_DU_PLATEAU (17),
    CENTRE_DU_PLATEAU (0),
    EXTREMITE_GAUCHE_DU_PLATEAU (-8),
    EXTREMITE_DROITE_DU_PLATEAU  (8),
    ENTREE_CHATEAU_GAUCHE (-6),
    ENTREE_CHATEAU_DROIT (6);

    int valeurElement;

    InfoPlateau(int valeurElement) {
        this.valeurElement = valeurElement;
    }

    public int getValeurElement() {
        return valeurElement;
    }
}
