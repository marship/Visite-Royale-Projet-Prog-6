package Modele;

import java.util.Arrays;

public class CoupleAtteindrePlateau {

    int[] positions;
    int[] cartes;

    public CoupleAtteindrePlateau(int[] pos, int[] car){
        positions = Arrays.copyOf(pos, 5);
        cartes = Arrays.copyOf(car, 8);
    }

    public int[] positions(){
        return positions;
    }

    public int[] cartes(){
        return cartes;
    }

}
