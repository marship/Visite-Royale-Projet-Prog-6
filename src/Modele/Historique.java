package Modele;

import Global.Configuration;
import Pattern.Commande;
import Structures.Sequence;

public class Historique <E extends Commande>{
    
    public Sequence<E> passe;
    public Sequence<E> futur;

    Historique() {
        passe = Configuration.instance().nouvelleSequence();
        futur = Configuration.instance().nouvelleSequence();
    }

    public void viderHistorique() {
        passe = Configuration.instance().nouvelleSequence();
        futur = Configuration.instance().nouvelleSequence();
    }

    void nouveau(E commande) {
        passe.insereTete(commande);
        // commande.execute();
        while (!futur.estVide()) {
            futur.extraitTete();
        }
    }

    public boolean peutAnnuler() {
        return !passe.estVide();
    }

    E annuler() {
        if (peutAnnuler()) {
            E commande = passe.extraitTete();
            commande.desexecute();
            futur.insereTete(commande);
            return commande;
        } else {
            return null;
        }
    }

    public boolean peutRefaire() {
        return !futur.estVide();
    }

    E refaire() {
        if (peutRefaire()) {
            E commande = futur.extraitTete();
            commande.execute();
            passe.insereTete(commande);
            return commande;
        } else {
            return null;
        }
    }
}
