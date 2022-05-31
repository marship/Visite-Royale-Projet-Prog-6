package Vue;

import Modele.Jeu;
import Pattern.Observateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import javax.swing.border.EmptyBorder;

import Adaptateur.AdaptateurBoutonGlissant;
import Adaptateur.AdaptateurClavier;
import Adaptateur.AdaptateurCommande;
import Adaptateur.AdaptateurSouris;
import Adaptateur.AdaptateurSourisMouvement;
import Adaptateur.AdaptateurTemps;
import Audio.Son;
import Global.Configuration;
import Global.InfoJeu;

public class InterfaceGraphique extends JPanel implements Runnable, InterfaceUtilisateur, Observateur {

    Jeu jeu;
    Son musique;
    String musiqueAudio = "the-weeknd-medieval";
    // String musiqueAudio = "gangstas-paradise-medieval";
    CollecteurEvenements collecteurEvenements;

    final static int VALEUR_MINIMALE_BOUTON_GLISSANT = -24;
    final static int VALEUR_MAXIMALE_BOUTON_GLISSANT = 6;

    PlateauGraphique plateauGraphique;
    JSlider boutonGlissantMusique;
    JDialog victoire;
    DesignBoutons boutonJouer, boutonCharger, boutonRegles, boutonOptions, boutonQuitter, boutonCredits,
            boutonConfirmer, boutonRetourAccueil, boutonTelechargement, boutonValiderSelection, boutonAnnulerSelection, boutonMainAdverse,
            boutonHistoriqueArriere, boutonHistoriqueAvant, boutonOptionsJeu, boutonFinDeTour,
            boutonRetourMenu, boutonAide, boutonRecommencer, boutonSauvegarderEtQuitter, boutonRetourJeu;

    CardLayout layout;
    JPanel panelCourant, panelMenuPrincipal, panelOptions, panelRegles, panelCredits, panelSelectionJoueurs, panelPlateau, panelOptionsJeu;

    JComboBox<String> comboBoxJoueurGaucheInit, comboBoxJoueurDroiteInit;
    JComboBox<String> comboBoxJoueurGaucheChang, comboBoxJoueurDroiteChang;
    JTextField valeurNomJoueurGaucheInit, valeurNomJoueurDroiteInit;
    JTextField valeurNomJoueurGaucheChang, valeurNomJoueurDroiteChang;
    static JRadioButton prioJoueurGauche;
    static JRadioButton prioJoueurDroite;
    static JRadioButton joueurAleatoire;

    public static JFrame fenetre;

    private int hauteurFenetre;
    private int largeurFenetre;

    InterfaceGraphique(Jeu j, CollecteurEvenements cEvenements) {
        jeu = j;
        collecteurEvenements = cEvenements;
        musique = new Son(musiqueAudio);
    }

    public static void demarrer(Jeu jeu, CollecteurEvenements cEvenements) {
        InterfaceGraphique vue = new InterfaceGraphique(jeu, cEvenements);
        cEvenements.ajouteInterfaceUtilisateur(vue);
        SwingUtilities.invokeLater(vue);
        // Garde à jour l'interface graphique du controleur
    }

