package Vue;

import Modele.Jeu;
import Pattern.Observateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import javax.swing.border.EmptyBorder;

import Global.InfoJeu;


public class InterfaceGraphique extends JPanel implements Runnable, InterfaceUtilisateur, Observateur{
	
    Jeu jeu;
	CollecteurEvenements collecteurEvenements;

    PlateauGraphique plateauGraphique;
	DesignBoutons boutonJouer, boutonCharger, boutonRegles, boutonOptions, boutonQuitter, boutonCredits, 
                boutonConfirmer, boutonRetourAccueil, boutonValider, boutonAnnuler, boutonAnnulerJeu, boutonOptionsJeu, boutonFinDeTour, boutonHistorique,
                boutonRetourArriere, boutonAide, boutonRecommencer, boutonSauvegarderEtQuitter, boutonRetourJeu;
                    
	CardLayout layout; 
    JPanel panelCourant, panelMenuPrincipal, panelOptions, panelSelectionJoueurs, panelPlateau, panelOptionsJeu;
	 
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
        largeurFenetre = fenetre.getWidth();

        layout = new CardLayout();
		panelCourant = new JPanel(layout);

		fenetre.add(panelCourant);


		Timer chrono = new Timer( 16, new AdaptateurTemps(collecteurEvenements));
		chrono.start();

        try {
            creerMenuPrincipal();
            panelCourant.add(panelMenuPrincipal, "MenuPrincipal");
        
            creerSelectionJoueurs();
            panelCourant.add(panelSelectionJoueurs, "SelectionJoueur");

            creerOptions();
            panelCourant.add(panelOptions, "Options");
            creerPlateauJeu();
            panelCourant.add(plateauGraphique, "Plateau");

            creerOptionsJeu();
            panelCourant.add(panelOptionsJeu, "OptionsJeu");

        } catch (IOException e) {
            System.out.println("nan c'est trop j'abandonne");
            e.printStackTrace();
        }
		
        

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


	//Crée le JPanel du menu principal
	public void creerMenuPrincipal() throws IOException{

        int borderTop = hauteurFenetre / 3;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 4;

		panelMenuPrincipal = new MenuGraphique(InfoJeu.MENU_PRINCIPAL);
		panelMenuPrincipal.setLayout(new GridLayout(0,1,0,30));
		panelMenuPrincipal.setBorder(new EmptyBorder(borderTop,borderSides,borderBottom,borderSides));

        boutonJouer = new DesignBoutons("Jouer", "Texture_Bouton");
        boutonJouer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Jouer"));
        panelMenuPrincipal.add(boutonJouer);

        boutonCharger = new DesignBoutons("Charger une partie", "Texture_Bouton");
        boutonCharger.addActionListener(new AdaptateurCommande(collecteurEvenements, "Charger"));
        panelMenuPrincipal.add(boutonCharger);

        boutonRegles = new DesignBoutons("Règles du jeu", "Texture_Bouton");
        boutonRegles.addActionListener(new AdaptateurCommande(collecteurEvenements, "Regles"));
        panelMenuPrincipal.add(boutonRegles);

        boutonOptions = new DesignBoutons("Options", "Texture_Bouton");
        boutonOptions.addActionListener(new AdaptateurCommande(collecteurEvenements, "Options"));
        panelMenuPrincipal.add(boutonOptions);

        boutonQuitter = new DesignBoutons("Quitter", "Texture_Bouton");
        boutonQuitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
        panelMenuPrincipal.add(boutonQuitter);
    } 

