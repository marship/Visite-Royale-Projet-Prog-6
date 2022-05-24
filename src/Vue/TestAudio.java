package Vue;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TestAudio {

    // String nomFichierAudio = "gangstas-paradise-medieval";
    String nomFichierAudio = "the-weeknd-medieval";

    Son son = new Son(nomFichierAudio, false);
    JSlider boutonGlissant;
    
    public static void main(String[] args) {
        new TestAudio();
    }

    public TestAudio() {

        JFrame fenetre = new JFrame();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLayout(new GridLayout(1, 3));

        JButton jouerBouton = new JButton("LIRE");
        jouerBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jouerAudio(nomFichierAudio);
            }
        });
        fenetre.add(jouerBouton);

        JButton stopBouton = new JButton("STOP");
        stopBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                son.stop();
            }
        });
        fenetre.add(stopBouton);

        boutonGlissant = new JSlider(-24, 6);
        boutonGlissant.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                son.volumeCourant = boutonGlissant.getValue();
                if (son.volumeCourant == -24) {
                    son.volumeCourant = -80;
                }
                System.out.println("Volume : " + son.volumeCourant);
                son.floatControl.setValue(son.volumeCourant);
            }
        });

        JButton boutonMuter = new JButton("Muter");
        boutonMuter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                son.muterVolume(boutonGlissant);
            }
        });
        fenetre.add(boutonMuter);
        fenetre.add(boutonGlissant);

        fenetre.pack();
        fenetre.setVisible(true);

        // Fichier Audio
        jouerAudio(nomFichierAudio);
    }

    public void jouerAudio(String nomFichierAudio) {
        son.setFichier(nomFichierAudio);
        son.jouer();
        son.boucle();
    }
}
