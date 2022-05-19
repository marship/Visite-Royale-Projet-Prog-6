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
        for(int y = 0; y < bufImg.getHeight(); y++){
            for(int x = 0; x < bufImg.getWidth(); x++){
                int p = bufImg.getRGB(x,y);
        
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
        
                //calculate average
                int avg = (r+g+b)/3;
        
                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
        
                bufImg.setRGB(x, y, p);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    <E> E image() {
        return (E) bufImg;
    }
}