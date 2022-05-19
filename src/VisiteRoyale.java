
import Controleur.ControleurMediateur;

import Global.Configuration;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
import Vue.InterfaceTextuelle;

public class VisiteRoyale {

    // ================
    // ===== MAIN =====
    // ================
    public static void main(String[] args) throws Exception {
        try {
            Jeu jeu = new Jeu();
            CollecteurEvenements controleurMediateur = new ControleurMediateur(jeu);

            switch (Configuration.instance().lis("Interface")) {
                case "Textuelle":
                    InterfaceTextuelle.demarrer(jeu, controleurMediateur);
                    break;
                case "Graphique":
                    jeu.changerEtatPartie();
                    InterfaceGraphique.demarrer(jeu, controleurMediateur);
                    
                    break;
                default:
                    Configuration.instance().logger().severe("Interface inconnue !!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
