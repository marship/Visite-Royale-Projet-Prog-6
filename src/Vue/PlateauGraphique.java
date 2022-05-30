package Vue;

import Modele.Jeu;
import Modele.Plateau;
import Modele.Carte;
import Pattern.Observateur;
import Structures.Sequence;

import javax.swing.*;

import Adaptateur.AdaptateurCommande;
import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Global.InfoJeu;
import Global.InfoPlateau;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PlateauGraphique extends JPanel implements Observateur {

    static final int EPAISSEUR_BORDURE = 3;

    Jeu jeu;
    Plateau plateau;
    Graphics2D dessinable;
    public static JDialog victoire;
    CollecteurEvenements collecteurEvenements;
    boolean affichageEcranVictoire;

    // ==================================
    // ===== BOUTONS ECRAN VICTOIRE =====
    // ==================================
    DesignBoutons boutonRecommencerVictoire, boutonRetourMenuVictoire, boutonQuitterVictoire;

    // ==========================
    // ===== IMAGES PLATEAU =====
    // ==========================
    ImagePlateau imagePlateau, imagePlateauGauche, imagePlateauDroit, imageBandeauTour, imageCadreCartesPosees,
            imageCadrePiocheDefausse;

    // =========================
    // ===== IMAGES JETONS =====
    // =========================
    ImagePlateau imageJetonGrandeCouronne, imageJetonPetiteCouronne;
    ImagePlateau imageJetonGardeGauche, imageJetonGardeDroit, imageJetonRoi, imageJetonFou, imageJetonSorcier;
    ImagePlateau imageJetonGardeGaucheTransparent, imageJetonGardeDroitTransparent, imageJetonRoiTransparent,
            imageJetonFouTransparent, imageJetonSorcierTransparent;
    ImagePlateau imageJetonGardeGaucheSelection, imageJetonGardeDroitSelection, imageJetonRoiSelection,
            imageJetonFouSelection, imageJetonSorcierSelection;
    ImagePlateau imageJetonFouPouvoir, imageJetonSorcierPouvoir, imageJetonRoiPouvoir;
    ImagePlateau imageJetonGardeGaucheGrise, imageJetonGardeDroitGrise, imageJetonRoiGrise, imageJetonFouGrise,
            imageJetonSorcierGrise;
    ImagePlateau imageJetonGardeGaucheGriseTransparent, imageJetonGardeDroitGriseTransparent,
            imageJetonRoiGriseTransparent, imageJetonFouGriseTransparent, imageJetonSorcierGriseTransparent;

    // =========================
    // ===== IMAGES CARTES =====
    // =========================
    ImagePlateau imageCarteErreur, imageCarteVide, imageDosCarte;
    ImagePlateau imageCarteRoi;
    ImagePlateau imageCarteFouUn, imageCarteFouDeux, imageCarteFouTrois, imageCarteFouQuatre, imageCarteFouCinq,
            imageCarteFouM;
    ImagePlateau imageCarteSorcierUn, imageCarteSorcierDeux, imageCarteSorcierTrois;
    ImagePlateau imageCarteGardesUn, imageCarteGardesUnPlusUn, imageCarteGardesRaproche;

    // =========================
    // = IMAGES CARTES GRISES ==
    // =========================
    ImagePlateau imageCarteRoiGrise;
    ImagePlateau imageTorcheAllumee, imageTorcheEteint;
    ImagePlateau imageCarteFouUnGrise, imageCarteFouDeuxGrise, imageCarteFouTroisGrise, imageCarteFouQuatreGrise,
            imageCarteFouCinqGrise, imageCarteFouMGrise;
    ImagePlateau imageCarteSorcierUnGrise, imageCarteSorcierDeuxGrise, imageCarteSorcierTroisGrise;
    ImagePlateau imageCarteGardesUnGrise, imageCarteGardesUnPlusUnGrise, imageCarteGardesRaprocheGrise;

    // =========================
    // ===== IMAGES BOUTONS ====
    // =========================
    ImagePlateau imageBoutonAnnuler, imageBoutonAnnulerGrise;

    // =============================================
    // ===== INFO POSITIONS ELEMENTS GRAPHIQUE =====
    // =============================================
    int taillePlateau = 0;
    int largeurFenetre, hauteurFenetre = 0;
    int largeurCasePlateau, hauteurCasePlateau = 0;
    int debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau, quartHauteurPlateau = 0;
    int debutCartesX, debutCartesY, largeurCarte, hauteurCarte = 0;

    /////////////////////////////////////////////////////////////////////////

    // ========================
    // ===== CONSTRUCTEUR =====
    // ========================
    public PlateauGraphique(Jeu j, CollecteurEvenements cEvenements) {
        chargementDesImages();
        jeu = j;
        collecteurEvenements = cEvenements;
        jeu.ajouteObservateur(this);
        
        // plateau = jeu.plateau();
    }

    // ===========================
    // ===== PAINT COMPONENT =====
    // ===========================
    @Override
    public void paintComponent(Graphics g) {

        dessinable = (Graphics2D) g;

        largeurFenetre = getWidth();
        hauteurFenetre = getHeight();

        taillePlateau = jeu.obtenirInfoPlateau(InfoPlateau.TAILLE_DU_PLATEAU);

        debutPlateauY = 5 * hauteurFenetre / 28;
        largeurPlateau = largeurFenetre;
        hauteurPlateau = 3 * hauteurFenetre / 7;

        largeurCarte = largeurFenetre / 16;
        hauteurCarte = hauteurFenetre / 7;

        largeurCasePlateau = largeurPlateau / taillePlateau;
        hauteurCasePlateau = hauteurPlateau;
        quartHauteurPlateau = hauteurCasePlateau / 4;

        dessinable.clearRect(0, 0, largeurFenetre, hauteurFenetre);
        dessinable.setColor(new Color(23, 74, 11)); // TODO choisir couleur
        dessinable.fillRect(0, 0, largeurFenetre, hauteurFenetre);

        tracerPlateau();
        affichageEcranVictoire = false;

        
        if(!jeu.estPartieTerminee() && affichageEcranVictoire == false){

            afficherCartesAutreJoueur();
            afficherZoneCartesJouees();
            //afficherInfoTour();
            afficherBoutonAnnuler();
            afficherPioche();
            afficherDefausse();
            if(jeu.actionAutoriser()){
                afficherCartesJoueurCourant();
            }

        } else {
            

            afficherEcranVictoire();
            affichageEcranVictoire = true;

            InterfaceGraphique.fenetre.setEnabled(false);
        }

    }

    private void afficherEcranVictoire() {
        victoire = new JDialog(new JFrame("Victoire !"));
        victoire.setBounds(500, 500, 800, 200);
        victoire.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        victoire.setAlwaysOnTop(true);
        

        victoire.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        JLabel annonceVictoire = new JLabel(jeu.traiterGagnant());
        annonceVictoire.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        victoire.add(annonceVictoire, gbc);

        gbc.weighty = 0.33;
        gbc.anchor = GridBagConstraints.PAGE_END;

        gbc.gridy++;
        DesignBoutons boutonRecommencerVictoire;
        try {
            gbc.gridx = 0;
            boutonRecommencerVictoire = new DesignBoutons("Recommencer", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 12);
            boutonRecommencerVictoire.addActionListener(new AdaptateurCommande(collecteurEvenements, "Recommencer"));
            victoire.add(boutonRecommencerVictoire, gbc);

            gbc.gridx++;
            boutonRetourMenuVictoire = new DesignBoutons("Retour au menu", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 12);
            boutonRetourMenuVictoire.addActionListener(new AdaptateurCommande(collecteurEvenements, "MenuPrincipal"));
            victoire.add(boutonRetourMenuVictoire, gbc);
            
            gbc.gridx++;
            boutonQuitterVictoire = new DesignBoutons("Quitter le jeu", "Texture_Moyen_Bouton", "Texture_Moyen_Bouton_Clique", 12);
            boutonQuitterVictoire.addActionListener(new AdaptateurCommande(collecteurEvenements, "Quitter"));
            victoire.add(boutonQuitterVictoire, gbc);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        victoire.setVisible(true);
        jeu.nonFinPartie();
    }


    // =======================
    // ===== MISE A JOUR =====
    // =======================
    @Override
    public void miseAJour() {
        repaint();
    }

    // ===========================
    // ===== TRACER ELEMENTS =====
    // ===========================
    public void tracerPlateau() {

        tracerImage(imagePlateau, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);

        if (jeu.joueurCourant() == 1) {
            tracerTorches(imageTorcheEteint, imageTorcheAllumee);
        } else {
            tracerTorches(imageTorcheAllumee, imageTorcheEteint);
        }

        if (jeu.getEtatCouronne()) {
            tracerJeton(Element.COURONNE, imageJetonGrandeCouronne);
        } else {
            tracerJeton(Element.COURONNE, imageJetonPetiteCouronne);
        }

        switch (jeu.getEtatJeu()) {
            case DEBUT_TOUR:
                tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGauche);
                tracerJeton(Element.ROI, imageJetonRoi);
                tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroit);
                tracerJeton(Element.FOU, imageJetonFou);
                tracerJeton(Element.SORCIER, imageJetonSorcier);
                if (jeu.carteActuelle() != 8) {
                    switch (jeu.recupererMainJoueur(jeu.joueurCourant())[jeu.carteActuelle()].personnage()) {
                        case ROI:
                            tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerJeton(Element.FOU, imageJetonFouGrise);
                            tracerJeton(Element.SORCIER, imageJetonSorcierGrise);
                            break;

                        case GARDES:
                            tracerJeton(Element.ROI, imageJetonRoiGrise);
                            tracerJeton(Element.FOU, imageJetonFouGrise);
                            tracerJeton(Element.SORCIER, imageJetonSorcierGrise);
                            break;

                        case FOU:
                            tracerJeton(Element.ROI, imageJetonRoiGrise);
                            tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerJeton(Element.SORCIER, imageJetonSorcierGrise);
                            tracerJeton(Element.FOU, imageJetonFouGrise);
                            switch (jeu.personnageManipulerParLeFou) {
                                case FOU:
                                    tracerJeton(Element.FOU, imageJetonFou);
                                    break;
                                case GARDES:
                                    tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGauche);
                                    tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroit);
                                    break;
                                case SORCIER:
                                    tracerJeton(Element.SORCIER, imageJetonSorcier);
                                    break;
                                case ROI:
                                    tracerJeton(Element.ROI, imageJetonRoi);
                                    break;
                            
                                default:
                                    break;
                            }
                            break;

                        case SORCIER:
                            tracerJeton(Element.ROI, imageJetonRoiGrise);
                            tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerJeton(Element.FOU, imageJetonFouGrise);
                            break;

                        default:
                            break;
                    }
                }
                break;

            case CHOIX_FOU:
                tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheSelection);
                tracerJeton(Element.ROI, imageJetonRoiSelection);
                tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitSelection);
                tracerJeton(Element.FOU, imageJetonFouPouvoir);
                tracerJeton(Element.SORCIER, imageJetonSorcierSelection);
                break;

            case CHOIX_SORCIER:
                if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
                    tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheSelection);
                } else {
                    tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                }

                if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
                    tracerJeton(Element.ROI, imageJetonRoiSelection);
                } else {
                    tracerJeton(Element.ROI, imageJetonRoiGrise);
                }

                if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
                    tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitSelection);
                } else {
                    tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                }
                tracerJeton(Element.FOU, imageJetonFouGrise);
                tracerJeton(Element.SORCIER, imageJetonSorcierPouvoir);
                break;

            case CHOIX_ROI:
                tracerJeton(Element.ROI, imageJetonRoiPouvoir);
                tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                tracerJeton(Element.FOU, imageJetonFouGrise);
                tracerJeton(Element.SORCIER, imageJetonSorcierGrise);
                int d = jeu.positionsPourCour();
                if (d == 1 || d == 0) {
                    dessinable.setColor(new Color(255, 255, 0));
                    dessinable.fillOval(
                            positionJeton(Element.ROI, true) - largeurCasePlateau
                                    + largeurCasePlateau / 3,
                            quartHauteurPlateau * 4,
                            30, 30);
                }
                if (d == 2 || d == 0) {
                    dessinable.setColor(new Color(255, 255, 0));
                    dessinable.fillOval(
                            positionJeton(Element.ROI, true) + largeurCasePlateau
                                    + largeurCasePlateau / 3,
                            quartHauteurPlateau * 4,
                            30, 30);
                }

            default:
                break;
        }

        tracerChoixPreView();
        tracerChoix();

    }

    private void tracerTorches(ImagePlateau imageTorcheGauche, ImagePlateau imageTorcheDroite) {
        tracerImage(imageTorcheGauche, largeurCasePlateau(), 2 * hauteurFenetre / 40, largeurCasePlateau(), hauteurCasePlateau()/3);
        tracerLabel(jeu.nomJoueurGauche(), largeurCasePlateau(), 5 * hauteurFenetre / 30);
        tracerImage(imageTorcheDroite, 15*largeurCasePlateau(), 2 * hauteurFenetre / 40, largeurCasePlateau(), hauteurCasePlateau()/3);
        tracerLabel(jeu.nomJoueurDroite(), 15*largeurCasePlateau(), 5 * hauteurFenetre / 30);
    }

    void tracerChoix() {
        if (jeu.actionAutoriser()) {
            if (jeu.carteActuelle() == 8) {
                return;
            }
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[jeu.carteActuelle];
            int[] listeDeplacementPossiblesAvecCarte = jeu.listeDeplacementPossiblesAvecCarte(carte.personnage(),
                    carte.deplacement());
            int i = 0;
            while (i < 17) {
                if (listeDeplacementPossiblesAvecCarte[i] == 1) {
                    ImagePlateau image = imageJetonFouGrise;
                    ImagePlateau pass = imageJetonFouGrise;
                    ImagePlateau ancien = imageJetonFouGrise;
                    switch (carte.personnage()) {
                        case FOU:
                            switch (jeu.personnageManipulerParLeFou) {
                                case FOU:
                                    image = imageJetonFouTransparent;
                                    pass = imageJetonFou;
                                    ancien = imageJetonFouGriseTransparent;
                                    break;
                                case SORCIER:
                                    image = imageJetonSorcierTransparent;
                                    pass = imageJetonSorcier;
                                    ancien = imageJetonSorcierGriseTransparent;
                                    break;
                                case ROI:
                                    image = imageJetonRoiTransparent;
                                    pass = imageJetonRoi;
                                    ancien = imageJetonRoiGriseTransparent;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case SORCIER:
                            image = imageJetonSorcierTransparent;
                            pass = imageJetonSorcier;
                            ancien = imageJetonSorcierGriseTransparent;
                            break;
                        // TODO afficher le/les bons gardes à deplacer
                        case GARDES:
                            image = imageJetonGardeDroitTransparent;
                            pass = imageJetonGardeDroit;
                            ancien = imageJetonGardeDroitGriseTransparent;
                            break;
                        case ROI:
                            image = imageJetonRoiTransparent;
                            pass = imageJetonRoi;
                            ancien = imageJetonRoiGriseTransparent;
                            break;
                        default:
                            image = imageJetonFouGrise;
                            break;
                    }
                    if (carte.personnage() == Element.GARDES) {
                        if (jeu.casePassee() == i) {
                            tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGriseTransparent);
                            tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGriseTransparent);
                            tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGriseTransparent);
                            tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGriseTransparent);

                            if (carte.deplacement() == Deplacement.RAPPROCHE) {
                                tracerJeton(carte.personnage(), imageJetonGardeGauche,
                                        jeu.obtenirPositionElement(Element.ROI) - 1 + 8);
                                tracerJeton(carte.personnage(), imageJetonGardeDroit,
                                        jeu.obtenirPositionElement(Element.ROI) + 1 + 8);
                            }

                            if (carte.deplacement() == Deplacement.UN) {
                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 1 ||
                                        i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche, i);
                                } else {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit, i);
                                }
                            }

                            if (carte.deplacement() == Deplacement.UN_PLUS_UN) {

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 2) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche, i);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) + 2) {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit, i);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 2
                                        && jeu.obtenirPositionElement(Element.ROI) > i - 8) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche, i);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) - 2
                                        && jeu.obtenirPositionElement(Element.ROI) < i - 8) {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit, i);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche, i);
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit,
                                            jeu.obtenirPositionElement(Element.GARDE_DROIT) - 1 + 8);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) - 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit, i);
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche,
                                            jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 1 + 8);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche, i);
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit,
                                            jeu.obtenirPositionElement(Element.GARDE_DROIT) + 1 + 8);
                                }

                                if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) + 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroit, i);
                                    tracerJeton(carte.personnage(), imageJetonGardeGauche,
                                            jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 1 + 8);
                                }
                            }
                        } else {
                            if (carte.deplacement() == Deplacement.RAPPROCHE) {
                                if (i - 8 == jeu.obtenirPositionElement(Element.ROI) - 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeGaucheTransparent, i);
                                }
                                if (i - 8 == jeu.obtenirPositionElement(Element.ROI) + 1) {
                                    tracerJeton(carte.personnage(), imageJetonGardeDroitTransparent, i);
                                }
                            }
                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 2) {
                                tracerJeton(carte.personnage(), imageJetonGardeGaucheTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) + 2) {
                                tracerJeton(carte.personnage(), imageJetonGardeDroitTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 2
                                    && jeu.obtenirPositionElement(Element.ROI) > i - 8) {
                                tracerJeton(carte.personnage(), imageJetonGardeGaucheTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) - 2
                                    && jeu.obtenirPositionElement(Element.ROI) < i - 8) {
                                tracerJeton(carte.personnage(), imageJetonGardeDroitTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 1) {
                                tracerJeton(carte.personnage(), imageJetonGardeGaucheTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) - 1) {
                                tracerJeton(carte.personnage(), imageJetonGardeDroitTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 1) {
                                tracerJeton(carte.personnage(), imageJetonGardeGaucheTransparent, i);
                            }

                            if (i - 8 == jeu.obtenirPositionElement(Element.GARDE_DROIT) + 1) {
                                tracerJeton(carte.personnage(), imageJetonGardeDroitTransparent, i);
                            }
                        }
                    } else {
                        if(carte.personnage() == Element.FOU){
                            if(jeu.personnageManipulerParLeFou == Element.GARDES){
                                if (jeu.casePassee() == i) {
                                    tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGriseTransparent);
                                    tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGriseTransparent);
                                    tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheGriseTransparent);
                                    tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroitGriseTransparent);
                                    if(i < jeu.obtenirPositionElement(Element.ROI) + 8){
                                        tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGauche, jeu.casePassee());
                                    }
                                    else{
                                        tracerJeton(Element.GARDE_DROIT, imageJetonGardeDroit, jeu.casePassee());
                                    }
                                }
                                else{
                                    if(i < jeu.obtenirPositionElement(Element.ROI) + 8){
                                        tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeGaucheTransparent, i);
                                    }
                                    else{
                                        tracerJeton(Element.GARDE_GAUCHE, imageJetonGardeDroitTransparent, i);
                                    }
                                }
                            }
                            else{
                                if (jeu.casePassee() == i) {
                                    tracerJeton(jeu.personnageManipulerParLeFou, pass, jeu.casePassee());
                                    tracerJeton(jeu.personnageManipulerParLeFou, ancien);
                                    tracerJeton(jeu.personnageManipulerParLeFou, ancien);
                                } else {
                                    tracerJeton(jeu.personnageManipulerParLeFou, image, i);
                                }
                            }
                        }
                        else{
                            if (jeu.casePassee() == i) {
                                tracerJeton(carte.personnage(), pass, jeu.casePassee());
                                tracerJeton(carte.personnage(), ancien);
                                tracerJeton(carte.personnage(), ancien);
                            } else {
                                tracerJeton(carte.personnage(), image, i);
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    void tracerChoixPreView() {
        if (jeu.actionAutoriser()) {
            if (jeu.cartePasse() == 8) {
                return;
            }
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[jeu.cartePasse()];
            int[] listeDeplacementPossiblesAvecCarte = jeu.listeDeplacementPossiblesAvecCarte(carte.personnage(),
                    carte.deplacement());
            int i = 0;
            while (i < 17) {
                if (listeDeplacementPossiblesAvecCarte[i] == 1) {
                    dessinable.setColor(new Color(255, 255, 0));
                    dessinable.setStroke(new BasicStroke(5f));
                    dessinable.drawRect(i * largeurCasePlateau, debutPlateauY, largeurCasePlateau, hauteurPlateau);
                }
                i++;
            }
        }
    }

    void tracerJeton(Element element, ImagePlateau imageElement) {
        int hauteurElement = debutPlateauY;
        switch (element) {
            case COURONNE:
                break;
            case FOU:
                hauteurElement = hauteurElement + quartHauteurPlateau;
                break;
            case GARDES:
            case GARDE_GAUCHE:
            case GARDE_DROIT:
            case ROI:
                hauteurElement = hauteurElement + 2 * quartHauteurPlateau;
                break;
            case SORCIER:
                hauteurElement = hauteurElement + 3 * quartHauteurPlateau;
                break;
            default:
                break;
        }
        tracerImage(imageElement, positionJeton(element, true), hauteurElement,
                largeurCasePlateau, quartHauteurPlateau);
    }

    void tracerJeton(Element element, ImagePlateau imageElement, int casePlateau) {
        int hauteurElement = debutPlateauY;
        switch (element) {
            case COURONNE:
                break;
            case FOU:
                hauteurElement = hauteurElement + quartHauteurPlateau;
                break;
            case GARDES:
            case GARDE_GAUCHE:
            case GARDE_DROIT:
            case ROI:
                hauteurElement = hauteurElement + 2 * quartHauteurPlateau;
                break;
            case SORCIER:
                hauteurElement = hauteurElement + 3 * quartHauteurPlateau;
                break;
            default:
                break;
        }
        tracerImage(imageElement, casePlateau * largeurCasePlateau, hauteurElement,
                largeurCasePlateau, quartHauteurPlateau);
    }

    public void afficherCartesJoueurCourant() {

        if (jeu.estPartieTerminee()) {
            return;
        }

        Carte[] cartesJoueurCourant = jeu.recupererMainJoueur(jeu.joueurCourant());
        ImagePlateau image = imageCarteErreur;
        ImagePlateau imageGrise = imageCarteErreur;

        debutCartesX = largeurFenetre / 16;

        for (int i = 0; i < cartesJoueurCourant.length; i++) {
            if (i != jeu.carteActuelle()) {
                debutCartesY = 6 * hauteurFenetre / 7;
            } else {
                debutCartesY = 11 * hauteurFenetre / 14;
            }
            switch (cartesJoueurCourant[i].personnage()) {
                case ROI:
                    image = imageCarteRoi;
                    imageGrise = imageCarteRoiGrise;
                    break;
                case FOU:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteFouUn;
                            imageGrise = imageCarteFouUnGrise;
                            break;
                        case DEUX:
                            image = imageCarteFouDeux;
                            imageGrise = imageCarteFouDeuxGrise;
                            break;
                        case TROIS:
                            image = imageCarteFouTrois;
                            imageGrise = imageCarteFouTroisGrise;
                            break;
                        case QUATRE:
                            image = imageCarteFouQuatre;
                            imageGrise = imageCarteFouQuatreGrise;
                            break;
                        case CINQ:
                            image = imageCarteFouCinq;
                            imageGrise = imageCarteFouCinqGrise;
                            break;
                        case MILIEU:
                            image = imageCarteFouM;
                            imageGrise = imageCarteFouMGrise;
                            break;
                        default:
                            break;
                    }
                    break;
                case SORCIER:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteSorcierUn;
                            imageGrise = imageCarteSorcierUnGrise;
                            break;
                        case DEUX:
                            image = imageCarteSorcierDeux;
                            imageGrise = imageCarteSorcierDeuxGrise;
                            break;
                        case TROIS:
                            image = imageCarteSorcierTrois;
                            imageGrise = imageCarteSorcierTroisGrise;
                            break;
                        default:
                            break;
                    }
                    break;
                case GARDES:
                    switch (cartesJoueurCourant[i].deplacement()) {
                        case UN:
                            image = imageCarteGardesUn;
                            imageGrise = imageCarteGardesUnGrise;
                            break;
                        case UN_PLUS_UN:
                            image = imageCarteGardesUnPlusUn;
                            imageGrise = imageCarteGardesUnPlusUnGrise;
                            break;
                        case RAPPROCHE:
                            image = imageCarteGardesRaproche;
                            imageGrise = imageCarteGardesRaprocheGrise;
                            break;
                        default:
                            break;
                    }
                    break;
                case VIDE:
                    imageGrise = imageCarteVide;
                    break;
                default:
                    break;
            }
            InfoJeu etat = jeu.getEtatJeu();
            if (jeu.carteJouable(cartesJoueurCourant[i]) && etat == InfoJeu.DEBUT_TOUR) {
                tracerImage(image, (4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            } else {
                tracerImage(imageGrise, (4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            }
            if (jeu.cartePasse() == i && cartesJoueurCourant[i].personnage() != Element.VIDE) {
                dessinable.setColor(new Color(255, 255, 0));
                dessinable.setStroke(new BasicStroke(5f));
                dessinable.drawRect((4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            }
            if (jeu.carteActuelle() == i) {
                dessinable.setColor(new Color(0, 150, 255));
                dessinable.setStroke(new BasicStroke(5f));
                dessinable.drawRect((4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            }
        }
    }

    public void afficherCartesAutreJoueur() {

        debutCartesX = largeurFenetre / 16;
        debutCartesY = 0;
        ImagePlateau image = imageDosCarte;
        if (!jeu.mainJoueurSecondaireVisible) {
            for (int i = 0; i < 8; i++) {
                tracerImage(image, (4 + i) * debutCartesX, 0, largeurCarte, hauteurCarte);
            }
        } else {
            Carte[] cartesJoueurSecondaire = jeu.recupererMainJoueur((jeu.joueurCourant() + 1) % 2);
            for (int i = 0; i < cartesJoueurSecondaire.length; i++) {
                switch (cartesJoueurSecondaire[i].personnage()) {
                    case ROI:
                        image = imageCarteRoi;
                        break;
                    case FOU:
                        switch (cartesJoueurSecondaire[i].deplacement()) {
                            case UN:
                                image = imageCarteFouUn;
                                break;
                            case DEUX:
                                image = imageCarteFouDeux;
                                break;
                            case TROIS:
                                image = imageCarteFouTrois;
                                break;
                            case QUATRE:
                                image = imageCarteFouQuatre;
                                break;
                            case CINQ:
                                image = imageCarteFouCinq;
                                break;
                            case MILIEU:
                                image = imageCarteFouM;
                                break;
                            default:
                                break;
                        }
                        break;
                    case SORCIER:
                        switch (cartesJoueurSecondaire[i].deplacement()) {
                            case UN:
                                image = imageCarteSorcierUn;
                                break;
                            case DEUX:
                                image = imageCarteSorcierDeux;
                                break;
                            case TROIS:
                                image = imageCarteSorcierTrois;
                                break;
                            default:
                                break;
                        }
                        break;
                    case GARDES:
                        switch (cartesJoueurSecondaire[i].deplacement()) {
                            case UN:
                                image = imageCarteGardesUn;
                                break;
                            case UN_PLUS_UN:
                                image = imageCarteGardesUnPlusUn;
                                break;
                            case RAPPROCHE:
                                image = imageCarteGardesRaproche;
                                break;
                            default:
                                break;
                        }
                        break;
                    case VIDE:
                        image = imageDosCarte;
                        break;
                    default:
                        break;
                }
                tracerImage(image, (4 + i) * debutCartesX, 0, largeurCarte, hauteurCarte);
            }
        }

    }

    public void afficherZoneCartesJouees() {

        ImagePlateau image;
        debutCartesX = largeurFenetre / 16;
        debutCartesY = 18 * hauteurFenetre / 28;
        tracerImage(imageCadreCartesPosees, 4 * debutCartesX, debutCartesY, 8 * largeurCarte, hauteurCarte);
        Sequence<Carte> cartesJouees = Configuration.instance().nouvelleSequence();
        ;
        cartesJouees = jeu.plateau().paquet.copieSequence(jeu.plateau().paquet.tourActuel());
        Carte carte;
        int i = 0;
        while (!cartesJouees.estVide()) {
            carte = cartesJouees.extraitTete();
            switch (carte.personnage()) {
                case ROI:
                    image = imageCarteRoi;
                    break;
                case FOU:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteFouUn;
                            break;
                        case DEUX:
                            image = imageCarteFouDeux;
                            break;
                        case TROIS:
                            image = imageCarteFouTrois;
                            break;
                        case QUATRE:
                            image = imageCarteFouQuatre;
                            break;
                        case CINQ:
                            image = imageCarteFouCinq;
                            break;
                        case MILIEU:
                            image = imageCarteFouM;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case SORCIER:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteSorcierUn;
                            break;
                        case DEUX:
                            image = imageCarteSorcierDeux;
                            break;
                        case TROIS:
                            image = imageCarteSorcierTrois;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case GARDES:
                    switch (carte.deplacement()) {
                        case UN:
                            image = imageCarteGardesUn;
                            break;
                        case UN_PLUS_UN:
                            image = imageCarteGardesUnPlusUn;
                            break;
                        case RAPPROCHE:
                            image = imageCarteGardesRaproche;
                            break;
                        default:
                            image = imageCarteErreur;
                            break;
                    }
                    break;
                case VIDE:
                    image = imageCarteVide;
                    break;
                default:
                    image = imageCarteErreur;
                    break;
            }
            tracerImage(image, (4 + i) * debutCartesX, debutCartesY, largeurCarte, hauteurCarte);
            i++;
        }
    }

    private void afficherPioche() {
        int debutPiocheX = largeurFenetre / 32;
        int debutPiocheY = 18 * hauteurFenetre / 28;
        if (jeu.plateau().paquet.pioche().taille() != 0) {
            tracerImage(imageDosCarte, debutPiocheX, debutPiocheY, largeurCarte, hauteurCarte);
        } else {
            tracerImage(imageCadrePiocheDefausse, debutPiocheX, debutPiocheY, largeurCarte, hauteurCarte);
        }
        String msg = jeu.plateau().paquet.pioche().taille() + " cartes restantes";
        tracerLabel(msg, debutPiocheX, debutPiocheY + hauteurCarte + (hauteurCarte / 5));
    }

    private void afficherDefausse() {
        int debutPiocheX = largeurFenetre / 32;
        int debutPiocheY = 23 * hauteurFenetre / 28;
        if (jeu.plateau().paquet.defausse().taille() != 0) {
            tracerImage(imageDosCarte, debutPiocheX, debutPiocheY, largeurCarte, hauteurCarte);
        } else {
            tracerImage(imageCadrePiocheDefausse, debutPiocheX, debutPiocheY, largeurCarte, hauteurCarte);
        }
        String msg = "Défausse";
        tracerLabel(msg, debutPiocheX, debutPiocheY + hauteurCarte + (hauteurCarte / 5));

    }

    private void afficherBoutonAnnuler() { 
        int debutBoutonX = largeurFenetre / 16;
        int debutBoutonY = 18 * hauteurFenetre / 28;
        int largeurBouton = largeurCarte()/2;
        int hauteurBouton = hauteurCarte()/2;
        if (jeu.plateau().paquet.tourActuel().estVide()) {
            tracerImage(imageBoutonAnnulerGrise, 3 * debutBoutonX, debutBoutonY, largeurBouton, hauteurBouton);
        } else {
            tracerImage(imageBoutonAnnuler, 3 * debutBoutonX, debutBoutonY, largeurBouton, hauteurBouton);
        }
    }

    public int positionJeton(Element element, boolean enPixel) {
        if(enPixel){
            return (jeu.obtenirPositionElement(element) + taillePlateau / 2) * largeurCasePlateau;
        }
        return (jeu.obtenirPositionElement(element) + taillePlateau / 2);
    }

    // ==================
    // ===== IMAGES =====
    // ==================
    private ImagePlateau chargeImage(String nomImage) {
        // System.out.println("Chargement de l'image : " + nomImage);
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImage(in);
    }

    private ImagePlateau chargeImageGrise(String nomImage) {
        // System.out.println("Chargement de l'image : " + nomImage);
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImageGrise(in);
    }

    public void tracerImage(ImagePlateau image, int x, int y, int largeurCase, int hauteurCase) {
        dessinable.drawImage(image.image(), x, y, largeurCase, hauteurCase, null);
    }

    public void tracerLabel(String texte, int x, int y) {
        Font fonte = new Font(" TimesRoman ", Font.BOLD, 16);
        dessinable.setFont(fonte);
        dessinable.setColor(Color.white);
        dessinable.drawString(texte, x, y);
    }

    // TODO charger images depuis le fichier de configuration
    private void chargementDesImages() {
        imagePlateau = chargeImage("Plateau");

        imageTorcheAllumee = chargeImage("Torche_On");
        imageTorcheEteint = chargeImage("Torche_Off");

        imagePlateauDroit = chargeImage("Previsualisation_Droite");
        imagePlateauGauche = chargeImage("Previsualisation_Gauche");

        imageJetonGrandeCouronne = chargeImage("Jeton_Grande_CouronneV2");
        imageJetonPetiteCouronne = chargeImage("Jeton_Petite_CouronneV2");

        imageJetonGardeGauche = chargeImage("Jeton_Garde_Gauche");
        imageJetonGardeDroit = chargeImage("Jeton_Garde_Droit");
        imageJetonRoi = chargeImage("Jeton_Roi");
        imageJetonFou = chargeImage("Jeton_Fou");
        imageJetonSorcier = chargeImage("Jeton_Sorcier");

        imageJetonGardeGaucheTransparent = chargeImage("Jeton_Garde_Gauche_Transparent");
        imageJetonGardeDroitTransparent = chargeImage("Jeton_Garde_Droit_Transparent");
        imageJetonRoiTransparent = chargeImage("Jeton_Roi_Transparent");
        imageJetonFouTransparent = chargeImage("Jeton_Fou_Transparent");
        imageJetonSorcierTransparent = chargeImage("Jeton_Sorcier_Transparent");

        imageJetonGardeGaucheSelection = chargeImage("Jeton_Garde_Gauche_Selection");
        imageJetonGardeDroitSelection = chargeImage("Jeton_Garde_Droit_Selection");
        imageJetonRoiSelection = chargeImage("Jeton_Roi_Selection");
        imageJetonFouSelection = chargeImage("Jeton_Fou_Selection");
        imageJetonSorcierSelection = chargeImage("Jeton_Sorcier_Selection");

        imageJetonRoiPouvoir = chargeImage("Jeton_Roi_Pouvoir");
        imageJetonFouPouvoir = chargeImage("Jeton_Fou_Pouvoir");
        imageJetonSorcierPouvoir = chargeImage("Jeton_Sorcier_Pouvoir");

        imageJetonGardeGaucheGrise = chargeImageGrise("Jeton_Garde_Gauche");
        imageJetonGardeDroitGrise = chargeImageGrise("Jeton_Garde_Droit");
        imageJetonRoiGrise = chargeImageGrise("Jeton_Roi");
        imageJetonFouGrise = chargeImageGrise("Jeton_Fou");
        imageJetonSorcierGrise = chargeImageGrise("Jeton_Sorcier");

        imageJetonGardeGaucheGriseTransparent = chargeImageGrise("Jeton_Garde_Gauche_Transparent");
        imageJetonGardeDroitGriseTransparent = chargeImageGrise("Jeton_Garde_Droit_Transparent");
        imageJetonRoiGriseTransparent = chargeImageGrise("Jeton_Roi_Transparent");
        imageJetonFouGriseTransparent = chargeImageGrise("Jeton_Fou_Transparent");
        imageJetonSorcierGriseTransparent = chargeImageGrise("Jeton_Sorcier_Transparent");

        imageCarteErreur = chargeImage("Carte_Erreur");
        imageCarteVide = chargeImage("Carte_Vide");

        imageDosCarte = chargeImage("Dos_2");

        imageCarteRoi = chargeImage("Roi_1");

        imageCarteFouUn = chargeImage("Fou_1");
        imageCarteFouDeux = chargeImage("Fou_2");
        imageCarteFouTrois = chargeImage("Fou_3");
        imageCarteFouQuatre = chargeImage("Fou_4");
        imageCarteFouCinq = chargeImage("Fou_5");
        imageCarteFouM = chargeImage("Fou_M");

        imageCarteSorcierUn = chargeImage("Sorcier_1");
        imageCarteSorcierDeux = chargeImage("Sorcier_2");
        imageCarteSorcierTrois = chargeImage("Sorcier_3");

        imageCarteGardesUn = chargeImage("Garde_1");
        imageCarteGardesUnPlusUn = chargeImage("Garde_1plus1");
        imageCarteGardesRaproche = chargeImage("Garde_Rapproche");

        imageBandeauTour = chargeImage("Bandeau_Tour");
        imageCadreCartesPosees = chargeImage("Cadre_Cartes_Posees");
        imageCadrePiocheDefausse = chargeImage("Cadre_Pioche_Defausse");

        imageCarteRoiGrise = chargeImageGrise("Roi_1");

        imageCarteFouUnGrise = chargeImageGrise("Fou_1");
        imageCarteFouDeuxGrise = chargeImageGrise("Fou_2");
        imageCarteFouTroisGrise = chargeImageGrise("Fou_3");
        imageCarteFouQuatreGrise = chargeImageGrise("Fou_4");
        imageCarteFouCinqGrise = chargeImageGrise("Fou_5");
        imageCarteFouMGrise = chargeImageGrise("Fou_M");

        imageCarteSorcierUnGrise = chargeImageGrise("Sorcier_1");
        imageCarteSorcierDeuxGrise = chargeImageGrise("Sorcier_2");
        imageCarteSorcierTroisGrise = chargeImageGrise("Sorcier_3");

        imageCarteGardesUnGrise = chargeImageGrise("Garde_1");
        imageCarteGardesUnPlusUnGrise = chargeImageGrise("Garde_1plus1");
        imageCarteGardesRaprocheGrise = chargeImageGrise("Garde_Rapproche");

        imageBoutonAnnuler = chargeImage("Bouton_Annuler");
        imageBoutonAnnulerGrise = chargeImageGrise("Bouton_Annuler");

    }

    // ============================
    // ===== TRACER RECTANGLE =====
    // ============================
    public void tracerRectangle(int x, int y, int largeurCarte, int hauteurCarte) {
        dessinable.setStroke(new BasicStroke(EPAISSEUR_BORDURE));
        if (masquerPrevisualisation()) {
            dessinable.setColor(Color.BLACK);
        } else {
            dessinable.setColor(Color.BLACK); // YELLOW
        }
        dessinable.drawRect(x * debutZoneCartesX(), y * debutZoneCartesY(), largeurCarte(), hauteurCarte());
        System.out.println("x = " + x * debutZoneCartesX() + ", y = " + debutZoneCartesY() + ", larg = "
                + largeurCarte() + ", haut = " + hauteurCarte());
    }

    public boolean masquerPrevisualisation() {
        return masquerPrevisualisationNonAutorisee();
    }

    /*
     * public boolean masquerPrevisualisationDebut() {
     * return jeu.estAuDebut();
     * }
     */

    public boolean masquerPrevisualisationNonAutorisee() {
        return !jeu.actionAutoriser();
    }

    // ===================================================
    // ===== INFO POSITION ELEMENT GRAPHIQUE PLATEAU =====
    // ===================================================
    public int largeurCasePlateau() {
        return largeurCasePlateau;
    }

    public int hauteurCasePlateau() {
        return hauteurCasePlateau;
    }

    public int debutPlateauX() {
        return debutPlateauX;
    }

    public int debutPlateauY() {
        return debutPlateauY;
    }

    public int largeurPlateau() {
        return largeurPlateau;
    }

    public int hauteurPlateau() {
        return hauteurPlateau + debutPlateauY;
    }

    public int debutZoneCartesX() {
        return 4 * debutCartesX;
    }

    public int debutZoneCartesY() {
        return debutCartesY;
    }

    public int finZoneCartesX() {
        return debutZoneCartesX() + 8 * largeurCarte;
    }

    public int finZoneCartesY() {
        return debutCartesY + hauteurCarte;
    }

    public int largeurCarte() {
        return largeurCarte;
    }

    public int hauteurCarte() {
        return hauteurCarte;
    }

    public int quartHauteurPlateau() {
        return quartHauteurPlateau;
    }
}
