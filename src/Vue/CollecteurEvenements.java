package Vue;

import Global.InfoJeu;

public interface CollecteurEvenements {
    
    void traqueSouris(int coupX, int coupY);
    void tictac();
	void changerJoueurCourant(int numeroJoueur, int typeJoueur);
	boolean commande(String com);
    void ajouteInterfaceUtilisateur(InterfaceUtilisateur interfaceUtilisateur);
    void choix(int choix);
    InfoJeu getInfoJeu();
    void clicPlateau(int coupY);
    void clicCarte(int coupY);
}
