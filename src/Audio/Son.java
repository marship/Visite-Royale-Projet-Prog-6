package Audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JSlider;

import Global.Configuration;

public class Son {

    // ======================
    // ===== CONSTANTES =====
    // ======================

    // static final String CHEMIN_FICHIER_AUDIO = "res/Audios/";
    static final String EXTENSION_FICHIER_AUDIO = ".wav";
    static final float VOLUME_PAR_DEFAUT = -16;
    static final String SON = "Son";

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
        if (nomFichierAudio != SON) {
            jouer();
            boucle();
        }
    }

    // =============================
    // ===== RECUPERER FICHIER =====
    // =============================
    public void recupererFichierAudio(String nomFichierAudio) {
        // try {
            // AudioInputStream audioInputStream =
            // Configuration.instance().chargeAudio(nomFichierAudio);

            /*
             * InputStream inputStream = Configuration.chargeAudio(File.separator + "Audios"
             * + File.separator + nomFichierAudio + ".wav");
             * 
             * AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(inputStream);
             * AudioFormat audioFormat = fileFormat.getFormat();
             * 
             * float length = 1000 * fileFormat.getFrameLength() /
             * fileFormat.getFormat().getFrameRate();
             * int bitrate = Math.round(audioFormat.getFrameSize() *
             * audioFormat.getFrameRate() / 1000);
             * 
             * audioInputStream = new AudioInputStream(inputStream, audioFormat, (long)
             * (length * bitrate));
             */

            /*
             * InputStream inputStream = getClass().getResourceAsStream("/test1/bark.wav");
             * AudioInputStream audioInputStream = new AudioInputStream();
             */

            // clip = AudioSystem.getClip();
            // clip.open(audioInputStream);
            /*
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            Configuration.instance().logger().severe("Echec de recuperation du fichier audio : " + nomFichierAudio + EXTENSION_FICHIER_AUDIO);
            e.printStackTrace();
        }
        */

        // URL url = this.getClass().getResource("Audios" + File.separator + nomFichierAudio + ".wav");
        // final URL resourceUrl = classLoader.getResource
        
        URL url = ClassLoader.getSystemClassLoader().getResource("Audios" + File.separator + nomFichierAudio + ".wav");
        // AudioInputStream audioInputStream;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(url);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            // clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(AudioSystem.getAudioInputStream(url));
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Configuration.instance().logger().severe("Echec de recuperation du fichier audio : " + nomFichierAudio + EXTENSION_FICHIER_AUDIO);
            e.printStackTrace();
        }

        /*
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("Audios" + File.separator + nomFichierAudio + ".wav");

            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            stream = AudioSystem.getAudioInputStream(url);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(new LineListener() {
    
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });
            clip.open(stream);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            //throw new RuntimeException(e.getClass().getSimpleName() + " " + e.getMessage());
            e.printStackTrace();
        }
        */
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
        if (volumeCourant < -80.0f) {
            volumeCourant = -80.0f;
        }
        floatControl.setValue(volumeCourant);
    }

    public void muterVolume(JSlider boutonGlissant) {
        if (estMuter == false) {
            volumePrecedentMute = volumeCourant;
            volumeCourant = -80.0f;
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