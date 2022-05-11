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
	ArrierePlan panelOptions;

	InterfaceGraphique(Plateau p, CollecteurEvenements cEvenements) {
		plateau = p;
		collecteurEvenements = cEvenements;
	}

	public static void demarrer(Plateau plateau, CollecteurEvenements cEvenements) {
		SwingUtilities.invokeLater(new InterfaceGraphique(plateau, cEvenements));
	}


	//TODO Rajouter dans panel courant: panelOption, panelRegles
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
		creerOptions();
		panelCourant.add(panelOptions, "Options");

		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

		fenetre.add(panelCourant);

		// Garde à jour l'interface graphique du controleur
        collecteurEvenements.getInterfaceGraphique(this);

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(1280, 720);
		fenetre.setVisible(true);
	}

	//affiche le panel passé en paramètre
	public void afficher_panel(String nomPanel){

		layout.show(panelCourant, nomPanel);
    } 

	//Crée le JPanel du menu principal
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

	//Crée le JPanel du plateau de jeu
	//TODO Faire l'affichage du plateau de jeu
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
	//Crée le JPanel des regles
	//TODO Faire l'affichage des regles
	public void creerRegles(){

	}

	//Crée le JPanel des options
	//TODO Faire l'affichage des options
	public void creerOptions(){
		ArrierePlan backgroundBoutons = new ArrierePlan("res/Images/texture_fond.jpg");
		backgroundBoutons.setLayout(null);
		backgroundBoutons.setBounds(110, 200, 500, 300);
		
	
		panelOptions = new ArrierePlan("res/Images/ratos.jpg");
		panelOptions.setLayout(new GridLayout(0, 1, 0, 30));
		panelOptions.setBorder(new EmptyBorder(240,640/4,50,640/4));
		panelOptions.setBounds(320, 320, 640, 640);
	
		panelOptions.add(backgroundBoutons);
		
		JLabel txtMusique = new JLabel("Musique");
		JSlider musique = new JSlider(0, 100, 100);
		musique.setMajorTickSpacing(25);
		musique.setMinorTickSpacing(5);
		musique.setPaintTicks(true);
		musique.setPaintLabels(true);
	
		
		Container musiqueBox = Box.createHorizontalBox();
		musiqueBox.add(musique);
		musiqueBox.add(Box.createHorizontalGlue());
		musiqueBox.add(txtMusique);
	
		panelOptions.add(musiqueBox);
	
		JLabel txtSon = new JLabel("Vol son");
		JSlider son = new JSlider(0, 100, 100);
		son.setMajorTickSpacing(25);
		son.setMinorTickSpacing(5);
		son.setPaintTicks(true);
		son.setPaintLabels(true);
		son.setMinimumSize(new Dimension(100,10));
	
		Container sonBox = Box.createHorizontalBox();
		sonBox.add(son);
		sonBox.add(Box.createHorizontalGlue());
		sonBox.add(txtSon);
	
		panelOptions.add(sonBox);
	
		String[] labelsBoutons = {
			"Crédits",
			"Confirmer les options",
			"Retour à l'accueil",
		};
	
		DesignBoutons b = null;
		for (int i = 0; i < labelsBoutons.length; i++) {
			b = new DesignBoutons(labelsBoutons[i], "res/Images/textureBouton.jpg");
			panelOptions.add(b);
		}	
	}

}