package Structures;

public class IterateurListe<Riri> extends Iterateur<Riri> {

    Maillon<Riri> courant, precedent, arrierePrecedent;
    SequenceListe<Riri> sequenceEnListe;

    IterateurListe(SequenceListe<Riri> sequenceListe) {
        courant = sequenceListe.tete;
        sequenceEnListe = sequenceListe;
    }

    @Override
    public boolean aProchain() {
        return courant != null;
    }

    @Override
    public Riri prochain() {
        super.prochain();
        Riri resultat = courant.element;
        arrierePrecedent = precedent;
        precedent = courant;
        courant = courant.suivant;
        return resultat;
    }

    @Override
    public void supprime() {
        super.prochain();
        if (arrierePrecedent == null) {
            sequenceEnListe.tete = courant;
        } else {
            arrierePrecedent.suivant = courant;
        }
        if (sequenceEnListe.queue == precedent) {
            sequenceEnListe.queue = arrierePrecedent;
        }
        precedent = arrierePrecedent;
    }
}
