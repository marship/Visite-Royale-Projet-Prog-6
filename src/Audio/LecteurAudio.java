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

    // define storage for start position
    Long momentActuel;
    Clip clip;

    // get the clip status
    String etatClip;

    float gainAmount = 0;
    FloatControl volume;

    AudioInputStream audioStream;
    static String thePath;

    // initialize both the clip and streams
    public LecteurAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        
        // the input stream object
        audioStream = AudioSystem.getAudioInputStream(new File(thePath));
        
        // the reference to the clip
        clip = AudioSystem.getClip();

        clip.open(audioStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void main(String[] args) {
        try {
            // add the path to the audio file
            thePath = "res/Audios/the-weeknd-medieval.wav";

            LecteurAudio simpleSoundPlayer = new LecteurAudio();

            simpleSoundPlayer.play();
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
                simpleSoundPlayer.gotoChoice(a);
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

    // operation is now as per the user's choice

    private void gotoChoice(int a)
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
                if (thePath == "res/Audios/gangstas-paradise-medieval.wav") {
                    stop();
                    thePath = "res/Audios/the-weeknd-medieval.wav";
                    // the input stream object
                    audioStream = AudioSystem.getAudioInputStream(new File(thePath));
        
                    // the reference to the clip
                    clip = AudioSystem.getClip();

                    clip.open(audioStream);

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    restart();

                } else {
                    stop();
                    thePath = "res/Audios/gangstas-paradise-medieval.wav";

                    // the input stream object
                    audioStream = AudioSystem.getAudioInputStream(new File(thePath));
        
                    // the reference to the clip
                    clip = AudioSystem.getClip();

                    clip.open(audioStream);

                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    restart();
                }
                break;
            case 6:
                // 1.0 => 0.1
                gainAmount = (float) (gainAmount - 1.0);
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (gainAmount >= volume.getMinimum()) {
                    volume.setValue(gainAmount);
                } else {
                    System.out.println("Volume au minimum !!");
                    gainAmount = (float) (gainAmount + 1.0);
                }
                break;
            case 7:
                gainAmount = (float) (gainAmount + 1.0);
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                if (gainAmount <= volume.getMaximum()) {
                    volume.setValue(gainAmount);
                } else {
                    System.out.println("Volume au maximum !!");
                    gainAmount = (float) (gainAmount - 1.0);
                }
                break;
        }
    }

    // play
    public void play() {
        // start the clip
        clip.start();
        etatClip = "play";
    }

    // Pause audio
    public void pause() {
        if (etatClip.equals("paused")) {
            System.out.println("L'audio est en pause !");
            return;
        }
        this.momentActuel = this.clip.getMicrosecondPosition();
        clip.stop();
        etatClip = "paused";
    }

    // resume audio
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

    // restart audio
    public void restart() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(1000);
        this.play();
    }

    // stop audio
    public void stop() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        momentActuel = 0L;
        clip.stop();
        clip.close();
    }

    // reset the audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioStream = AudioSystem.getAudioInputStream(new File(thePath));
        clip.open(audioStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}