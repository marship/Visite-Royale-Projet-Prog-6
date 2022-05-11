// import Controleur.ControleurMediateur;
import Global.Element;
import Modele.Plateau;
// import Vue.CollecteurEvenements;
// import Vue.InterfaceGraphique;

public class VisiteRoyale {
    public static void main(String[] args) throws Exception {

        try {
            Plateau plateau = new Plateau();

            System.out.println("========== Cas Fou ==========");
            System.out.println("");
            plateau.deplacerElement(Element.FOU, 9);
            plateau.afficherPlateau();
            

            // CollecteurEvenements controleurMediateur = new ControleurMediateur(plateau);
            // InterfaceGraphique.demarrer(plateau, controleurMediateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
