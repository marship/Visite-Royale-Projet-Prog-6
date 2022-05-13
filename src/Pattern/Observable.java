package Pattern;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Observable {

    static Sequence<Observateur> observateurs;

    public Observable() {
        observateurs = Configuration.instance().nouvelleSequence();
    }

    public void ajouteObservateur(Observateur observateur) {
        observateurs.insereQueue(observateur);
    }

    public static void metAJour() {
        
        Iterateur<Observateur> iterateur;
        iterateur = observateurs.iterateur();
        while (iterateur.aProchain()) {
            ((Observateur) iterateur.prochain()).miseAJour();
        }
        
    }

}
