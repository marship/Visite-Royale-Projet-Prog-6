package Structures;

public interface Sequence<Toto>{

    // Insere element en debut de sequence (en premiere position)
    void insereTete(Toto element);

    // Insère element en fin de sequence (en derniere position)
    void insereQueue(Toto element);

    // Extrait + Renvoie la valeur de l'element situe en debut de sequence (en
    // première position)
    Toto extraitTete();

    // Renvoie vrai ssi la sequence est vide
    boolean estVide();

    Iterateur<Toto> iterateur();
}
