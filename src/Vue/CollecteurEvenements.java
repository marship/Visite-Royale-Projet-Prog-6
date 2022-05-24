package Vue;

public interface CollecteurEvenements {
    
    void tictac();
	void changerJoueurCourant(int numeroJoueur, int typeJoueur);
	boolean commande(String com);
    void ajouteInterfaceUtilisateur(InterfaceUtilisateur interfaceUtilisateur);
    void choix(int choix);
    void clicPlateau(int coupX, int coupY);
    void clicCarte(int coupX);
    void passerSurCarte(int coupX);
    // void gestionVolume(boolean gestionMusique);
    void lancerAudioSon(String nomSonAudio);
}
