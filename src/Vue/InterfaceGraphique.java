package Vue;

import Modele.Jeu;
import Pattern.Observateur;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.EmptyBorder;


public class InterfaceGraphique extends JPanel implements Runnable, InterfaceUtilisateur, Observateur{
	
    Jeu jeu;
	CollecteurEvenements collecteurEvenements;

    PlateauGraphique plateauGraphique;
	DesignBoutons boutonJouer, boutonCharger, boutonRegles, boutonOptions, boutonQuitter, boutonCredits, 
                boutonConfirmer, boutonRetourAccueil, boutonValider, boutonAnnuler, boutonAnnulerJeu, boutonOptionsJeu, boutonFinDeTour, boutonHistorique;
                    
	CardLayout layout; 
    JPanel panelCourant, panelMenuPrincipal, panelPlateau, panelSelectionJoueurs;
	ArrierePlan panelOptions;
    JFrame fenetre;

    private int hauteurFenetre;
    private int largeurFenetre;

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
        fenetre.setSize(1280, 720);
        hauteurFenetre = fenetre.getHeight();
        System.out.println("alled" + hauteurFenetre);
        largeurFenetre = fenetre.getWidth();

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
		
		fenetre.setVisible(true);

 
	}

	//affiche le panel passé en paramètre
	public void afficherPanel(String nomPanel){

		layout.show(panelCourant, nomPanel);
    } 

	//Crée le JPanel du menu principal
	public void creerMenuPrincipal(){

        int borderTop = hauteurFenetre / 3;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 4;

		panelMenuPrincipal = new ArrierePlan("res/Images/backgroundMenu.png");
		panelMenuPrincipal.setLayout(new GridLayout(0,1,0,30));
		panelMenuPrincipal.setBorder(new EmptyBorder(borderTop,borderSides,borderBottom,borderSides));

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

        int borderTop = hauteurFenetre / 2;
        int borderBottom = hauteurFenetre / 3;
        int borderSides = largeurFenetre / 3;

        panelSelectionJoueurs = new ArrierePlan("res/Images/backgroundSelection.png");
        panelSelectionJoueurs.setLayout(new GridBagLayout());

        panelSelectionJoueurs.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        GridBagConstraints gbc = new GridBagConstraints();

        String[] choixComboBox = {
            "Humain",
            "IA Aléatoire",
            "IA experte"
        };

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,0,0,10);  //top padding

        JLabel nomJoueur1 = new JLabel("Nom du Joueur 1");
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueur1, gbc);


        JTextField valeurNomJoueur1 = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 1;  
        panelSelectionJoueurs.add(valeurNomJoueur1, gbc);


        JComboBox<String> comboBoxJoueur1 = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueur1.addItem(choixComboBox[i]);
        }
        comboBoxJoueur1.setFocusable(false);
        //comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueur1.getSelectedItem().toString()));
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueur1, gbc);

        gbc.insets = new Insets(10,10,0,10);  //top padding

        JLabel nomJoueur2 = new JLabel("Nom du Joueur 2");
        gbc.gridx = 1;
        gbc.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueur2, gbc);

        JTextField valeurNomJoueur2 = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;  
        panelSelectionJoueurs.add(valeurNomJoueur2, gbc);

        JComboBox<String> comboBoxJoueur2 = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueur2.addItem(choixComboBox[i]);
        }
        comboBoxJoueur2.setFocusable(false);
        //comboBox.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueur1.getSelectedItem().toString()));
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueur2, gbc);
        
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.insets = new Insets(50,20,0,20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.5;

        DesignBoutons valider = new DesignBoutons("Valider", "res/Images/textureBouton.png");
        System.out.println(valider.getBounds());
        valider.addActionListener(new AdaptateurCommande(collecteurEvenements, "Valider"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(valider, gbc);
        
        gbc.gridx = 1;
        DesignBoutons annuler = new DesignBoutons("Annuler", "res/Images/textureBouton.png");
        annuler.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(annuler, gbc);



    }

    //Crée le JPanel du plateau de jeu
	//TODO Faire l'affichage du plateau de jeu
	public void creerPlateauJeu(){

        plateauGraphique = new PlateauGraphique(jeu);
        plateauGraphique.setLayout(new BorderLayout());

        Box boxBoutonsSud = Box.createHorizontalBox();
        Box boxBoutonsNord = Box.createHorizontalBox();
        Box boxBoutonFinDeTour =  Box.createVerticalBox();
        
		boutonAnnulerJeu = new DesignBoutons("Annuler", "res/Images/texturePetitBouton.png");
        //boutonAnnulerJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "AnnulerTour"));
        boutonHistorique = new DesignBoutons("Historique", "res/Images/texturePetitBouton.png");
        //boutonHistorique.addActionListener(new AdaptateurCommande(collecteurEvenements, "Historique"));
        boxBoutonsSud.add(boutonAnnulerJeu);
        boxBoutonsSud.add(Box.createGlue());
        boxBoutonsSud.add(boutonHistorique);
        plateauGraphique.add(boxBoutonsSud, BorderLayout.SOUTH);

		boutonOptionsJeu = new DesignBoutons("Options", "res/Images/texturePetitBouton.png");
        //boutonOptionsJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "OptionsJeu"));
        boxBoutonsNord.add(Box.createGlue());
        boxBoutonsNord.add(boutonOptionsJeu);
        plateauGraphique.add(boxBoutonsNord, BorderLayout.NORTH);

        boutonFinDeTour = new DesignBoutons("Fin de tour", "res/Images/texturePetitBouton.png");
        boutonFinDeTour.addActionListener(new AdaptateurCommande(collecteurEvenements, "FinDeTour"));
        int padding = 9 * (hauteurFenetre/14);
        padding = 350;
        boxBoutonFinDeTour.add(Box.createVerticalStrut((int) padding));
        boxBoutonFinDeTour.add(boutonFinDeTour);
        plateauGraphique.add(boxBoutonFinDeTour, BorderLayout.EAST);
        

    } 

    public void miseAJourFinDeTour() {
        fenetre.repaint();
    }
	//Crée le JPanel des regles
	//TODO Faire l'affichage des regles
	public void creerRegles(){

	}

	    //Crée le JPanel des options
    //TODO Faire l'affichage des options
    public void creerOptions(){

        int borderTop = hauteurFenetre / 4;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 3;

        panelOptions = new ArrierePlan("res/Images/backgroundOptions.png");
        panelOptions.setLayout(new GridLayout(0, 1, 0, 30));
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
        fenetre.repaint();
    }

    @Override
    public void afficherPanneau(String nomPanneau) {
        // TODO Auto-generated method stub
    }

    // ================================
    // ====== Previsualisation ========
    // ================================
    @Override
    public void previsualisation(int coupX, int coupY, int largeurPreselection, int hauteurPreselection) {
        plateauGraphique.tracerRectangle(coupX, coupY, largeurPreselection, hauteurPreselection);
        ((Component) plateauGraphique).repaint();
    }
}