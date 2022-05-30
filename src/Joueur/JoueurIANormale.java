package Joueur;

import java.util.Random;

import Global.Element;
import Global.Deplacement;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Modele.Jeu;
import Modele.ListePlateaux;
import Structures.Couple;
import Structures.FAP;
import Structures.FAPListe;
import Structures.Sequence;

// Agnès
public class JoueurIANormale extends Joueur {

	Random random;
	Element jouee = Element.VIDE;

	public JoueurIANormale(int numeroJoueurCourant, Jeu jeu) {
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
            liste.insereTete(fap.extrait().element());
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

    void poserLesCartes(int[] cartes) {
        int i = 0;
        int nbUnPlusUn = 0;
        while (i < 8) {
            if (cartes[i] == 1) {
                if(jeu.recupererMainJoueur(jeu.joueurCourant())[i].deplacement() == Deplacement.UN_PLUS_UN){
                    if(nbUnPlusUn < 3){
                        nbUnPlusUn++;
                        jouee = jeu.recupererMainJoueur(jeu.joueurCourant())[i].personnage();
                        jeu.poserCarte(i);
                    }
                }
                else{
                    jouee = jeu.recupererMainJoueur(jeu.joueurCourant())[i].personnage();
                    jeu.poserCarte(i);
                }
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

		Evaluation eval = new Evaluation(jeu.plateau());
		double note = eval.note(jeu.joueurCourant());

		double nouvelleNote = note;
		do {
            jeu.annulerTour();
			int choix = r.nextInt(liste.taille());

			CoupleAtteindrePlateau fin = liste.extraitTete();
			while(choix != 0){
				fin = liste.extraitTete();
				liste.insereQueue(fin);
				choix--;
			}

			poserLesCartes(fin.cartes());
			mettreLesPositions(fin.positions());
			eval = new Evaluation(jeu.plateau());
			nouvelleNote = eval.note(jeu.joueurCourant());
		} while (note > nouvelleNote);

        return true;
    }
}