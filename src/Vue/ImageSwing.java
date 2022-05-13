package Vue;

import Global.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class ImageSwing extends ImagePlateau {
    Image image;

    ImageSwing(InputStream inputStream) {
        try {
            // Chargement d'une image utilisable dans Swing
            image = ImageIO.read(inputStream);
        } catch (Exception e) {
            Configuration.instance().logger().severe("Impossible de charger l'image !\n");
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    <E> E image() {
        return (E) image;
    }
}