    public void creerSelectionJoueurs() throws IOException{

        int borderTop = hauteurFenetre / 2;
        int borderBottom = hauteurFenetre / 3;
        int borderSides = largeurFenetre / 3;

        panelSelectionJoueurs = new MenuGraphique(InfoJeu.SELECTION_JOUEURS);
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

        DesignBoutons valider = new DesignBoutons("Valider", "Texture_Bouton");
        valider.addActionListener(new AdaptateurCommande(collecteurEvenements, "Valider"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(valider, gbc);
        
        gbc.gridx = 1;
        DesignBoutons annuler = new DesignBoutons("Annuler", "Texture_Bouton");
        annuler.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        valider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(annuler, gbc);


        
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
    public void creerOptions() throws IOException{

        int borderTop = hauteurFenetre / 4;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 3;

        panelOptions = new MenuGraphique(InfoJeu.OPTIONS_MENU);
        panelOptions.setLayout(new GridLayout(0, 1, 0, 30));
        panelOptions.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));

        JLabel txtMusique = new JLabel("Volume musique");
        JSlider musique = new JSlider(0, 100, 100);
        musique.setMajorTickSpacing(25);
        musique.setMinorTickSpacing(5);
        musique.setOpaque(false);
        musique.setPaintLabels(true);
        musique.setMaximumSize(new Dimension(200, 30));
    
        
        Container musiqueBox = Box.createHorizontalBox();
        musiqueBox.add(musique);
        //musiqueBox.add(Box.createHorizontalGlue());
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

        boutonCredits = new DesignBoutons("Crédits", "Texture_Bouton");
        boutonCredits.addActionListener(new AdaptateurCommande(collecteurEvenements, "Credits"));
        panelOptions.add(boutonCredits);

        boutonConfirmer = new DesignBoutons("Confirmer les Options", "Texture_Bouton");
        boutonConfirmer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Confirmation"));
        panelOptions.add(boutonConfirmer);

        boutonRetourAccueil = new DesignBoutons("Retour à l'accueil", "Texture_Bouton");
        boutonRetourAccueil.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        panelOptions.add(boutonRetourAccueil);
    }
    
        //Crée le JPanel du plateau de jeu
	//TODO Faire l'affichage du plateau de jeu
	public void creerPlateauJeu() throws IOException{

        plateauGraphique = new PlateauGraphique(jeu);        
        plateauGraphique.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weightx = 0.5;
        gbc.weighty = 0.33;
        // Top/right
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        boutonOptionsJeu = new DesignBoutons("Options", "Texture_Petit_Bouton");
        boutonOptionsJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "OptionsJeu"));
        plateauGraphique.add(boutonOptionsJeu, gbc);

        // bottom/left
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        boutonAnnulerJeu = new DesignBoutons("Annuler", "Texture_Petit_Bouton");
        boutonAnnulerJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "AnnulerTour"));
        plateauGraphique.add(boutonAnnulerJeu, gbc);


        // bottom/right
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        boutonHistorique = new DesignBoutons("Historique", "Texture_Petit_Bouton");
        //boutonHistorique.addActionListener(new AdaptateurCommande(collecteurEvenements, "Historique"));
        plateauGraphique.add(boutonHistorique, gbc);
                
        // middle/right
        gbc.gridx = 1;
        gbc.gridy = 1;
        //gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(180, 0, 0, 0);
        boutonFinDeTour = new DesignBoutons("Fin de tour", "Texture_Petit_Bouton");
        boutonFinDeTour.addActionListener(new AdaptateurCommande(collecteurEvenements, "FinDeTour"));
        plateauGraphique.add(boutonFinDeTour, gbc);


    } 


    public void creerOptionsJeu() throws IOException{

        int borderTop = hauteurFenetre / 4;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 3;

        panelOptionsJeu = new MenuGraphique(InfoJeu.OPTIONS_JEU);
        panelOptionsJeu.setLayout(new GridLayout(0, 1, 0, 30));
        panelOptionsJeu.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));

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
    
        panelOptionsJeu.add(musiqueBox);
    
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
    
        panelOptionsJeu.add(sonBox);

        boutonRetourArriere = new DesignBoutons("Retour arrière", "Texture_Bouton");
        boutonRetourArriere.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourArriere"));
        panelOptionsJeu.add(boutonRetourArriere);

        boutonAide = new DesignBoutons("Aide", "Texture_Bouton");
        boutonAide.addActionListener(new AdaptateurCommande(collecteurEvenements, "Aide"));
        panelOptionsJeu.add(boutonAide);

        boutonRecommencer = new DesignBoutons("Recommencer", "Texture_Bouton");
        boutonRecommencer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Recommencer"));
        panelOptionsJeu.add(boutonRecommencer);

        boutonSauvegarderEtQuitter = new DesignBoutons("Sauvegarder et quitter", "Texture_Bouton");
        boutonSauvegarderEtQuitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "SauvegarderQuitter"));
        panelOptionsJeu.add(boutonSauvegarderEtQuitter);

        boutonRetourJeu = new DesignBoutons("Retour au jeu", "Texture_Bouton");
        boutonRetourJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourJeu"));
        panelOptionsJeu.add(boutonRetourJeu);
    }

    @Override
    public void miseAJour() {
        fenetre.repaint();
        hauteurFenetre = fenetre.getHeight();
        largeurFenetre = fenetre.getWidth();
    }

    @Override
    public void afficherPanneau(String nomPanneau) {
        layout.show(panelCourant, nomPanneau);
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