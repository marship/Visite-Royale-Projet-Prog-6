package Telechargement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Global.Configuration;

public class TelechargementFichier {

    public TelechargementFichier() throws IOException {

        String bureau = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "VisiteRoyal_RegleDuJeu.pdf";
        File telechargementRegles = new File(bureau);
        OutputStream outputStream = null;

        try {
            InputStream inputStream = Configuration.charge("Documents" + File.separator + "VisiteRoyal_RegleDuJeu.pdf");
            outputStream = new FileOutputStream(telechargementRegles);
            
            byte[] tampon = new byte[1024];
            int longeur;
            while ((longeur = inputStream.read(tampon)) > 0) {
                outputStream.write(tampon, 0, longeur);
            }
            inputStream.close();
            outputStream.close();
        }
        catch(IOException e){
            Configuration.instance().logger().warning("Echec de creation du fichier de regle !!!");
            e.printStackTrace();
        }
    }
}
