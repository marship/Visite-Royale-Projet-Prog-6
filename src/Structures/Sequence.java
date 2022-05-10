package Structures;

public interface Sequence<Toto>{

    // Insere élément en debut de séquence (en première position)
    void insereTete(Toto element);

    // Insère élément en fin de séquence (en dernière position)
    void insereQueue(Toto element);

    // Extrait + Renvoie la valeur de l'élement situé en debut de séquence (en première position)
    Toto extraitTete();

    // Renvoie vrai ssi la séquence est vide
    boolean estVide();

    Iterateur<Toto> iterateur();
}
