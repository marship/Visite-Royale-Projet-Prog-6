package Vue;

public interface CollecteurEvenements {
    
    void clicSouris(int coupX, int coupY);
    void tictac();
	void changerJoueurCourant(int numeroJoueur, int typeJoueur);
	boolean commande(String com);
    void getInterfaceGraphique(InterfaceGraphique interfaceGraphique);
}