    // TODO Rajouter dans panel courant: panelOption, panelRegles
    @Override
    public void run() {

        fenetre = new JFrame("Visite Royale");
        fenetre.setSize(1280, 720);
        hauteurFenetre = fenetre.getHeight();
        largeurFenetre = fenetre.getWidth();

        layout = new CardLayout();
        panelCourant = new JPanel(layout);

        fenetre.add(panelCourant);

        Timer chrono = new Timer(16, new AdaptateurTemps(collecteurEvenements));
        chrono.start();

        try {
            creerMenuPrincipal();
            panelCourant.add(panelMenuPrincipal, "MenuPrincipal");
            creerOptions();
            panelCourant.add(panelOptions, "Options");
            creerRegles();
            panelCourant.add(panelRegles, "ReglesJeu");
            creerCredits();
            panelCourant.add(panelCredits, "Credits");
            creerPlateauJeu();
            panelCourant.add(plateauGraphique, "Plateau");
            creerChangementJoueurs();
            panelCourant.add(panelSelectionJoueurs, "ChangementJoueur");
            creerSelectionJoueurs();
            panelCourant.add(panelSelectionJoueurs, "SelectionJoueur");
            creerOptionsJeu();
            panelCourant.add(panelOptionsJeu, "OptionsJeu");

        } catch (IOException e) {
            Configuration.instance().logger().severe("Erreur d'affichage des menus ou de l'ecran de victoire !!!");
            e.printStackTrace();
        }

        // Garde à jour l'interface graphique du controleur
        collecteurEvenements.ajouteInterfaceUtilisateur(this);

        // Mise en place des Listeners
        ((Component) plateauGraphique)
                .addMouseMotionListener(new AdaptateurSourisMouvement(plateauGraphique, collecteurEvenements));
        ((Component) plateauGraphique).addMouseListener(new AdaptateurSouris(plateauGraphique, collecteurEvenements));
        fenetre.addKeyListener(new AdaptateurClavier(collecteurEvenements));

        fenetre.setFocusable(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);
    }


    // Crée le JPanel du menu principal
    public void creerMenuPrincipal() throws IOException {

        int borderTop = hauteurFenetre / 6;
        int borderBottom = hauteurFenetre / 8;
        int borderSides = largeurFenetre / 3;

        panelMenuPrincipal = new MenuGraphique(InfoJeu.MENU_PRINCIPAL);
        panelMenuPrincipal.setLayout(new GridBagLayout());
        panelMenuPrincipal.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);

