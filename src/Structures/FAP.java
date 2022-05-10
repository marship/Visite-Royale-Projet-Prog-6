package Structures;

public abstract class FAP<Bob> {

    Sequence<Bob> sequence;

    public abstract void insere(Bob element);

    public Bob extrait() {
        return sequence.extraitTete();
    }

    public boolean estVide() {
        return sequence.estVide();
    }
}
