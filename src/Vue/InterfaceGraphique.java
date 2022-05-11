package Vue;

import Modele.Plateau;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;


public class InterfaceGraphique implements Runnable {
	
    Plateau plateau;
	CollecteurEvenements collecteurEvenements;

	JButton bouton_jouer, bouton_charger, bouton_regles, bouton_options, bouton_quitter;

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
		
        /*Box barre = Box.createVerticalBox();
		for (int i=0; i<2; i++) {
			barre.add(new JLabel("Joueur " + (i+1)));
			JToggleButton but = new JToggleButton("IA");
			but.addActionListener(new AdaptateurJoueur(collecteurEvenements, but, i));
			barre.add(but);
		}
        barre.add(Box.createGlue());
		fenetre.add(barre, BorderLayout.LINE_END); */

		JPanel panelBoutons = new JPanel(new GridLayout(0,1,0,30));
    panelBoutons.setBorder(new EmptyBorder(240,640/4,50,640/4));

    bouton_jouer = new JButton("Jouer");
    bouton_jouer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Jouer"));
    panelBoutons.add(bouton_jouer);

    bouton_charger = new JButton("Charger une partie");
    bouton_charger.addActionListener(new AdaptateurCommande(collecteurEvenements, "Charger"));
    panelBoutons.add(bouton_charger);

    bouton_regles = new JButton("Règles du jeu");
    bouton_regles.addActionListener(new AdaptateurCommande(collecteurEvenements, "Regles"));
    panelBoutons.add(bouton_regles);

    bouton_options = new JButton("Options");
    bouton_options.addActionListener(new AdaptateurCommande(collecteurEvenements, "Options"));
    panelBoutons.add(bouton_options);

    bouton_quitter = new JButton("Quitter");
    bouton_quitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
    panelBoutons.add(bouton_quitter);
    


    fenetre.add(panelBoutons);

		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(500, 500);
		fenetre.setVisible(true);
	}
}