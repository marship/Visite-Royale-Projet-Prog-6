package Vue;

import Global.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageGrise extends ImagePlateau{
    private BufferedImage bufImg;

    ImageGrise(InputStream inputStream) {
        try {
            // Chargement d'une image utilisable dans Swing
            bufImg = ImageIO.read(inputStream);
        } catch (Exception e) {
            Configuration.instance().logger().severe("Impossible de charger l'image !\n");
            System.exit(1);
        }
        //convert to grayscale
        for(int i = 0; i < bufImg.getHeight(); i++){
            for(int j = 0; j < bufImg.getWidth(); j++){
                int p = bufImg.getRGB(j,i);
        
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
        
                //calculate average
                int avg = (r+g+b)/3;
        
                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
        
                bufImg.setRGB(j, i, p);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    <E> E image() {
        return (E) bufImg;
    }
}