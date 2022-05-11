package Vue;

import Modele.Plateau;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;


public class InterfaceGraphique implements Runnable {
	
    Plateau plateau;
	CollecteurEvenements collecteurEvenements;

	JButton bouton_jouer, bouton_charger, bouton_regles, bouton_options, bouton_quitter;
	CardLayout layout; 

    JPanel panelCourant, panelMenuPrincipal, panelOption, panelPlateau;

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
		layout = new CardLayout();
		panelCourant = new JPanel(layout);

		creerMenuPrincipal();
    	panelCourant.add(panelMenuPrincipal, "MenuPrincipal");
		creerPlateauJeu();
		panelCourant.add(panelPlateau, "Jouer");

		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

		fenetre.add(panelCourant);

		// Garde à jour l'interface graphique du controleur
        collecteurEvenements.getInterfaceGraphique(this);

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(1280, 720);
		fenetre.setVisible(true);
	}

	public void afficher_panel(String nomPanel){

		layout.show(panelCourant, nomPanel);
    } 

	public void creerMenuPrincipal(){

		panelMenuPrincipal = new JPanel(new GridLayout(0,1,0,30));
		panelMenuPrincipal.setBorder(new EmptyBorder(240,640/4,50,640/4));

        bouton_jouer = new JButton("Jouer");
        bouton_jouer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Jouer"));
        panelMenuPrincipal.add(bouton_jouer);

        bouton_charger = new JButton("Charger une partie");
        bouton_charger.addActionListener(new AdaptateurCommande(collecteurEvenements, "Charger"));
        panelMenuPrincipal.add(bouton_charger);

        bouton_regles = new JButton("Règles du jeu");
        bouton_regles.addActionListener(new AdaptateurCommande(collecteurEvenements, "Regles"));
        panelMenuPrincipal.add(bouton_regles);

        bouton_options = new JButton("Options");
        bouton_options.addActionListener(new AdaptateurCommande(collecteurEvenements, "Options"));
        panelMenuPrincipal.add(bouton_options);

        bouton_quitter = new JButton("Quitter");
        bouton_quitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
        panelMenuPrincipal.add(bouton_quitter);
    } 

	public void creerPlateauJeu(){

		panelPlateau = new JPanel(new GridLayout(0,1,0,30));
		panelPlateau.setBorder(new EmptyBorder(240,640/4,50,640/4));
        panelPlateau.setBorder(new EmptyBorder(240,640/4,50,640/4));

		bouton_options = new JButton("Retour");
        bouton_options.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        panelPlateau.add(bouton_options);

        bouton_quitter = new JButton("Quitter");
        bouton_quitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
        panelPlateau.add(bouton_quitter);

    } 

}