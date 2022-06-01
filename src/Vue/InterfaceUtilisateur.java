package Vue;

import javax.swing.JFrame;

public interface InterfaceUtilisateur {

    void previsualisation(int coupX, int coupY, int largeurPreselection, int hauteurPreselection);
	void afficherPanneau(String nomPanneau);
	String getNomJoueurInit(int coteJoueur);
	String getInfoJoueurInit(int coteJoueur);
	String getNomJoueurChang(int coteJoueur);
	String getInfoJoueurChang(int coteJoueur);
	void setBoutonHistoriqueArriere(boolean visible);
	void setBoutonHistoriqueAvant(boolean visible);
	void miseAJourFinDeTour();
	JFrame fenetre();
    void augmenterVolume();
    void diminuerVolume();
    void muterVolume();
}
