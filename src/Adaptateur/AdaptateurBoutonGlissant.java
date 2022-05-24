package Adaptateur;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Audio.Son;

public class AdaptateurBoutonGlissant implements ChangeListener {

    Son audio;
    JSlider boutonGlissantAudio;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurBoutonGlissant(Son a, JSlider bGlissantAudio) {
        audio = a;
        boutonGlissantAudio = bGlissantAudio;
    }

    // =========================
    // ===== BOUGE CURSEUR =====
    // =========================
    @Override
    public void stateChanged(ChangeEvent e) {
        audio.volumeCourant = boutonGlissantAudio.getValue();
        if (audio.volumeCourant == -24) {
            audio.volumeCourant = -80;
        }
        audio.floatControl.setValue(audio.volumeCourant);
    }
}
