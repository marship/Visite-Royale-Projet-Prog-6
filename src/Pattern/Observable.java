package Pattern;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Observable {

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    static Sequence<Observateur> observateurs;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public Observable() {
        observateurs = Configuration.instance().nouvelleSequence();
    }

    // ===============================
    // ===== AJOUTER OBSERVATEUR =====
    // ===============================
    public void ajouteObservateur(Observateur observateur) {
        observateurs.insereQueue(observateur);
    }

    // =========================
    // ===== METTRE A JOUR =====
    // =========================
    public static void metAJour() {
        Iterateur<Observateur> iterateur;
        iterateur = observateurs.iterateur();
        while (iterateur.aProchain()) {
            ((Observateur) iterateur.prochain()).miseAJour();
        }
    }
}