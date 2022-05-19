package Global;

public enum InfoJeu {

    MENU_PRINCIPAL (1),
    OPTIONS_JEU (2),
    DEBUT_TOUR (0),
    CHOIX_GARDE (3),
    CHOIX_FOU (4),
    CHOIX_SORCIER (5),
    CHOIX_ROI (6),
    APRES_UNE_CARTE(7),
    JOUER_UNE_CARTE (8),
    
    CHARGER_PARTIE (9),
    OPTIONS_MENU (10),
    REGLES_JEU (12),

    SELECTION_JOUEURS (11),
    AIDE_JEU (13);

    int valeurElement;

    InfoJeu(int valeurElement) {
        this.valeurElement = valeurElement;
    }

    public int getValeurElement() {
        return valeurElement;
    }
}