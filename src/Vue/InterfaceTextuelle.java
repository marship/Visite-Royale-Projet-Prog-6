package Vue;

import java.util.Scanner;
import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Modele.Carte;
import Modele.Jeu;
import Pattern.Observateur;
import Structures.Sequence;

public class InterfaceTextuelle implements InterfaceUtilisateur, Observateur {

    static Jeu jeu;
    static CollecteurEvenements collecteurEvenements;
    static Scanner sc;

    static int options = 0;

    InterfaceTextuelle(Jeu j, CollecteurEvenements cEvenements) {
        jeu = j;
        collecteurEvenements = cEvenements;
        sc = new Scanner(System.in);
    }

    public static void demarrer(Jeu j, CollecteurEvenements cEvenements) {
        InterfaceGraphique vue = new InterfaceGraphique(jeu, cEvenements);
        jeu = j;
        collecteurEvenements = cEvenements;
        sc = new Scanner(System.in);
        collecteurEvenements.ajouteInterfaceUtilisateur(vue);
        run();
    }

    @Override
    public void miseAJour() {
        // TODO Auto-generated method stub

    }

    @Override
    public void afficherPanel(String nomPanel) {
        // TODO Auto-generated method stub

    }

    public static void run() {

        while (!jeu.estPartieTerminee()) {
            afficherPlateauMainOptions(options);
            int choix = sc.nextInt();
            switch (choix) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    jouerCarte(choix - 1);
                    options = 1;
                    break;
                case 9:
                    if (options == 0) {
                        pouvoirSorcier();
                    } else {
                        Configuration.instance().logger()
                                .info("Vous avez déjà fait un autre type de deplacement ce tour");
                    }
                    break;
                case 0:
                    if (options == 0) {
                        pouvoirFou();
                    } else {
                        Configuration.instance().logger()
                                .info("Vous avez déjà fait un autre type de deplacement ce tour");
                    }
                    break;
                case 11:
                    if (jeu.plateau().paquet.nombreCartesElement(jeu.plateau().joueurCourant, Element.ROI, 0) >= 2
                            && (jeu.dernierTypeDePersonnageJouer == Element.ROI
                                    || jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
                        deplacerCour();
                    } else {
                        Configuration.instance().logger().info("Vous ne pouvez pas déplacer la cour !");
                    }
                    break;
                case 10:
                    if (options == 0) {
                        Configuration.instance().logger()
                                .info("Vous ne pouvez pas finir votre tour sans faire un déplacement !");
                    } else {
                        options = 0;
                        jeu.finDeTour();
                    }
                    break;
                case 666:
                    selonPerso(Element.FOU);
                    break;
                case 999:
                    Evaluation eval = new Evaluation(jeu.plateau());
                    System.out.println(eval.note(jeu.plateau().joueurCourant));
                    System.out.println(eval.note(0));
                    break;
                default:
                    Configuration.instance().logger().info("Erreur lors du choix des options");
                    break;
            }
        }
    }

    private static void selonPerso(Element fou) {
        int[] a = jeu.listeDeplacementPossiblesAvecPerso(Element.GARDE_GAUCHE);
        int i = -8;
        while (i <= 8) {
            if (i >= 0) {
                System.out.print(" ");
            }
            System.out.print(i + " ");
            i++;
        }
        System.out.println(" ");
        i = 0;
        while (i != 17) {
            System.out.print(" " + a[i] + " ");
            i++;
        }
    }

