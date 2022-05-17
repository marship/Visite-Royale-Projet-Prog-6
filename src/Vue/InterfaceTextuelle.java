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

    // ==========================
    // ===== (CONSTRUCTEUR) =====
    // ==========================
    public static void demarrer(Jeu j, CollecteurEvenements cEvenements) {
        jeu = j;
        collecteurEvenements = cEvenements;
        sc = new Scanner(System.in);
        run();
    }

    // ===============
    // ===== RUN =====
    // ===============
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
                        Configuration.instance().logger().warning("Vous avez deja fait un autre type de deplacement ce tour !");
                    }
                    break;
                case 0:
                    if (options == 0) {
                        pouvoirFou();
                    } else {
                        Configuration.instance().logger().warning("Vous avez deja fait un autre type de deplacement ce tour !");
                    }
                    break;
                case 11:
                    if (jeu.plateau().paquet.nombreCartesElement(jeu.plateau().joueurCourant, Element.ROI, 0) >= 2
                        && (jeu.dernierTypeDePersonnageJouer == Element.ROI || jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
                        deplacerCour();
                    } else {
                        Configuration.instance().logger().info("Vous ne pouvez pas deplacer la cour !");
                    }
                    break;
                case 10:
                    if (options == 0) {
                        Configuration.instance().logger().info("Vous ne pouvez pas finir votre tour sans faire un deplacement !");
                    } else {
                        options = 0;
                        jeu.finDeTour();
                        System.out.println("#################################################################\n");
                    }
                    break;
                case 666:
                    // TODO DELETE Cas de test Paul
                    selonPerso(Element.FOU);
                    break;
                case 999:
                    // TODO DELETE Cas de test Paul
                    Evaluation eval = new Evaluation(jeu.plateau());
                    System.out.println(eval.note(jeu.plateau().joueurCourant));
                    System.out.println(eval.note(0));
                    break;
                default:
                    Configuration.instance().logger().info("Erreur lors du choix des options !");
                    break;
            }
        }
    }

    // ================
    // ===== COUR =====
    // ================
    private static void deplacerCour() {

        Configuration.instance().logger().info("Selectionnez de quel cote doit se deplacer la cour");

        int direction = jeu.positionsPourCour();

        switch (direction) {
            case 1:
                System.out.println("1 : Droite");
                break;
            case 2:
                System.out.println("2 : Gauche");
                break;
            default:
                System.out.println("1 : Droite");
                System.out.println("2 : Gauche");
                break;
        }

        int choix = sc.nextInt();

        if (choix == 1 && (direction == 2 || direction == 0)) {
            directionDeplacementCour(1);
        } else if (choix == 2 && (direction == 1 || direction == 0)) {
            directionDeplacementCour(0);
        } else {
            options = 0;
            Configuration.instance().logger().warning("Deplacement de la cour impossible dans ce sens !");
        }
    }

    static void directionDeplacementCour(int direction) {
        int[] cartes = new int[2];
        cartes[0] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 0);
        cartes[1] = jeu.plateau().paquet.trouverRoi(jeu.plateau().joueurCourant, 1);
        jeu.deplacerCour(direction, cartes);
        options = 1;
    }

    // =======================
    // ===== POUVOIR FOU =====
    // =======================
    private static void pouvoirFou() {

        if (jeu.estPouvoirFouActivable()) {

            Configuration.instance().logger().info("Selectionner le personnage a controler :");

            System.out.println("1 : ROI");
            System.out.println("2 : GARDES");
            System.out.println("3 : SORCIER");
            System.out.println("4 : FOU");

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
            Configuration.instance().logger().warning("Pouvoir du fou impossible à utiliser !");
        }
    }

    // ===========================
    // ===== POUVOIR SORCIER =====
    // ===========================
    private static void pouvoirSorcier() {

        Configuration.instance().logger().info("Selectionner le personnage a teleporter :");

        System.out.println("1 : ROI (" + jeu.estPouvoirSorcierActivable(Element.ROI) + ")");
        System.out.println("2 : GARDE GAUCHE (" + jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE) + ")");
        System.out.println("3 : GARDE DROIT (" + jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT) + ")");
        
        int sorcier = sc.nextInt();

        switch (sorcier) {
            case 1:
                choixTeleportation(Element.ROI);
                break;
            case 2:
                choixTeleportation(Element.GARDE_GAUCHE);
                break;
            case 3:
                choixTeleportation(Element.GARDE_DROIT);
                break;
            default:
                Configuration.instance().logger().warning("Pouvoir du sorcier impossible à utiliser !");
                break;
        }
    }

    static void choixTeleportation(Element element) {
        if (jeu.estPouvoirSorcierActivable(element)) {
            jeu.teleportationPouvoirSorcier(element);
            jeu.finDeTour();
        } else {
            Configuration.instance().logger().warning("Teleportation du " + element.name() + " interdite !");
        }
    }

    // =======================
    // ===== JOUER CARTE ===== // TODO
    // =======================
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
                a = jeu.listeDeplacementPossiblesAvecCarte(Element.GARDES, carte.deplacement());
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

    // ============================
    // ===== AFFICHER OPTIONS =====
    // ============================
    private static void afficherPlateauMainOptions(int options) {

        System.out.println("Au tour du joueur " + jeu.joueurCourant() + " de jouer !");
        
        jeu.plateau().afficherPlateau();
        jeu.plateau().paquet.afficherMain(jeu.joueurCourant());

        System.out.println("1/8 : Selection de la carte a jouer");

        if (options == 0) {
            System.out.println("9   : Activer le pouvoir du sorcier");
            System.out.println("0   : Activer le pouvoir du fou");
        }

        if (jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.ROI, 0) >= 2
            && (jeu.dernierTypeDePersonnageJouer == Element.ROI || jeu.dernierTypeDePersonnageJouer == Element.VIDE)) {
            System.out.println("11  : Jouer deux cartes Roi (Deplacement de la cour)");
        }

        System.out.println("10  : Terminer le tour\n");
    }

    // =====================
    // ===== TEST PAUL ===== //TODO DELETE
    // =====================
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

    // ==================================
    // ===== IMPLEMENTATION INUTILE =====
    // ==================================
    @Override
    public void miseAJour() {
        // Inutile dans l'interfaceTextuelle !!
    }

    // ==================================
    // ===== IMPLEMENTATION INUTILE =====
    // ==================================
    @Override
    public void miseAJourFinDeTour() {
        // Inutile dans l'interfaceTextuelle !!
    }

    @Override
    public void afficherPanneau(String nomPanneau) {
        // Inutile dans l'interfaceTextuelle !!
    }
}
