package Vue;

import Modele.Jeu;
import Pattern.Observateur;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;


public class InterfaceGraphique implements Runnable, InterfaceUtilisateur, Observateur {
	
    Jeu jeu;
	CollecteurEvenements collecteurEvenements;

    PlateauGraphique plateauGraphique;
	DesignBoutons boutonJouer, boutonCharger, boutonRegles, boutonOptions, boutonQuitter, boutonCredits, 
                    boutonConfirmer, boutonRetourAccueil, boutonValider, boutonAnnuler, boutonJoker, boutonMenu, boutonFinDeTour;
                    
	CardLayout layout; 

    JPanel panelCourant, panelMenuPrincipal, panelPlateau, panelSelectionJoueurs;
	ArrierePlan panelOptions;
    JFrame fenetre;

	InterfaceGraphique(Jeu j, CollecteurEvenements cEvenements) {
		jeu = j;
		collecteurEvenements = cEvenements;
	}

	public static void demarrer(Jeu jeu, CollecteurEvenements cEvenements) {
        InterfaceGraphique vue = new InterfaceGraphique(jeu, cEvenements);
		cEvenements.ajouteInterfaceUtilisateur(vue);
        SwingUtilities.invokeLater(vue);
        // Garde à jour l'interface graphique du controleur
	}


	//TODO Rajouter dans panel courant: panelOption, panelRegles
	@Override
	public void run() {

		fenetre = new JFrame("Visite Royale");

		layout = new CardLayout();
		panelCourant = new JPanel(layout);

        creerPlateauJeu();
		panelCourant.add(plateauGraphique, "Plateau");
		creerMenuPrincipal();
        panelCourant.add(panelMenuPrincipal, "MenuPrincipal");
        creerSelectionJoueurs();
		panelCourant.add(panelSelectionJoueurs, "Jouer");
		creerOptions();
		panelCourant.add(panelOptions, "Options");

		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

		fenetre.add(panelCourant);

        // Garde à jour l'interface graphique du controleur
        collecteurEvenements.ajouteInterfaceUtilisateur(this);

                
        // Mise en place des Listeners
        ((Component) plateauGraphique).addMouseMotionListener(new AdaptateurSourisMouvement(plateauGraphique, collecteurEvenements));
        ((Component) plateauGraphique).addMouseListener(new AdaptateurSouris(plateauGraphique, collecteurEvenements));
        fenetre.addKeyListener(new AdaptateurClavier(collecteurEvenements));
        fenetre.setFocusable(true);

		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(1280, 720);
		fenetre.setVisible(true);
	}

	//affiche le panel passé en paramètre
	public void afficherPanel(String nomPanel){

		layout.show(panelCourant, nomPanel);
    } 

	//Crée le JPanel du menu principal
	public void creerMenuPrincipal(){

		panelMenuPrincipal = new ArrierePlan("res/Images/backgroundMenu.png");
		panelMenuPrincipal.setLayout(new GridLayout(0,1,0,30));
		panelMenuPrincipal.setBorder(new EmptyBorder(240,640/4,50,640/4));

        boutonJouer = new DesignBoutons("Jouer", "res/Images/textureBouton.png");
        boutonJouer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Jouer"));
        panelMenuPrincipal.add(boutonJouer);

        boutonCharger = new DesignBoutons("Charger une partie", "res/Images/textureBouton.png");
        boutonCharger.addActionListener(new AdaptateurCommande(collecteurEvenements, "Charger"));
        panelMenuPrincipal.add(boutonCharger);

        boutonRegles = new DesignBoutons("Règles du jeu", "res/Images/textureBouton.png");
        boutonRegles.addActionListener(new AdaptateurCommande(collecteurEvenements, "Regles"));
        panelMenuPrincipal.add(boutonRegles);

        boutonOptions = new DesignBoutons("Options", "res/Images/textureBouton.png");
        boutonOptions.addActionListener(new AdaptateurCommande(collecteurEvenements, "Options"));
        panelMenuPrincipal.add(boutonOptions);

        boutonQuitter = new DesignBoutons("Quitter", "res/Images/textureBouton.png");
        boutonQuitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
        panelMenuPrincipal.add(boutonQuitter);
    } 

    public void creerSelectionJoueurs(){

        panelSelectionJoueurs = new ArrierePlan("res/Images/backgroundSelection.png");
        panelSelectionJoueurs.setLayout(new GridBagLayout());

        int height = 720;
        int width = 1280;

        int borderTop = height / 2;
        int borderBottom = height / 3;
        int borderSides = width / 3;

        panelSelectionJoueurs.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        GridBagConstraints test = new GridBagConstraints();

        String[] choixComboBox = {
            "Humain",
            "IA Aléatoire",
            "IA experte"
        };

        test.fill = GridBagConstraints.HORIZONTAL;
        test.insets = new Insets(10,0,0,10);  //top padding

        JLabel nomJoueur1 = new JLabel("Nom du Joueur 1");
        test.weightx = 0.5;
        test.gridx = 0;
        test.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueur1, test);


        JTextField valeurNomJoueur1 = new JTextField();
        test.gridx = 0;
        test.gridy = 1;  
        panelSelectionJoueurs.add(valeurNomJoueur1, test);


