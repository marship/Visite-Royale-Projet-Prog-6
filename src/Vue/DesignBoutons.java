package Vue;

import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.SwingConstants;
import Global.Configuration;



import java.io.File;
import java.io.IOException;

public class DesignBoutons extends JButton {

    public DesignBoutons(String txt, String icon, String iconHover, int tailleFont) throws IOException {
        super(txt);

        setForeground(Color.BLACK);

        setOpaque(false);
        setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
        setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
        setFocusPainted(false); // On n'affiche pas l'effet de focus.

        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        
        BufferedImage image;
        InputStream in = Configuration.charge("Images" + File.separator + icon + ".png");
        image = ImageIO.read(in);
        setIcon(new ImageIcon(image));


        BufferedImage imageHover;
        in = Configuration.charge("Images" + File.separator + iconHover + ".png");
        imageHover = ImageIO.read(in);
        setRolloverIcon(new ImageIcon(imageHover));

        setFont(new Font(Font.SANS_SERIF,  Font.BOLD, tailleFont));
    }

    public DesignBoutons(String txt, String icon, int tailleFont) throws IOException {
        super(txt);
        setForeground(Color.BLACK);

        setOpaque(false);
        setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
        setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
        setFocusPainted(false); // On n'affiche pas l'effet de focus.

        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);

        BufferedImage image;
        InputStream in = Configuration.charge("Images" + File.separator + icon + ".png");
        image = ImageIO.read(in);

        setIcon(new ImageIcon(image));
        setFont(new Font(Font.SANS_SERIF,  Font.BOLD, tailleFont));
    }
}