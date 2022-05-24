package Vue;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JSlider;

public class Son {
    
    Clip clip;
    float volumePrecedent = -9;
    float volumeCourant = -9;
    FloatControl floatControl;
    boolean estMuter = false;
    AudioInputStream audioInputStream;

    String nomFichierAudio = "gangstas-paradise-medieval";
    // String nomFichierAudio = "the-weeknd-medieval";

    static final String CHEMIN_FICHIER_AUDIO = "res/Audios/";
    static final String EXTENSION_FICHIER_AUDIO = ".wav";

    Son(String nomFichierAudio) {
        setFichier(nomFichierAudio);
        jouer();
        boucle();
    }

    public void setFichier(String nomFichierAudio) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + nomFichierAudio + EXTENSION_FICHIER_AUDIO));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            // Catch !!
        }
    }

    public void jouer() {

        floatControl.setValue(volumeCourant);
        clip.setFramePosition(0);
        clip.start();
    }

    public void boucle() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void augmenterVolume() {
        volumeCourant = volumeCourant + 1.0f;
        System.out.println("Volume : " + volumeCourant);
        if (volumeCourant > 6.0f) {
            volumeCourant = 6.0f;
        }
        floatControl.setValue(volumeCourant);
    }

    public void diminuerVolume() {
        volumeCourant = volumeCourant - 1.0f;
        System.out.println("Volume : " + volumeCourant);
        if (volumeCourant < - 80.0f) {
            volumeCourant = - 80.0f;
        }
        floatControl.setValue(volumeCourant);
    }

    public void muterVolume(JSlider boutonGlissant) {
        if (estMuter == false) {
            volumePrecedent = volumeCourant;
            System.out.println("Volume : " + volumeCourant);
            volumeCourant = - 80.0f;
            floatControl.setValue(volumeCourant);
            estMuter = true;

            boutonGlissant.setValue(boutonGlissant.getMinimum());
        } else if (estMuter == true) {
            volumeCourant = volumePrecedent;
            boutonGlissant.setValue((int) volumeCourant);
            System.out.println("Volume : " + volumeCourant);
            floatControl.setValue(volumeCourant);
            estMuter = false;
        }
    }
}
