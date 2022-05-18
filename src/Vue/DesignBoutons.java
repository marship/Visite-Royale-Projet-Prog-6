package Vue;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
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

        ImageIcon iconeBoutton = new ImageIcon(icon);
        setIcon(iconeBoutton);
        Image img = iconeBoutton.getImage() ;  
        Image newimg = img.getScaledInstance(getWidth(), getHeight(), java.awt.Image.SCALE_SMOOTH ) ;  
        iconeBoutton = new ImageIcon(newimg);
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