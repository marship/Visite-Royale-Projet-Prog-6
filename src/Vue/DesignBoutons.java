package Vue;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class DesignBoutons extends JButton {

    public DesignBoutons(String txt, String icon, String iconHover) {
        super(txt);
        setForeground(Color.BLACK);

        setOpaque(false);
        setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
        setFocusPainted(false); // On n'affiche pas l'effet de focus.

        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);

        setIcon(new ImageIcon(icon));
        setRolloverIcon(new ImageIcon(iconHover));
    }

    public DesignBoutons(String txt, String icon) {
        super(txt);
        setForeground(Color.BLACK);

        setOpaque(false);
        setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
        setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
        setFocusPainted(false); // On n'affiche pas l'effet de focus.

        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);

        setIcon(new ImageIcon(icon));
    }
}