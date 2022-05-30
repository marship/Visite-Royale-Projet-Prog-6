package Telechargement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Global.Configuration;

public class TelechargementFichier {

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public TelechargementFichier() throws IOException {

        String cheminVersBureau = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "VisiteRoyal_RegleDuJeu.pdf";
        File telechargementRegles = new File(cheminVersBureau);
        OutputStream fluxDeSortie = null;

        try {
            InputStream fluxEntree = Configuration.charge("Documents" + File.separator + "VisiteRoyal_RegleDuJeu.pdf");
            fluxDeSortie = new FileOutputStream(telechargementRegles);
            
            byte[] tampon = new byte[1024];

            int longeur;
            while ((longeur = fluxEntree.read(tampon)) > 0) {
                fluxDeSortie.write(tampon, 0, longeur);
            }
            
            fluxEntree.close();
            fluxDeSortie.close();
        }
        catch(IOException e){
            Configuration.instance().logger().warning("Echec de creation du fichier de regle !!!");
            e.printStackTrace();
        }
    }
}