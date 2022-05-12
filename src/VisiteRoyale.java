// import Controleur.ControleurMediateur;
import Global.Element;
import Modele.Jeu;
// import Vue.CollecteurEvenements;
// import Vue.InterfaceGraphique;

public class VisiteRoyale {
    public static void main(String[] args) throws Exception {

        try {
            Jeu jeu = new Jeu();

            System.out.println("========== Test ==========");
            System.out.println("");
            jeu.deplacerElement(Element.SORCIER, 1);

            jeu.plateau().afficherPlateau();
            jeu.plateau().paquet.afficherMain(0);
            jeu.jouerCarte(Element.SORCIER, -8, 1);

            jeu.plateau().afficherPlateau();
            jeu.plateau().paquet.afficherMain(0);
            jeu.plateau().paquet.remplirMain(0);
            jeu.plateau().paquet.afficherMain(0);

            // CollecteurEvenements controleurMediateur = new ControleurMediateur(plateau);
            // InterfaceGraphique.demarrer(plateau, controleurMediateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
