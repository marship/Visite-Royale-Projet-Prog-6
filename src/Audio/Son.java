package Audio;

import java.io.File;
// import java.io.ObjectInputFilter.Config;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JSlider;

import Global.Configuration;

public class Son {
    
    // ======================
    // ===== CONSTANTES =====
    // ======================

    // static final String GANGSTAS = "gangstas-paradise-medieval";
    // static final String WEEKND = "the-weeknd-medieval";
    static final String SON = "Son_Bouton";

    static final String CHEMIN_FICHIER_AUDIO = "res/Audios/";
    static final String EXTENSION_FICHIER_AUDIO = ".wav";
    static final float VOLUME_PAR_DEFAUT = -16;

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    AudioInputStream audioInputStream;
    public FloatControl floatControl;
    Clip clip;

    // =====================
    // ===== VARIABLES =====
    // =====================
    public float volumeCourant = VOLUME_PAR_DEFAUT;

    float volumePrecedentMute = VOLUME_PAR_DEFAUT;
    boolean estMuter = false;

    /////////////////////////////////////////////////////////////////////////

    // =========================
    // ===== CONSTRUCTEURS =====
    // =========================
    public Son(String nomFichierAudio) {
        recupererFichierAudio(nomFichierAudio);
        if (nomFichierAudio == SON) {
            ajusterVolumeEffetSonnore();
        }
        jouer();
        if (nomFichierAudio != SON) {
            boucle();
        }
    }

    public Son() {
        new Son(SON);
    }

    // =============================
    // ===== RECUPERER FICHIER =====
    // =============================
    public void recupererFichierAudio(String nomFichierAudio) {
        try {
            InputStream inputStream = Configuration.charge("Audios" + File.separator + nomFichierAudio + ".wav");

            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(inputStream);
            AudioFormat audioFormat = fileFormat.getFormat();

            long length = (long) (1000 * fileFormat.getFrameLength() / fileFormat.getFormat().getFrameRate());
            int bitrate = Math.round(audioFormat.getFrameSize() * audioFormat.getFrameRate() / 1000);

            AudioInputStream audioInputStream = new AudioInputStream(inputStream, audioFormat, length * bitrate);
            
            // AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + nomFichierAudio + EXTENSION_FICHIER_AUDIO));
            
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            Configuration.instance().logger().severe("Echec de recuperation du fichier audio : " + nomFichierAudio + EXTENSION_FICHIER_AUDIO);
        }
    }

    // =========================
    // ===== ACTIONS AUDIO =====
    // =========================
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
        if (volumeCourant > 6.0f) {
            volumeCourant = 6.0f;
        }
        floatControl.setValue(volumeCourant);
    }

    public void diminuerVolume() {
        volumeCourant = volumeCourant - 1.0f;
        if (volumeCourant < - 80.0f) {
            volumeCourant = - 80.0f;
        }
        floatControl.setValue(volumeCourant);
    }

    public void muterVolume(JSlider boutonGlissant) {
        if (estMuter == false) {
            volumePrecedentMute = volumeCourant;
            volumeCourant = - 80.0f;
            floatControl.setValue(volumeCourant);
            estMuter = true;

            boutonGlissant.setValue(boutonGlissant.getMinimum());
        } else if (estMuter == true) {
            volumeCourant = volumePrecedentMute;
            boutonGlissant.setValue((int) volumeCourant);
            floatControl.setValue(volumeCourant);
            estMuter = false;
        }
    }

    public void ajusterVolumeEffetSonnore() {
        volumeCourant = 1.0f;
        floatControl.setValue(volumeCourant);
    }
}
