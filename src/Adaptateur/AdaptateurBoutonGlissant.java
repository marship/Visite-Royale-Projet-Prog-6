package Adaptateur;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Audio.Son;

public class AdaptateurBoutonGlissant implements ChangeListener {

    // ======================
    // ===== CONSTANTES =====
    // ======================
    final static int VALEUR_MINIMALE_BOUTON_GLISSANT = -24;
    final static int VALEUR_MUTE = -80;

    // =====================
    // ===== ATTRIBUTS =====
    // =====================
    JSlider boutonGlissantAudio;
    Son audio;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public AdaptateurBoutonGlissant(Son a, JSlider bGlissantAudio) {
        audio = a;
        boutonGlissantAudio = bGlissantAudio;
    }

    // ==========================
    // ===== BOUGER CURSEUR =====
    // ==========================
    @Override
    public void stateChanged(ChangeEvent e) {
        audio.volumeCourant = boutonGlissantAudio.getValue();
        if (audio.volumeCourant <= VALEUR_MINIMALE_BOUTON_GLISSANT) {
            audio.volumeCourant = VALEUR_MUTE;
        }
        audio.floatControl.setValue(audio.volumeCourant);
    }
}