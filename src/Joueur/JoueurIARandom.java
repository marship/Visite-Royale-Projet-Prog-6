package Joueur;

import java.util.Random;

import Global.Deplacement;
import Global.Element;
import Modele.Carte;
import Modele.CoupleAtteindrePlateau;
import Modele.Jeu;
import Modele.ListePlateaux;
import Structures.Couple;
import Structures.FAP;
import Structures.FAPListe;
import Structures.Sequence;

public class JoueurIARandom extends Joueur {

	Random random;
	int nbActions;

	public JoueurIARandom(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
		random = new Random();
	}

    public Sequence<CoupleAtteindrePlateau> melanger(Sequence<CoupleAtteindrePlateau> liste) {
        FAP<Couple<CoupleAtteindrePlateau, Integer>> fap = new FAPListe<>();
        Random r = new Random();
        while (!liste.estVide()) {
            fap.insere(new Couple<CoupleAtteindrePlateau,Integer>(liste.extraitTete(), r.nextInt(10000)));
        }
        while (!fap.estVide()) {
            liste.insereTete(fap.extrait().e());
        }
        return liste;
    }

    void mettreLesPositions(int[] positions){
        jeu.obtenirPersonnageElement(Element.ROI).positionnerPersonnage(positions[0]);
        jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).positionnerPersonnage(positions[1]);
        jeu.obtenirPersonnageElement(Element.GARDE_DROIT).positionnerPersonnage(positions[2]);
        jeu.obtenirPersonnageElement(Element.FOU).positionnerPersonnage(positions[3]);
        jeu.obtenirPersonnageElement(Element.SORCIER).positionnerPersonnage(positions[4]);
    }

    void poserLesCartes(int[] cartes){
        int i = 0;
        while(i < 8){
            if(cartes[i] == 1){
                jeu.poserCarte(i);
            }
            i++;
        }
    }

	@Override
	public boolean tempsEcoule() {
        ListePlateaux lP = new ListePlateaux(jeu);

        Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();

        liste = melanger(liste);

        Random r = new Random();

        int choix = r.nextInt(liste.taille());

        CoupleAtteindrePlateau fin = liste.extraitTete();
        while(choix != 0){
            fin = liste.extraitTete();
            choix--;
        }

        poserLesCartes(fin.cartes());
        mettreLesPositions(fin.positions());

        jeu.majDernierTypeDePersonnageJouer(Element.ROI);

        return true;
    }
}