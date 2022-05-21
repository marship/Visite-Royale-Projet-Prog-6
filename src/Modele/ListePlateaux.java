package Modele;

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

    public ListePlateaux(Plateau p) {

        base = p.clone();
        jeu = new Jeu(base);
        baseGardeGauche = base.gardeGauche.positionPersonnage;
        baseGardeDroit = base.gardeDroit.positionPersonnage;
        baseSorcier = base.sorcier.positionPersonnage;
        baseRoi = base.roi.positionPersonnage;
        baseFou = base.fou.positionPersonnage;
        mainBase = base.paquet.copieTableau(base.joueurCourant);
    }

    public Sequence<TroupleAtteindrePlateau> constructionListePlateau() {
        Sequence<TroupleAtteindrePlateau> res = Configuration.instance().nouvelleSequence();

        res = calculSorcier(res);
        res = calculFou(res);

        jeu.plateau().paquet.mainJoueurs[jeu.joueurCourant()] = mainBase;
        jeu.plateau = base;
        jeu.annulerTour();

        return res;
    }

    private Sequence<TroupleAtteindrePlateau> calculSorcier(Sequence<TroupleAtteindrePlateau> res) {

        // Cas 
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_GAUCHE)) {
            jeu.teleportationPouvoirSorcier(Element.GARDE_GAUCHE);
            TroupleAtteindrePlateau n = new TroupleAtteindrePlateau(jeu.plateau().clone(), new Carte[1], Element.VIDE);
            res.insereQueue(n);
            jeu.annulerTour();
        }
        if (jeu.estPouvoirSorcierActivable(Element.ROI)) {
            jeu.teleportationPouvoirSorcier(Element.ROI);
            TroupleAtteindrePlateau n = new TroupleAtteindrePlateau(jeu.plateau().clone(), new Carte[1], Element.VIDE);
            res.insereQueue(n);
            jeu.annulerTour();
        }
        if (jeu.estPouvoirSorcierActivable(Element.GARDE_DROIT)) {
            jeu.teleportationPouvoirSorcier(Element.GARDE_DROIT);
            TroupleAtteindrePlateau n = new TroupleAtteindrePlateau(jeu.plateau().clone(), new Carte[1], Element.VIDE);
            res.insereQueue(n);
            jeu.annulerTour();
        }

        Carte[] cartesSorcier = jeu.plateau().paquet.carteSelonPerso(jeu.joueurCourant(), Element.SORCIER);
        res = calculNormal(res, cartesSorcier, cartesSorcier, baseSorcier, Element.SORCIER);
        return res;
    }

    private Sequence<TroupleAtteindrePlateau> calculFou(Sequence<TroupleAtteindrePlateau> res) {

        Carte[] cartesFou = jeu.plateau().paquet.carteSelonPerso(jeu.joueurCourant(), Element.FOU);
        res = calculNormal(res, cartesFou, cartesFou, baseFou, Element.FOU);
        return res;
    }

    private Sequence<TroupleAtteindrePlateau> calculNormal(Sequence<TroupleAtteindrePlateau> res, Carte[] cartes,
            Carte[] cartesBase, int base, Element perso) {
        int i = 0;
        Carte vide = new Carte(Element.VIDE, Deplacement.VIDE);
        while (i != cartes.length) {
            jeu.obtenirPersonnageElement(perso).positionnerPersonnage(base);
            jeu.plateau().paquet.mainJoueurs[jeu.joueurCourant()] = cartesBase;
            if (!cartes[i].estIdentique(vide)) {
                int[] deplacementRelatif = jeu.listeDeplacementPossiblesAvecCarte(perso, 
                    cartes[i].deplacement());
                int j = 0;
                while (j < 17) {
                    if (deplacementRelatif[j] == 1) {
                        jeu.jouerCarte(perso, j - 8, i);
                        TroupleAtteindrePlateau n = new TroupleAtteindrePlateau(jeu.plateau().clone(), 
                            cartes, perso);
                        res.insereQueue(n);
                        calculNormal(res, jeu.plateau().paquet.carteSelonPerso(jeu.joueurCourant(), perso), 
                            cartesBase, jeu.obtenirPositionElement(perso), perso);
                    }
                    j++;
                }
            }
            i++;
        }
        return res;
    }
}