        gbc.weighty = 0.2;
        boutonJouer = new DesignBoutons("Jouer", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonJouer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Jouer"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelMenuPrincipal.add(boutonJouer, gbc);

        boutonCharger = new DesignBoutons("Charger une partie", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonCharger.addActionListener(new AdaptateurCommande(collecteurEvenements, "Charger"));
        gbc.gridy++;
        panelMenuPrincipal.add(boutonCharger, gbc);

        boutonRegles = new DesignBoutons("Règles du jeu", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonRegles.addActionListener(new AdaptateurCommande(collecteurEvenements, "Regles"));
        gbc.gridy++;
        panelMenuPrincipal.add(boutonRegles, gbc);

        boutonOptions = new DesignBoutons("Options", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonOptions.addActionListener(new AdaptateurCommande(collecteurEvenements, "Options"));
        gbc.gridy++;
        panelMenuPrincipal.add(boutonOptions, gbc);

        boutonQuitter = new DesignBoutons("Quitter", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonQuitter.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
        gbc.gridy++;
        panelMenuPrincipal.add(boutonQuitter, gbc);
    }

    public void creerSelectionJoueurs() throws IOException{

        int borderTop = hauteurFenetre / 3;
        int borderBottom = hauteurFenetre / 3;
        int borderSides = largeurFenetre / 3;

        panelSelectionJoueurs = new MenuGraphique(InfoJeu.SELECTION_JOUEURS);
        panelSelectionJoueurs.setLayout(new GridBagLayout());
        panelSelectionJoueurs.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        GridBagConstraints gbc = new GridBagConstraints();

        String[] choixComboBox = {
            "Humain",
            "IA très facile",
            "IA facile",
            "IA normale",
            "IA difficile",
            "IA experte",
            "IA triche"
        };

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(10,0,0,10);  //top padding

        JLabel nomJoueurGauche = new JLabel("Joueur Gauche");
        gbc.gridx = 0;
        gbc.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueurGauche, gbc);

        gbc.ipady = 10;
        valeurNomJoueurGaucheInit = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;  
        valeurNomJoueurGaucheInit.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 15));
        valeurNomJoueurGaucheInit.setText("Joueur 1");
        panelSelectionJoueurs.add(valeurNomJoueurGaucheInit, gbc);

        comboBoxJoueurGaucheInit = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueurGaucheInit.addItem(choixComboBox[i]);
        }
        comboBoxJoueurGaucheInit.setFocusable(false);
        comboBoxJoueurGaucheInit.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueurGaucheInit.getSelectedItem().toString()));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueurGaucheInit, gbc);

        gbc.gridwidth = 2;

        ButtonGroup G1 = new ButtonGroup();

        prioJoueurGauche = new JRadioButton("Joueur gauche commence", false);
        prioJoueurGauche.setContentAreaFilled(false);
        prioJoueurGauche.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        G1.add(prioJoueurGauche);
        panelSelectionJoueurs.add(prioJoueurGauche, gbc);

        prioJoueurDroite = new JRadioButton("Joueur droit commence", false);
        prioJoueurDroite.setContentAreaFilled(false);
        prioJoueurDroite.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        G1.add(prioJoueurDroite);
        panelSelectionJoueurs.add(prioJoueurDroite, gbc);

        joueurAleatoire = new JRadioButton("Choix aléatoire", true);
        joueurAleatoire.setContentAreaFilled(false);
        joueurAleatoire.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        G1.add(joueurAleatoire);
        panelSelectionJoueurs.add(joueurAleatoire, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,30,0,0);  //padding elements Joueur2

        gbc.ipady = 0;
        JLabel nomJoueurDroite = new JLabel("Joueur Droite");
        gbc.gridx = 1;
        gbc.gridy = 0; 
        panelSelectionJoueurs.add(nomJoueurDroite, gbc);

        gbc.ipady = 10;
        valeurNomJoueurDroiteInit = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;  
        valeurNomJoueurDroiteInit.setText("Joueur 2");
        valeurNomJoueurDroiteInit.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 15));
        panelSelectionJoueurs.add(valeurNomJoueurDroiteInit, gbc);


        comboBoxJoueurDroiteInit = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueurDroiteInit.addItem(choixComboBox[i]);
        }
        comboBoxJoueurDroiteInit.setFocusable(false);
        comboBoxJoueurDroiteInit.setSelectedIndex(5);
        comboBoxJoueurDroiteInit.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueurDroiteInit.getSelectedItem().toString()));
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueurDroiteInit, gbc);

        gbc.insets = new Insets(30,0,0,0); 
        gbc.gridx = 0;
        gbc.gridy = 6; 

        boutonValiderSelection = new DesignBoutons("Annuler", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonValiderSelection.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourArriere"));
        boutonValiderSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(boutonValiderSelection, gbc);

        gbc.gridx = 1;
        boutonAnnulerSelection = new DesignBoutons("Valider", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonAnnulerSelection.addActionListener(new AdaptateurCommande(collecteurEvenements, "Valider"));
        boutonAnnulerSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(boutonAnnulerSelection, gbc);

        
    }

    public void creerChangementJoueurs() throws IOException{

        int borderTop = hauteurFenetre / 3;
        int borderBottom = hauteurFenetre / 3;
        int borderSides = largeurFenetre / 3;

        panelSelectionJoueurs = new MenuGraphique(InfoJeu.SELECTION_JOUEURS);
        panelSelectionJoueurs.setLayout(new GridBagLayout());
        panelSelectionJoueurs.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        GridBagConstraints gbc = new GridBagConstraints();

        String[] choixComboBox = {
            "Humain",
            "IA très facile",
            "IA facile",
            "IA normale",
            "IA difficile",
            "IA experte",
            "IA triche"
        };

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.33;
        gbc.insets = new Insets(10,0,0,10);  //top padding

        JLabel nomJoueurGauche = new JLabel("Joueur Gauche");
        gbc.gridx = 0;
        gbc.gridy = 0;  
        panelSelectionJoueurs.add(nomJoueurGauche, gbc);

        gbc.ipady = 10;
        valeurNomJoueurGaucheChang = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;  
        valeurNomJoueurGaucheChang.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 15));
        valeurNomJoueurGaucheChang.setText("Joueur 1");
        panelSelectionJoueurs.add(valeurNomJoueurGaucheChang, gbc);

        comboBoxJoueurGaucheChang = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueurGaucheChang.addItem(choixComboBox[i]);
        }
        comboBoxJoueurGaucheChang.setFocusable(false);
        comboBoxJoueurGaucheChang.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueurGaucheChang.getSelectedItem().toString()));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueurGaucheChang, gbc);

        gbc.gridwidth = 2;

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10,30,0,0);  //padding elements Joueur2

        gbc.ipady = 0;
        JLabel nomJoueurDroite = new JLabel("Joueur Droite");
        gbc.gridx = 1;
        gbc.gridy = 0; 
        panelSelectionJoueurs.add(nomJoueurDroite, gbc);

        gbc.ipady = 10;
        valeurNomJoueurDroiteChang = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;  
        valeurNomJoueurDroiteChang.setText("Joueur 2");
        valeurNomJoueurDroiteChang.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 15));
        panelSelectionJoueurs.add(valeurNomJoueurDroiteChang, gbc);


        comboBoxJoueurDroiteChang = new JComboBox<>();
        for(int i = 0; i < choixComboBox.length; i++){
            comboBoxJoueurDroiteChang.addItem(choixComboBox[i]);
        }
        comboBoxJoueurDroiteChang.setFocusable(false);
        comboBoxJoueurDroiteChang.setSelectedIndex(5);
        comboBoxJoueurDroiteChang.addActionListener(new AdaptateurCommande(collecteurEvenements, comboBoxJoueurDroiteChang.getSelectedItem().toString()));
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelSelectionJoueurs.add(comboBoxJoueurDroiteChang, gbc);

        gbc.insets = new Insets(30,0,0,0); 
        gbc.gridx = 0;
        gbc.gridy = 6; 

        boutonValiderSelection = new DesignBoutons("Annuler", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonValiderSelection.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourArriere"));
        boutonValiderSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(boutonValiderSelection, gbc);

        gbc.gridx = 1;
        boutonAnnulerSelection = new DesignBoutons("Valider", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonAnnulerSelection.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourJeu"));
        boutonAnnulerSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSelectionJoueurs.add(boutonAnnulerSelection, gbc);
    }
    
    @Override
    public String getInfoJoueurInit(int coteJoueur) {
        String joueur;
        if (coteJoueur == 1) {
            joueur = comboBoxJoueurDroiteInit.getSelectedItem().toString();
            return joueur.replaceAll(" ", "");
        } else {
            joueur = comboBoxJoueurGaucheInit.getSelectedItem().toString();
            return joueur.replaceAll(" ", "");
        }
    }

    @Override
    public String getInfoJoueurChang(int coteJoueur) {
        String joueur;
        if (coteJoueur == 1) {
            joueur = comboBoxJoueurDroiteChang.getSelectedItem().toString();
            return joueur.replaceAll(" ", "");
        } else {
            joueur = comboBoxJoueurGaucheChang.getSelectedItem().toString();
            return joueur.replaceAll(" ", "");
        }
    }

    @Override
    public String getNomJoueurInit(int coteJoueur) {
        if (coteJoueur == 1) {
            return valeurNomJoueurDroiteInit.getText();
        } else {
            return valeurNomJoueurGaucheInit.getText();
        }
    }

    @Override
    public String getNomJoueurChang(int coteJoueur) {
        if (coteJoueur == 1) {
            return valeurNomJoueurDroiteChang.getText();
        } else {
            return valeurNomJoueurGaucheChang.getText();
        }
    }

    public static int getJoueurPrioritaire(){
        if(prioJoueurGauche.isSelected()){
            return 0;
        } else if(prioJoueurDroite.isSelected()){
            return 1;
        } else if(joueurAleatoire.isSelected()){
            return 2;
        } else {
            return -1;
        }
    }

    public void miseAJourFinDeTour() {
        fenetre.repaint();
    }

    // Crée le JPanel des regles
    // TODO Faire l'affichage des regles
    public void creerRegles() throws IOException {

        panelRegles = new MenuGraphique(InfoJeu.REGLES_JEU);
        panelRegles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(40,40,40,0); 
        boutonRetourMenu = new DesignBoutons("Retour", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonRetourMenu.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        panelRegles.add(boutonRetourMenu, gbc);


        gbc.gridy++;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        boutonTelechargement = new DesignBoutons("Télécharger les règles", "Texture_Bouton", "Texture_Bouton_Clique", 20);
        boutonTelechargement.addActionListener(new AdaptateurCommande(collecteurEvenements, "TelechargerRegles"));
        panelRegles.add(boutonTelechargement, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        DesignBoutons remplissage = new DesignBoutons("remplissage", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        remplissage.setVisible(false);
        panelRegles.add(remplissage, gbc);
    }

    public void creerCredits() throws IOException {

        panelCredits = new MenuGraphique(InfoJeu.CREDITS);
        panelCredits.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(40,40,40,0); 
        boutonRetourMenu = new DesignBoutons("Retour", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        boutonRetourMenu.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
        panelCredits.add(boutonRetourMenu, gbc);


        gbc.gridy++;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        DesignBoutons remplissage = new DesignBoutons("remplissage", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        remplissage.setVisible(false);
        panelRegles.add(remplissage, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        DesignBoutons remplissage2 = new DesignBoutons("remplissage", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 15);
        remplissage2.setVisible(false);
        panelRegles.add(remplissage2, gbc);
    }

    // Crée le JPanel des options
    public void creerOptions() throws IOException {

        int borderTop = hauteurFenetre / 5;
        int borderBottom = hauteurFenetre / 8;
        int borderSides = largeurFenetre / 4;

        panelOptions = new MenuGraphique(InfoJeu.OPTIONS_MENU);
        panelOptions.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));
        panelOptions.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weighty = 0.25;
        gbc.insets = new Insets(50, 0, 0, 0);

        boutonGlissantMusique = new JSlider(-24, 6, -16);
        boutonGlissantMusique.addChangeListener(new AdaptateurBoutonGlissant(musique, boutonGlissantMusique));
        boutonGlissantMusique.setOpaque(false);
        boutonGlissantMusique.setPaintLabels(true);

        JLabel txtMusique = new JLabel("Volume musique");

        Container musiqueBox = Box.createHorizontalBox();
        musiqueBox.add(boutonGlissantMusique);
        musiqueBox.add(Box.createRigidArea(new Dimension(20, 20)));
        musiqueBox.add(txtMusique);
        gbc.gridx = 0;
        gbc.gridy++;

        panelOptions.add(musiqueBox, gbc);

        boutonCredits = new DesignBoutons("Crédits", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonCredits.addActionListener(new AdaptateurCommande(collecteurEvenements, "Credits"));
        gbc.gridy++;
        panelOptions.add(boutonCredits, gbc);

        gbc.insets = new Insets(20, 0, 0, 0);
        boutonRetourAccueil = new DesignBoutons("Retour à l'accueil", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonRetourAccueil.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourArriere"));
        gbc.gridy++;
        panelOptions.add(boutonRetourAccueil, gbc);
    }

    // Crée le JPanel du plateau de jeu
    // TODO Faire l'affichage du plateau de jeu
    public void creerPlateauJeu() throws IOException {

        plateauGraphique = new PlateauGraphique(jeu, collecteurEvenements);
        plateauGraphique.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 0.5;
        gbc.weighty = 0.33;
        // Top/right
        // gbc.gridx = 0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        boutonOptionsJeu = new DesignBoutons("Menu", "Texture_Petit_Bouton", "Texture_Petit_Bouton_Clique", 15);
        boutonOptionsJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "OptionsJeu"));
        plateauGraphique.add(boutonOptionsJeu, gbc);

        // middle/right
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(300, 0, 2, 0);
        boutonFinDeTour = new DesignBoutons("Fin de tour", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 20);
        boutonFinDeTour.addActionListener(new AdaptateurCommande(collecteurEvenements, "FinDeTour"));
        plateauGraphique.add(boutonFinDeTour, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel historique = new JLabel("Historique");
        historique.setForeground(Color.WHITE);
        historique.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        historique.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        boutonHistoriqueArriere = new DesignBoutons("←", "Texture_Petit_Bouton_Moitie",
                "Texture_Petit_Bouton_Moitie_Clique", 30);
        boutonHistoriqueArriere.addActionListener(new AdaptateurCommande(collecteurEvenements, "Annuler"));
        boutonHistoriqueAvant = new DesignBoutons("→", "Texture_Petit_Bouton_Moitie",
                "Texture_Petit_Bouton_Moitie_Clique", 30);
        boutonHistoriqueAvant.addActionListener(new AdaptateurCommande(collecteurEvenements, "Refaire"));
        Container historiqueAvantArriere = Box.createHorizontalBox();
        historiqueAvantArriere.add(boutonHistoriqueArriere);
        historiqueAvantArriere.add(boutonHistoriqueAvant);

        Container historiqueBox = Box.createVerticalBox();
        historiqueBox.add(historique);
        historiqueBox.add(historiqueAvantArriere);
        plateauGraphique.add(historiqueBox, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        boutonMainAdverse = new DesignBoutons("Révéler main", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 16);
        boutonMainAdverse.addActionListener(new AdaptateurCommande(collecteurEvenements, "Visible"));
        plateauGraphique.add(boutonMainAdverse, gbc);

        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(0, 220, 0, 0);
        gbc.gridy = 2;
        DesignBoutons boutonAideJeu = new DesignBoutons("Aide", "Texture_Petit_Bouton_Moitie",
                "Texture_Petit_Bouton_Moitie_Clique", 15);
        boutonAideJeu.addActionListener(new AdaptateurCommande(collecteurEvenements, "AideIA"));
        plateauGraphique.add(boutonAideJeu, gbc);

    }

    public void creerOptionsJeu() throws IOException {

        int borderTop = hauteurFenetre / 4;
        int borderBottom = hauteurFenetre / 10;
        int borderSides = largeurFenetre / 3;

        panelOptionsJeu = new MenuGraphique(InfoJeu.OPTIONS_JEU);
        panelOptionsJeu.setLayout(new GridLayout(0, 1, 0, 30));
        panelOptionsJeu.setBorder(new EmptyBorder(borderTop, borderSides, borderBottom, borderSides));

        JLabel txtMusique = new JLabel("Volume musique");
        boutonGlissantMusique = new JSlider(-24, 6, -16);
        boutonGlissantMusique.addChangeListener(new AdaptateurBoutonGlissant(musique, boutonGlissantMusique));
        boutonGlissantMusique.setOpaque(false);
        boutonGlissantMusique.setPaintLabels(true);

        Container musiqueBox = Box.createHorizontalBox();
        musiqueBox.add(boutonGlissantMusique);
        musiqueBox.add(Box.createHorizontalGlue());
        musiqueBox.add(txtMusique);

        panelOptionsJeu.add(musiqueBox);

        boutonAide = new DesignBoutons("Changer Joueurs", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonAide.addActionListener(new AdaptateurCommande(collecteurEvenements, "ChangeJou"));
        panelOptionsJeu.add(boutonAide);

        boutonRecommencer = new DesignBoutons("Recommencer", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonRecommencer.addActionListener(new AdaptateurCommande(collecteurEvenements, "Recommencer"));
        panelOptionsJeu.add(boutonRecommencer);

        boutonSauvegarderEtQuitter = new DesignBoutons("Sauvegarder", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonSauvegarderEtQuitter
                .addActionListener(new AdaptateurCommande(collecteurEvenements, "SauvegarderQuitter"));
        panelOptionsJeu.add(boutonSauvegarderEtQuitter);

        boutonRetourMenu = new DesignBoutons("Retour au menu ", "Texture_Bouton", "Texture_Bouton_Clique", 25);
        boutonRetourMenu.addActionListener(new AdaptateurCommande(collecteurEvenements, "RetourArriere"));
        panelOptionsJeu.add(boutonRetourMenu);

        boutonRetourJeu = new DesignBoutons("Retour au jeu", "Texture_Bouton", "Texture_Bouton_Clique", 25);
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

    @Override
    public JFrame fenetre() {
        return fenetre;
    }

    @Override
    public void augmenterVolume() {
        musique.augmenterVolume();
    }

    @Override
    public void diminuerVolume() {
        musique.diminuerVolume();
    }

    @Override
    public void muterVolume() {
        musique.muterVolume(boutonGlissantMusique);
    }
}