        JComboBox<String> comboBoxJoueur1 = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueur1.addItem(choixComboBox[i]);
        }
        comboBoxJoueur1.setFocusable(false);
        //comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueur1.getSelectedItem().toString()));
        test.ipady = 0;
        test.gridx = 0;
        test.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueur1, test);

        test.insets = new Insets(10,10,0,10);  //top padding

        JLabel nomJoueur2 = new JLabel("Nom du Joueur 2");
        test.gridx = 1;
        test.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueur2, test);

        JTextField valeurNomJoueur2 = new JTextField();
        test.gridx = 1;
        test.gridy = 1;  
        panelSelectionJoueurs.add(valeurNomJoueur2, test);

        JComboBox<String> comboBoxJoueur2 = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueur2.addItem(choixComboBox[i]);
        }
        comboBoxJoueur2.setFocusable(false);
        //comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueur1.getSelectedItem().toString()));
        
        test.gridx = 1;
        test.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueur2, test);
        
        test.anchor = GridBagConstraints.PAGE_END; //bottom of space
        test.insets = new Insets(50,20,0,20);  //padding des boutons 
        test.gridx = 0;
        test.gridy = 3;
        test.weightx = 0.5;

        DesignBoutons valider = new DesignBoutons("Valider", "res/Images/textureBouton.png");
        System.out.println(valider.getBounds());
        valider.addActionListener(new AdaptateurCommande(collecteurEvenements, "Valider"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(valider, test);
        
        test.gridx = 1;
        DesignBoutons annuler = new DesignBoutons("Annuler", "res/Images/textureBouton.png");
        annuler.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(annuler, test);



    }

//Crée le JPanel du plateau de jeu
	//TODO Faire l'affichage du plateau de jeu
	public void creerPlateauJeu(){

        plateauGraphique = new PlateauGraphique(jeu);
        //GridBagConstraints layoutConstraint = new GridBagConstraints();

        Box boxBoutons = Box.createHorizontalBox();
		boutonJoker = new DesignBoutons("Joker", "res/Images/textureBouton.png");
        boutonJoker.addActionListener(new AdaptateurCommande(collecteurEvenements, "Joker"));
		//layoutConstraint.gridx = 0;
		//layoutConstraint.gridy = 0;
        boxBoutons.add(boutonJoker);

		boutonMenu = new DesignBoutons("Menu", "res/Images/textureBouton.png");
        boutonMenu.addActionListener(new AdaptateurCommande(collecteurEvenements, "Menu"));
        //layoutConstraint.gridx = 0;
		//layoutConstraint.gridy = 1;
        boxBoutons.add(boutonMenu);

        boutonFinDeTour = new DesignBoutons("Fin de tour", "res/Images/textureBouton.png");
        boutonFinDeTour.addActionListener(new AdaptateurCommande(collecteurEvenements, "FinDeTour"));
        //layoutConstraint.gridx = 0;
		//layoutConstraint.gridy = 2;
        boxBoutons.add(boutonFinDeTour);

        plateauGraphique.add(boxBoutons);

		/*layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = 1;
        panelPlateau.add(boutonMenu, layoutConstraint);

		layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = 0;
		panelPlateau.add(imglabel, layoutConstraint); */
    } 

    public void miseAJourFinDeTour() {
        jeu.finDeTour();
        fenetre.repaint();
    }
	//Crée le JPanel des regles
	//TODO Faire l'affichage des regles
	public void creerRegles(){

	}

	    //Crée le JPanel des options
    //TODO Faire l'affichage des options
    public void creerOptions(){


        panelOptions = new ArrierePlan("res/Images/backgroundOptions.png");
        panelOptions.setLayout(new GridLayout(0, 1, 0, 30));

        int height = 720;
        int width = 1280;

        int borderTop = height / 4;
        int borderBottom = height / 10;
        int borderSides = width / 3;

        System.out.println("top = " + borderTop + " , sides = " + borderSides + " , bottom = " + borderBottom );
        panelOptions.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));

        JLabel txtMusique = new JLabel("Volume musique");
        JSlider musique = new JSlider(0, 100, 100);
        musique.setMajorTickSpacing(25);
        musique.setMinorTickSpacing(5);
        musique.setOpaque(false);
        musique.setPaintLabels(true);
    
        
        Container musiqueBox = Box.createHorizontalBox();
        musiqueBox.add(musique);
        musiqueBox.add(Box.createHorizontalGlue());
        musiqueBox.add(txtMusique);
    
        panelOptions.add(musiqueBox);
    
        JLabel txtSon = new JLabel("Volume son");
        JSlider son = new JSlider(0, 100, 100);
        son.setMajorTickSpacing(25);
        son.setMinorTickSpacing(5);
        son.setOpaque(false);
        son.setPaintLabels(true);
    
        Container sonBox = Box.createHorizontalBox();
        sonBox.add(son);
        sonBox.add(Box.createHorizontalGlue());
        sonBox.add(txtSon);
    
        panelOptions.add(sonBox);

        boutonCredits = new DesignBoutons("Crédits", "res/Images/textureBouton.png");
        boutonCredits.addActionListener(new AdaptateurCommande(collecteurEvenements, "Credits"));
        panelOptions.add(boutonCredits);

        boutonConfirmer = new DesignBoutons("Confirmer les Options", "res/Images/textureBouton.png");
        boutonConfirmer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Confirmation"));
        panelOptions.add(boutonConfirmer);

        boutonRetourAccueil = new DesignBoutons("Retour à l'accueil", "res/Images/textureBouton.png");
        boutonRetourAccueil.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        panelOptions.add(boutonRetourAccueil);
    }
    
    @Override
    public void miseAJour() {
        
    }
}