    private static void deplacerCour() {
        Configuration.instance().logger().info("De quel côté voulez vous bouger la cour ?");
        int direction = jeu.positionsPourCour();
        switch (direction) {
            case 1:
                Configuration.instance().logger().info("1 : Droite");
                break;
            case 2:
                Configuration.instance().logger().info("2 : Gauche");
                break;
            default:
                Configuration.instance().logger().info("1 : Droite");
                Configuration.instance().logger().info("2 : Gauche");
                break;
        }
        int choix = sc.nextInt();
        if (choix == 1 && (direction == 2 || direction == 0)) {
            int[] cartes = new int[2];
            cartes[0] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 0);
            cartes[1] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 1);
            jeu.deplacerCour(1, cartes);
            options = 1;
        } else {
            if (choix == 2 && (direction == 1 || direction == 0)) {
                int[] cartes = new int[2];
                cartes[0] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 0);
                cartes[1] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 1);
                jeu.deplacerCour(0, cartes);
                options = 1;
            } else {
                Configuration.instance().logger().info("Deplacement de la cour impossible dans ce sens !");
                options = 0;
            }
        }
    }

    private static void pouvoirFou() {
        if (jeu.estPouvoirFouActivable()) {
            Configuration.instance().logger().info("Que doivent bouger les cartes fou ?");
            Configuration.instance().logger().info("1 : ROI");
            Configuration.instance().logger().info("2 : GARDES");
            Configuration.instance().logger().info("3 : SORCIER");
            Configuration.instance().logger().info("4 : FOU");
            int fou = sc.nextInt();
            switch (fou) {
                case 1:
                    jeu.personnageManipulerParLeFou(Element.ROI);
                    break;

                case 2:
                    jeu.personnageManipulerParLeFou(Element.GARDES);
                    break;

                case 3:
                    jeu.personnageManipulerParLeFou(Element.SORCIER);
                    break;

                case 4:
                    jeu.personnageManipulerParLeFou(Element.FOU);
                    break;

                default:
                    break;
            }
        } else {
            Configuration.instance().logger().info("Pouvoir du fou impossible à utiliser !");
        }
    }

    private static void pouvoirSorcier() {
        Configuration.instance().logger().info("Vous pouvez TP :");
        Configuration.instance().logger().info("1 : ROI : " + jeu.estPouvoirSorcierActivable(Element.ROI));
        Configuration.instance().logger()
                .info("2 : GARDE GAUCHE : " + jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE));
        Configuration.instance().logger()
                .info("3 : GARDE DROIT : " + jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT));
        int choix = sc.nextInt();
        switch (choix) {
            case 1:
                if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
                    jeu.teleportationPouvoirSorcier(Element.ROI);
                    jeu.finDeTour();
                } else {
                    Configuration.instance().logger().info("Teleportation interdite !");
                }
                break;

            case 2:
                if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
                    jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
                    jeu.finDeTour();
                } else {
                    Configuration.instance().logger().info("Teleportation interdite !");
                }
                break;

            case 3:
                if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
                    jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
                    jeu.finDeTour();
                } else {
                    Configuration.instance().logger().info("Teleportation interdite !");
                }
                break;

            default:
                break;
        }
    }

    private static void jouerCarte(int choix) {
        if (jeu.listeCarteJouable()[choix] == 0) {
            System.out.println("Carte non identique");
        } else {
            Carte carte = jeu.recupererMainJoueur(jeu.plateau().joueurCourant)[choix];
            Element el = jeu.plateau().paquet.mainJoueur(jeu.plateau().joueurCourant)[choix].personnage();
            int[] a;
            if ((el == Element.GARDES) || ((jeu.personnageManipulerParLeFou == Element.GARDES)
                    || (jeu.personnageManipulerParLeFou == Element.GARDE_GAUCHE)
                    || (jeu.personnageManipulerParLeFou == Element.GARDE_DROIT))) {
                System.out.println("Quel garde voulez vous bouger ?");
                Configuration.instance().logger().info("1 : Gauche");
                Configuration.instance().logger().info("2 : Droite");
                int garde = sc.nextInt();
                if (garde == 1) {
                    el = Element.GARDE_GAUCHE;
                    if (jeu.personnageManipulerParLeFou == Element.GARDES) {
                        jeu.personnageManipulerParLeFou(Element.GARDE_GAUCHE);
                        System.out.println(jeu.personnageManipulerParLeFou);
                    }
                } else {
                    el = Element.GARDE_DROIT;
                    if (jeu.personnageManipulerParLeFou == Element.GARDES) {
                        jeu.personnageManipulerParLeFou(Element.GARDE_DROIT);
                    }
                }
                if(jeu.plateau().paquet.mainJoueur(jeu.plateau().joueurCourant)[choix].personnage() == Element.GARDES){
                    a = jeu.listeDeplacementPossiblesAvecCarte(el, carte.deplacement());
                }
                else{
                    a = jeu.listeDeplacementPossiblesAvecCarte(Element.FOU, carte.deplacement());
                }
            }
            else{
                a = jeu.listeDeplacementPossiblesAvecCarte(el, carte.deplacement());
            }
            int i = -8;
            
            while (i <= 8) {
                if (i >= 0) {
                    System.out.print(" ");
                }
                System.out.print(i + " ");
                i++;
            }
            System.out.println(" ");
            i = 0;
            while (i != 17) {
                System.out.print(" " + a[i] + " ");
                i++;
            }
            System.out.println("Que jouez vous ?");
            int jouer = sc.nextInt();
            if (a[jouer + 8] == 0) {
                System.out.println("Deplacement impossible !");
            } else {
                jeu.majDernierTypeDePersonnageJouer(carte.personnage());
                if (carte.personnage() == Element.FOU) {
                    jeu.jouerCarte(jeu.personnageManipulerParLeFou, jouer, choix);
                } else {
                    if ((el == Element.GARDE_GAUCHE || el == Element.GARDE_DROIT)
                            && (carte.deplacement() != Deplacement.UN)) {
                        Sequence<Element> elements = Configuration.instance().nouvelleSequence();
                        elements.insereQueue(Element.GARDE_GAUCHE);
                        elements.insereQueue(Element.GARDE_DROIT);
                        int[] positions = new int[2];
                        boolean un_plus_un = false;
                        if (carte.deplacement() == Deplacement.UN_PLUS_UN) {
                            if (el == Element.GARDE_GAUCHE) {
                                if ((jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 2 == jouer)
                                        || (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 2 == jouer)) {
                                    jeu.jouerCarte(el, jouer, choix);
                                    un_plus_un = true;
                                } else {

                                    positions[0] = jouer;
                                    if (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) > jouer) {
                                        positions[1] = jeu.obtenirPositionElement(Element.GARDE_DROIT) - 1;
                                    } else {
                                        positions[1] = jeu.obtenirPositionElement(Element.GARDE_DROIT) + 1;
                                    }
                                }
                            } else {
                                if ((jeu.obtenirPositionElement(Element.GARDE_DROIT) + 2 == jouer)
                                        || (jeu.obtenirPositionElement(Element.GARDE_DROIT) - 2 == jouer)) {
                                    jeu.jouerCarte(el, jouer, choix);
                                    un_plus_un = true;
                                } else {
                                    if (jeu.obtenirPositionElement(Element.GARDE_DROIT) > jouer) {
                                        positions[0] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE) - 1;
                                    } else {
                                        positions[0] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE) + 1;
                                    }
                                    positions[1] = jouer;
                                }
                            }
                        } else {
                            positions[0] = jeu.obtenirPositionElement(Element.ROI) - 1;
                            positions[1] = jeu.obtenirPositionElement(Element.ROI) + 1;
                        }
                        int[] cartes = new int[1];
                        cartes[0] = choix;
                        if (!un_plus_un) {
                            jeu.jouerSequenceCarte(elements, positions, cartes);
                        }
                    } else {
                        jeu.jouerCarte(el, jouer, choix);
                    }
                }
                options = 1;
            }
        }
    }

    private static void afficherPlateauMainOptions(int options) {
        Configuration.instance().logger().info("Au tour de " + jeu.plateau().joueurCourant + " de jouer !");
        jeu.plateau().afficherPlateau();
        jeu.plateau().paquet.afficherMain(jeu.plateau().joueurCourant);
        Configuration.instance().logger().info("1/8 : La carte à jouer");
        if (options == 0) {
            Configuration.instance().logger().info("0 : Le pouvoir du fou");
            Configuration.instance().logger().info("9 : Le pouvoir du sorcier");
        }
        if (jeu.plateau().paquet.nombreCartesElement(jeu.plateau().joueurCourant, Element.ROI, 0) >= 2
                && (jeu.dernierTypeDePersonnageJouer == Element.ROI
                        || jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
            Configuration.instance().logger().info("11 : Jouer deux cartes Roi");
        }
        Configuration.instance().logger().info("10 : Fin du tour");
    }

    @Override
    public void miseAJourFinDeTour() {
        // TODO Auto-generated method stub
        
    }

}
