package Vue;

import javax.swing.JFrame;

public interface InterfaceUtilisateur {

    void previsualisation(int coupX, int coupY, int largeurPreselection, int hauteurPreselection);
	void afficherPanneau(String nomPanneau);
	void miseAJourFinDeTour();
	JFrame fenetre();
}
