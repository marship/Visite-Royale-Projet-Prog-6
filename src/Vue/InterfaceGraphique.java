package Vue;

import Modele.Plateau;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable {
	
    Plateau plateau;
	CollecteurEvenements collecteurEvenements;

	InterfaceGraphique(Plateau p, CollecteurEvenements cEvenements) {
		plateau = p;
		collecteurEvenements = cEvenements;
	}

	public static void demarrer(Plateau plateau, CollecteurEvenements cEvenements) {
		SwingUtilities.invokeLater(new InterfaceGraphique(plateau, cEvenements));
	}

	@Override
	public void run() {

		JFrame fenetre = new JFrame("Visite Royale");

		PlateauGraphique plateauGraphique = new PlateauGraphique(plateau);
		plateauGraphique.addMouseListener(new AdaptateurSouris(plateauGraphique, collecteurEvenements));
		fenetre.add(plateauGraphique);
		
        Box barre = Box.createVerticalBox();
		for (int i=0; i<2; i++) {
			barre.add(new JLabel("Joueur " + (i+1)));
			JToggleButton but = new JToggleButton("IA");
			but.addActionListener(new AdaptateurJoueur(collecteurEvenements, but, i));
			barre.add(but);
		}
        barre.add(Box.createGlue());
		fenetre.add(barre, BorderLayout.LINE_END);

		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(500, 500);
		fenetre.setVisible(true);
	}
}