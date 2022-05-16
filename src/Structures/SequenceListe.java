package Structures;

public class SequenceListe<Titi> implements Sequence<Titi> {

    Maillon<Titi> tete, queue;

    // Insère élément en début de séquence (en première position)
    public void insereTete(Titi element) {

        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = tete;

        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            tete = nouveau;
        }
    }

    // Insère element en fin de séquence (en dernière position)
    public void insereQueue(Titi element) {

        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = null;

        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            queue.suivant = nouveau;
            queue = nouveau;
        }
    }

    // Extrait + Renvoie la valeur de l'élement situé en début de séquence (en première position)
    public Titi extraitTete() {

        if (tete == null) {
            throw new RuntimeException("Sequence vide !");
        }
        Titi resultat = tete.element;
        tete = tete.suivant;
        return resultat;
    }

    // Renvoie vrai ssi la séquence est vide
    public boolean estVide() {
        return tete == null;
    }

    public Iterateur<Titi> iterateur() {
        return new IterateurListe<>(this);
    }

    public String toString() {

        String resultat = "Sequence liste : [ ";
        Maillon<Titi> courant = tete;

        while (courant != null) {
            resultat = resultat + courant.element + " ";
            courant = courant.suivant;
        }
        resultat = resultat + "]";
        
        return resultat;
    }
    
    public int taille(){
        int resultat = 0;
        Maillon<Titi> courant = tete;
        while (courant != null) {
            resultat = resultat + 1;
            courant = courant.suivant;
        }
        return resultat;
    }
}
