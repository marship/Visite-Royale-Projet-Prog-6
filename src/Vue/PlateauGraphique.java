package Vue;

import Modele.Jeu;
import Modele.Plateau;
import Modele.Carte;
import Pattern.Observateur;
import Structures.Sequence;

import javax.swing.*;

import Global.Configuration;
import Global.Element;
import Global.InfoJeu;
import Global.InfoPlateau;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class PlateauGraphique extends JPanel implements Observateur {

    static final int EPAISSEUR_BORDURE = 3;

    Jeu jeu;
    Plateau plateau;
    Graphics2D dessinable;

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
    ImagePlateau imageJetonGardeGaucheSelection, imageJetonGardeDroitSelection, imageJetonRoiSelection,
            imageJetonFouSelection, imageJetonSorcierSelection;
    ImagePlateau imageJetonFouPouvoir, imageJetonSorcierPouvoir, imageJetonRoiPouvoir;
    ImagePlateau imageJetonGardeGaucheGrise, imageJetonGardeDroitGrise, imageJetonRoiGrise, imageJetonFouGrise,
            imageJetonSorcierGrise;

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
    ImagePlateau imageCarteFouUnGrise, imageCarteFouDeuxGrise, imageCarteFouTroisGrise, imageCarteFouQuatreGrise,
            imageCarteFouCinqGrise, imageCarteFouMGrise;
    ImagePlateau imageCarteSorcierUnGrise, imageCarteSorcierDeuxGrise, imageCarteSorcierTroisGrise;
    ImagePlateau imageCarteGardesUnGrise, imageCarteGardesUnPlusUnGrise, imageCarteGardesRaprocheGrise;

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
    public PlateauGraphique(Jeu j) {
        chargementDesImages();
        jeu = j;
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
        afficherCartesAutreJoueur();
        afficherZoneCartesJouees();
        //afficherInfoTour();
        afficherPioche();
        afficherCartesJoueurCourant();
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

        if (jeu.joueurCourant() == 1) {
            tracerImage(imagePlateauDroit, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);
        } else {
            tracerImage(imagePlateauGauche, debutPlateauX, debutPlateauY, largeurPlateau, hauteurPlateau);
        }

        if (jeu.getEtatCouronne()) {
            tracerImageElement(Element.COURONNE, imageJetonGrandeCouronne);
        } else {
            tracerImageElement(Element.COURONNE, imageJetonPetiteCouronne);
        }

        switch (jeu.getEtatJeu()) {
            case DEBUT_TOUR:
                tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGauche);
                tracerImageElement(Element.ROI, imageJetonRoi);
                tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroit);
                tracerImageElement(Element.FOU, imageJetonFou);
                tracerImageElement(Element.SORCIER, imageJetonSorcier);
                if (jeu.carteActuelle() != 8) {
                    switch (jeu.recupererMainJoueur(jeu.joueurCourant())[jeu.carteActuelle()].personnage()) {
                        case ROI:
                            tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerImageElement(Element.FOU, imageJetonFouGrise);
                            tracerImageElement(Element.SORCIER, imageJetonSorcierGrise);
                            break;

                        case GARDES:
                            tracerImageElement(Element.ROI, imageJetonRoiGrise);
                            tracerImageElement(Element.FOU, imageJetonFouGrise);
                            tracerImageElement(Element.SORCIER, imageJetonSorcierGrise);
                            break;

                        case FOU:
                            tracerImageElement(Element.ROI, imageJetonRoiGrise);
                            tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerImageElement(Element.SORCIER, imageJetonSorcierGrise);
                            break;

                        case SORCIER:
                            tracerImageElement(Element.ROI, imageJetonRoiGrise);
                            tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                            tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                            tracerImageElement(Element.FOU, imageJetonFouGrise);
                            break;

                        default:
                            break;
                    }
                }
                break;

            case CHOIX_FOU:
                tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheSelection);
                tracerImageElement(Element.ROI, imageJetonRoiSelection);
                tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitSelection);
                tracerImageElement(Element.FOU, imageJetonFouPouvoir);
                tracerImageElement(Element.SORCIER, imageJetonSorcierSelection);
                break;

            case CHOIX_SORCIER:
                if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
                    tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheSelection);
                } else {
                    tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                }

                if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
                    tracerImageElement(Element.ROI, imageJetonRoiSelection);
                } else {
                    tracerImageElement(Element.ROI, imageJetonRoiGrise);
                }

                if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
                    tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitSelection);
                } else {
                    tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                }
                tracerImageElement(Element.FOU, imageJetonFouGrise);
                tracerImageElement(Element.SORCIER, imageJetonSorcierPouvoir);
                break;

            case CHOIX_ROI:
                tracerImageElement(Element.ROI, imageJetonRoiPouvoir);
                tracerImageElement(Element.GARDE_GAUCHE, imageJetonGardeGaucheGrise);
                tracerImageElement(Element.GARDE_DROIT, imageJetonGardeDroitGrise);
                tracerImageElement(Element.FOU, imageJetonFouGrise);
                tracerImageElement(Element.SORCIER, imageJetonSorcierGrise);
                int d = jeu.positionsPourCour();
                if (d == 1 || d == 0) {
                    dessinable.setColor(new Color(255, 255, 0));
                    dessinable.fillOval(
                            positionJeton(jeu.obtenirPositionElement(Element.ROI)) - largeurCasePlateau
                                    + largeurCasePlateau / 3,
                            quartHauteurPlateau * 4,
                            30, 30);
                }
                if (d == 2 || d == 0) {
                    dessinable.setColor(new Color(255, 255, 0));
                    dessinable.fillOval(
                            positionJeton(jeu.obtenirPositionElement(Element.ROI)) + largeurCasePlateau
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
                    dessinable.setColor(new Color(0, 150, 255));
                    dessinable.setStroke(new BasicStroke(5f));
                    dessinable.drawRect(i * largeurCasePlateau, debutPlateauY, largeurCasePlateau, hauteurPlateau);
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

    void tracerImageElement(Element element, ImagePlateau imageElement) {
        int hauteurElement = debutPlateauY;
        switch (element) {
            case COURONNE:
                break;
            case FOU:
                hauteurElement = hauteurElement + quartHauteurPlateau;
                break;
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
        tracerImage(imageElement, positionJeton(jeu.obtenirPositionElement(element)), hauteurElement,
                largeurCasePlateau, quartHauteurPlateau);
    }

    public void afficherInfoTour() {
        String msg = "Tour de " + jeu.nomJoueurCourant();
        tracerLabel(msg, 100, hauteurCarte);
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
            if(i != jeu.carteActuelle()){
                debutCartesY = 6 * hauteurFenetre / 7;
            }
            else{
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
            if (jeu.cartePasse() == i) {
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
            Carte[] cartesJoueurSecondaire = jeu.recupererMainJoueur(jeu.joueurSecondaire());
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
        tracerLabel(msg, debutPiocheX, debutPiocheY + hauteurCarte);

    }

    public int positionJeton(int positionElement) {
        return (positionElement + taillePlateau / 2) * largeurCasePlateau;
    }

    // ==================
    // ===== IMAGES =====
    // ==================
    private ImagePlateau chargeImage(String nomImage) {
        System.out.println("Chargement de l'image : " + nomImage);
        InputStream in = Configuration.charge("Images" + File.separator + nomImage + ".png");
        return ImagePlateau.getImage(in);
    }

    private ImagePlateau chargeImageGrise(String nomImage) {
        System.out.println("Chargement de l'image : " + nomImage);
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
        imagePlateauDroit = chargeImage("Previsualisation_Droite");
        imagePlateauGauche = chargeImage("Previsualisation_Gauche");

        imageJetonGrandeCouronne = chargeImage("Jeton_Grande_CouronneV2");
        imageJetonPetiteCouronne = chargeImage("Jeton_Petite_CouronneV2");

        imageJetonGardeGauche = chargeImage("Jeton_Garde_Gauche");
        imageJetonGardeDroit = chargeImage("Jeton_Garde_Droit");
        imageJetonRoi = chargeImage("Jeton_Roi");
        imageJetonFou = chargeImage("Jeton_Fou");
        imageJetonSorcier = chargeImage("Jeton_Sorcier");

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
