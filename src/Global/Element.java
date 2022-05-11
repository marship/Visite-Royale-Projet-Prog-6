package Global;

public enum Element {

    VIDE (-1),
    COURONNE (0),
    GARDE_GAUCHE (1),
    GARDE_DROIT (2),
    ROI (3),
    FOU (4),
    SORCIER (5),
    GARDES (6);

    int valeurElement;

    Element(int valeurElement) {
        this.valeurElement = valeurElement;
    }

    public int getValeurElement() {
        return valeurElement;
    }
}
