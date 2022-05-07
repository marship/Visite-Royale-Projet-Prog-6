package Structures;

public abstract class Iterateur<Tata> {

    boolean peutSupprimer;

    public abstract boolean aProchain();

    public Object prochain() {
        peutSupprimer = true;
        return null;
    }

    public void supprime() {
        if (!peutSupprimer) {
            throw new IllegalStateException("Deux suppressions d'affilee");
        }
        peutSupprimer = false;
    }
}
