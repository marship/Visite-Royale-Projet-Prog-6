package Global;

public enum InfoJeu {

    MENU_PRICIPAL (1),
    OPTIONS (2),
    DEBUT_TOUR (0),
    CHOIX_GARDE (3),
    CHOIX_FOU (4),
    CHOIX_SORCIER (5),
    CHOIX_DEPLACEMENT (6),
    APRES_UNE_CARTE(7),
    JOUER_UNE_CARTE (8);

    int valeurElement;

    InfoJeu(int valeurElement) {
        this.valeurElement = valeurElement;
    }

    public int getValeurElement() {
        return valeurElement;
    }
}
