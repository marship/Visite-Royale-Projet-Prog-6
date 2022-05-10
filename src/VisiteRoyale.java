// import Controleur.ControleurMediateur;
import Modele.Plateau;
// import Vue.CollecteurEvenements;
// import Vue.InterfaceGraphique;

public class VisiteRoyale {
    public static void main(String[] args) throws Exception {

        try {
            Plateau plateau = new Plateau();

            plateau.choixPremierJoueur(0);
            plateau.afficherPlateau();

            // CollecteurEvenements controleurMediateur = new ControleurMediateur(plateau);
            // InterfaceGraphique.demarrer(plateau, controleurMediateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
