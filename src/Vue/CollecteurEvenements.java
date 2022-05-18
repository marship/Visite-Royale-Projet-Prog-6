package Vue;

import Global.InfoJeu;

public interface CollecteurEvenements {
    
    void traquePlateau(int coupX, int coupY);
    void traqueCarte(int positionSourisX, int positionSourisY);
    void tictac();
	void changerJoueurCourant(int numeroJoueur, int typeJoueur);
	boolean commande(String com);
    void ajouteInterfaceUtilisateur(InterfaceUtilisateur interfaceUtilisateur);
    void choix(int choix);
    InfoJeu getInfoJeu();
    void clicPlateau(int coupX, int coupY);
    void clicCarte(int coupX);
    void setDebutZoneCartesX(int debutZoneCartesX);
    void setDebutZoneCartesY(int debutZoneCartesX);
    void setValeurHauteurPrevisualisation(int i);
    void setValeurLargeurPrevisualisation(int i);
    void passerSurCarte(int coupX);
}
