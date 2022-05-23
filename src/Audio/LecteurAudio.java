package Audio;

import java.io.File;
import java.util.Scanner;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class LecteurAudio {

    Long momentActuel;
    Clip clip;

    String etatClip;

    float gererVolume = 0;
    FloatControl volume;

    AudioInputStream audioInputStream;
    static final String CHEMIN_FICHIER_AUDIO = "res/Audios/";
    static final String EXTENSION_FICHIER_AUDIO = ".wav";

    String nomFichierAudio = "the-weeknd-medieval";

    // =========================
    // ===== CONSTRUCTEUR  =====
    // =========================
    public LecteurAudio(String nomFichierAudio) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        
        audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + nomFichierAudio + EXTENSION_FICHIER_AUDIO));
        
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // ======================
    // ===== MAIN TEST  =====
    // ======================
    public static void main(String[] args) {
        try {

            String nomFichierAudio = "the-weeknd-medieval";
            LecteurAudio lecteurAudio = new LecteurAudio(nomFichierAudio);
            lecteurAudio.play();

            Scanner scanned = new Scanner(System.in);

            // show the options
            while (true) {
                System.out.println("1. Pause");
                System.out.println("2. Reprendre");
                System.out.println("3. Redemarrer");
                System.out.println("4. Stopper");
                System.out.println("5. Changer");
                System.out.println("6. Volume --");
                System.out.println("7. Volume ++");
                int a = scanned.nextInt();
                lecteurAudio.gotoChoice(a);
                if (a == 4)
                    break;
            }
            scanned.close();
        }

        catch (Exception e) {
            System.out.println("Erreur rencontree durant la lecture du fichier audio !");
            e.printStackTrace();
        }
    }

    public void gotoChoice(int a)
            throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        switch (a) {
            case 1:
                pause();
                break;
            case 2:
                resumeAudio();
                break;
            case 3:
                restart();
                break;
            case 4:
                stop();
                break;
            case 5:
                if (audioInputStream.toString() == "res/Audios/gangstas-paradise-medieval.wav") {
                    stop();
                    audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + "gangstas-paradise-medieval" + EXTENSION_FICHIER_AUDIO));
        
                    // the reference to the clip
                    clip = AudioSystem.getClip();

                    clip.open(audioInputStream);

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    restart();

                } else {
                    stop();
                    audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + "the-weeknd-medieval" + EXTENSION_FICHIER_AUDIO));
        
                    // the reference to the clip
                    clip = AudioSystem.getClip();

                    clip.open(audioInputStream);

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    restart();
                }
                break;
            case 6:
                // 1.0 => 0.1
                gererVolume = (float) (gererVolume - 1.0);
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (gererVolume >= volume.getMinimum()) {
                    volume.setValue(gererVolume);
                } else {
                    System.out.println("Volume au minimum !!");
                    gererVolume = (float) (gererVolume + 1.0);
                }
                break;
            case 7:
                gererVolume = (float) (gererVolume + 1.0);
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (gererVolume <= volume.getMaximum()) {
                    volume.setValue(gererVolume);
                } else {
                    System.out.println("Volume au maximum !!");
                    gererVolume = (float) (gererVolume - 1.0);
                }
                break;
        }
    }

    public void play() {
        // start the clip
        clip.start();
        etatClip = "play";
    }

    public void pause() {
        if (etatClip.equals("paused")) {
            System.out.println("L'audio est en pause !");
            return;
        }
        this.momentActuel = this.clip.getMicrosecondPosition();
        clip.stop();
        etatClip = "paused";
    }

    public void resumeAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (etatClip.equals("play")) {
            System.out.println("L'audio demarre !");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(momentActuel);
        this.play();
    }

    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(1000);
        this.play();
    }

    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        momentActuel = 0L;
        clip.stop();
        clip.close();
    }

    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(CHEMIN_FICHIER_AUDIO + nomFichierAudio + EXTENSION_FICHIER_AUDIO));
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}