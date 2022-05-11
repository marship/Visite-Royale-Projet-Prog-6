package Vue;

import java.awt.Graphics;
 
import javax.swing.ImageIcon;
import javax.swing.JPanel;
 
public class ArrierePlan extends JPanel {
 
    private ImageIcon background;
   
    public ArrierePlan(String fileName) {
        super();
        //this.setLayout(new GridLayout());
        this.background = new ImageIcon(fileName);
    }
 
    public void setBackground(ImageIcon background) {
        this.background = background;
    }
 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, this);
    }
}