package Vue;

import java.io.InputStream;
import java.io.File;


public abstract class ImagePlateau {
    
    static ImagePlateau getImage(InputStream inputStream) {
        return new ImageSwing(inputStream);
    }

    static ImagePlateau getImageGrise(InputStream inputStream) {
        return new ImageGrise(inputStream);
    }

    abstract <E> E image();
}
