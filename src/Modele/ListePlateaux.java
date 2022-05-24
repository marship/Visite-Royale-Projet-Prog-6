package Modele;

import java.util.Arrays;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Structures.Sequence;

public class ListePlateaux {

    Jeu jeu;
    Plateau base;
    int baseFou;
    int baseGardeGauche;
    int baseGardeDroit;
    int baseSorcier;
    int baseRoi;
    Carte[] mainBase;

    public ListePlateaux(Jeu j) {

        jeu = j;
        baseRoi = jeu.obtenirPositionElement(Element.ROI);
        baseGardeGauche = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        baseGardeDroit = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        baseFou = jeu.obtenirPositionElement(Element.FOU);
        baseSorcier = jeu.obtenirPositionElement(Element.SORCIER);

    }

    void remettreLesPositions(int[] positions) {
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    int numeroPerso(Element perso) {
        switch (perso) {
            case ROI:
                return 0;
            case GARDE_GAUCHE:
                return 1;
            case GARDE_DROIT:
                return 2;
            case FOU:
                return 3;
            case SORCIER:
                return 4;
            default:
                return 42;
        }
    }

    public Sequence<CoupleAtteindrePlateau> constructionListePlateau() {

        int[] positions = new int[5];
        positions[0] = baseRoi;
        positions[1] = baseGardeGauche;
        positions[2] = baseGardeDroit;
        positions[3] = baseFou;
        positions[4] = baseSorcier;

        int[] cartesJouees = jeu.initialiserTableau(8, 0);

        Sequence<CoupleAtteindrePlateau> res = Configuration.instance().nouvelleSequence();

        res = calculSorcier(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        res = calculFou(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        res = calculRoi(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));
        res = calculGardes(res, Arrays.copyOf(positions, 5), Arrays.copyOf(cartesJouees, 8));

        remettreLesPositions(positions);

        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculGardes(Sequence<CoupleAtteindrePlateau> res, int[] positions,
            int[] cartesJouees) {
        // Jouer les cartes des gardes
        res = calculDeplacementGarde(res, positions, cartesJouees, 0);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculRoi(Sequence<CoupleAtteindrePlateau> res, int[] positions,
            int[] cartesJouees) {
        // Jouer les cartes du Roi en prenant en compte le fait d'en jouer deux
        int nbRoi = jeu.plateau().paquet.nombreCartesElement(jeu.joueurCourant(), Element.ROI, 0);
        res = calculDeplaceRoi(res, positions, cartesJouees, nbRoi, 0);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculSorcier(Sequence<CoupleAtteindrePlateau> res, int[] positions,
            int[] cartesJouees) {

        int[] nouv;
        // Le pouvoir du sorcier
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[1] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }
        if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[0] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
            nouv = Arrays.copyOf(positions, 5);
            nouv[2] = positions[4];
            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(nouv, cartesJouees);
            res.insereQueue(couple);
        }

        // Jouer les cartes du sorcier
        res = calculNormal(res, positions, cartesJouees, Element.SORCIER, 4);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculFou(Sequence<CoupleAtteindrePlateau> res, int[] positions,
            int[] cartesJouees) {

        // Le pouvoir du fou
        if (jeu.estPouvoirFouActivable()) {
            jeu.personnageManipulerParLeFou(Element.ROI);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.GARDES);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.SORCIER);
            res = calculPouvoirFou(res, positions, cartesJouees, Element.FOU, 3);
            jeu.personnageManipulerParLeFou(Element.FOU);
        }

        // Jouer les cartes du fou
        res = calculNormal(res, positions, cartesJouees, Element.FOU, 3);
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculNormal(Sequence<CoupleAtteindrePlateau> res, int[] base,
            int[] cartes, Element perso, int numPerso) {
        int i = 0;
        while (i < 8) {
            remettreLesPositions(base);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if (carte.personnage() == perso && cartes[i] != 1) {
                int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(perso, carte.deplacement());
                int j = 0;
                while (j < 17) {
                    if (deplacementPossibles[j] == 1) {
                        int tmp = base[numPerso];
                        base[numPerso] = j - 8;
                        cartes[i] = 1;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                        res.insereQueue(couple);
                        calculNormal(res, base, cartes, perso, numPerso);
                        base[numPerso] = tmp;
                        cartes[i] = 0;
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculPouvoirFou(Sequence<CoupleAtteindrePlateau> res, int[] base,
            int[] cartes, Element perso, int numPerso) {
        int i = 0;
        while (i < 8) {
            remettreLesPositions(base);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if (carte.personnage() == perso && cartes[i] != 1) {
                int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(perso, carte.deplacement());
                int j = 0;
                while (j < 17) {
                    if (deplacementPossibles[j] == 1) {
                        int num = 8;
                        if (jeu.personnageManipulerParLeFou == Element.GARDES) {
                            if (jeu.obtenirPositionElement(Element.ROI) > j - 8) {
                                num = numeroPerso(Element.GARDE_GAUCHE);
                            } else {
                                num = numeroPerso(Element.GARDE_DROIT);
                            }
                        } else {
                            num = numeroPerso(jeu.personnageManipulerParLeFou);
                        }
                        int tmp = base[num];
                        base[num] = j - 8;
                        cartes[i] = 1;
                        CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                        res.insereQueue(couple);
                        calculPouvoirFou(res, base, cartes, perso, numPerso);
                        base[num] = tmp;
                        cartes[i] = 0;
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculDeplaceRoi(Sequence<CoupleAtteindrePlateau> res, int[] base,
            int[] cartes, int nbRoi, int nbJouee) {
        remettreLesPositions(base);
        int tmpR = base[0];
        int tmpGG = base[1];
        int tmpGD = base[2];

        // Jouer deux cartes pour bouger la cour
        if (nbRoi >= 2) {
            if ( (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8
                    || jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) && 
                    (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8
                    && jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) ) {

                // Deplacement à gauche possible
                if (jeu.obtenirPositionElement(Element.GARDE_GAUCHE) != -8) {

                    // On met les nouvelle positions
                    base[0] = base[0] + 1;
                    base[1] = base[1] + 1;
                    base[2] = base[2] + 1;

                    // On note les cartes utilisées
                    int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte1] = 1;
                    nbJouee++;
                    nbRoi--;
                    int carte2 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte2] = 1;
                    nbJouee++;
                    nbRoi--;

                    // On ajoute ce plateau à la liste
                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                    res.insereQueue(couple);

                    // On va tester de continuer les déplacements
                    calculDeplaceRoi(res, base, cartes, nbRoi, nbJouee);

                    // On remet comme avant
                    base[0] = tmpR;
                    base[1] = tmpGG;
                    base[2] = tmpGD;
                    cartes[carte1] = 0;
                    cartes[carte2] = 0;
                    nbJouee = nbJouee - 2;
                    nbRoi = nbRoi + 2;
                }

                // Deplacement à droite possible
                if (jeu.obtenirPositionElement(Element.GARDE_DROIT) != 8) {
                    // On met les nouvelle positions
                    base[0] = base[0] - 1;
                    base[1] = base[1] - 1;
                    base[2] = base[2] - 1;

                    // On note les cartes utilisées
                    int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte1] = 1;
                    nbJouee++;
                    nbRoi--;
                    int carte2 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                    cartes[carte2] = 1;
                    nbJouee++;
                    nbRoi--;

                    // On ajoute ce plateau à la liste
                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                    res.insereQueue(couple);

                    // On va tester de continuer les déplacements
                    calculDeplaceRoi(res, base, cartes, nbRoi, nbJouee);

                    // On remet comme avant
                    base[0] = tmpR;
                    base[1] = tmpGG;
                    base[2] = tmpGD;
                    cartes[carte1] = 0;
                    cartes[carte2] = 0;
                    nbJouee = nbJouee - 2;
                    nbRoi = nbRoi + 2;
                }
            }
        }

        // Jouer une carte pour bouger le roi
        if (nbRoi >= 1) {

            // Deplacement à gauche possible
            if (base[1] != (base[0] - 1)) {

                // On note la nouvelle position
                base[0] = base[0] - 1;

                // On note la carte jouée
                int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                cartes[carte1] = 1;
                nbJouee++;
                nbRoi--;

                // On ajoute ce plateau à la liste
                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                res.insereQueue(couple);

                // On va tester de continuer les déplacements
                calculDeplaceRoi(res, base, cartes, nbRoi, nbJouee);

                // On remet comme avant
                base[0] = tmpR;
                cartes[carte1] = 0;
                nbJouee--;
                nbRoi++;
            }

            // Deplacement à droite possible
            if (base[2] != (base[0] + 1)) {

                // On note la nouvelle position
                base[0] = base[0] + 1;

                // On note la carte jouée
                int carte1 = jeu.plateau().paquet.trouverRoi(jeu.joueurCourant(), nbJouee);
                cartes[carte1] = 1;
                nbJouee++;
                nbRoi--;

                // On ajoute ce plateau à la liste
                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                res.insereQueue(couple);

                // On va tester de continuer les déplacements
                calculDeplaceRoi(res, base, cartes, nbRoi, nbJouee);

                // On remet comme avant
                base[0] = tmpR;
                cartes[carte1] = 0;
                nbJouee--;
                nbRoi++;
            }
        }
        return res;
    }

    private Sequence<CoupleAtteindrePlateau> calculDeplacementGarde(Sequence<CoupleAtteindrePlateau> res, int[] base,
            int[] cartes, int nbUnPlusUnFait) {
        int i = 0;
        int tmpGG = base[1];
        int tmpGD = base[2];
        while (i < 8) {
            remettreLesPositions(base);
            Carte carte = jeu.recupererMainJoueur(jeu.joueurCourant())[i];
            if (carte.personnage() == Element.GARDES && cartes[i] != 1) {
                int[] deplacementPossibles = jeu.listeDeplacementPossiblesAvecCarte(Element.GARDES,
                        carte.deplacement());
                int j = 0;
                while (j < 17) {
                    if (deplacementPossibles[j] == 1) {
                        if (carte.deplacement() == Deplacement.RAPPROCHE) {
                            base[1] = base[0] - 1;
                            base[2] = base[0] + 1;
                            cartes[i] = 1;
                            CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                            res.insereQueue(couple);
                            calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                            base[1] = tmpGG;
                            base[2] = tmpGD;
                            cartes[i] = 0;
                        }
                        if (carte.deplacement() == Deplacement.UN_PLUS_UN) {

                            if (nbUnPlusUnFait < 3) {

                                // Garde Gauche, 2 deplacement
                                if ((base[1] - 2 == j - 8 || base[1] + 2 == j - 8) && (base[0] > j - 8)) {
                                    base[1] = j - 8;
                                    cartes[i] = 1;
                                    nbUnPlusUnFait++;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                    res.insereQueue(couple);
                                    calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                    base[1] = tmpGG;
                                    cartes[i] = 0;
                                    nbUnPlusUnFait--;
                                }

                                // Garde Droit, 2 deplacement
                                if ((base[2] - 2 == j - 8 || base[2] + 2 == j - 8) && (base[0] < j - 8)) {
                                    base[2] = j - 8;
                                    cartes[i] = 1;
                                    nbUnPlusUnFait++;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                    res.insereQueue(couple);
                                    calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                    base[2] = tmpGD;
                                    cartes[i] = 0;
                                    nbUnPlusUnFait--;
                                }

                                // Gardes, 1+1 à gauche
                                if (base[1] - 1 == j - 8 || base[2] - 1 == j - 8) {
                                    base[1] = base[1] - 1;
                                    base[2] = base[2] - 1;
                                    cartes[i] = 1;
                                    nbUnPlusUnFait++;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                    res.insereQueue(couple);
                                    calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                    base[1] = tmpGG;
                                    base[2] = tmpGD;
                                    cartes[i] = 0;
                                    nbUnPlusUnFait--;
                                }

                                // Gardes, 1+1 à droite
                                if (base[1] + 1 == j - 8 || base[2] + 1 == j - 8) {
                                    base[1] = base[1] + 1;
                                    base[2] = base[2] + 1;
                                    cartes[i] = 1;
                                    nbUnPlusUnFait++;
                                    CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                    res.insereQueue(couple);
                                    calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                    base[1] = tmpGG;
                                    base[2] = tmpGD;
                                    cartes[i] = 0;
                                    nbUnPlusUnFait--;
                                }
                            }
                            else{
                                cartes[i] = 1;
                            }
                        }
                        if (carte.deplacement() == Deplacement.UN) {
                            // Le deplacement se fait sur le garde gauche
                            if (base[1] - 1 == j - 8 || base[1] + 1 == j - 8) {
                                base[1] = j - 8;
                                cartes[i] = 1;
                                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                res.insereQueue(couple);
                                calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                base[1] = tmpGG;
                                cartes[i] = 0;
                            }
                            // Le déplacement se fait sur le garde droit
                            else {
                                base[2] = j - 8;
                                cartes[i] = 1;
                                CoupleAtteindrePlateau couple = new CoupleAtteindrePlateau(base, cartes);
                                res.insereQueue(couple);
                                calculDeplacementGarde(res, base, cartes, nbUnPlusUnFait);
                                base[2] = tmpGD;
                                cartes[i] = 0;
                            }
                        }
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }
}
