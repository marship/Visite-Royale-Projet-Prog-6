package Joueur;

import java.util.Random;

import Global.Configuration;
import Global.Element;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.Plateau;
import Modele.Coup;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Structures.Sequence;

public class JoueurIAmelie extends Joueur {

    Element jouee = Element.VIDE;
    int baseCouronne;
    int quiDoitGagner;
    Plateau plateauDebutTour;

    public JoueurIAmelie(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
        quiDoitGagner = numeroJoueurCourant; 
	}

    void mettreLesPositions(int[] positions){
        jeu.plateau().couronne.positionnerCouronne(baseCouronne);
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
                jouee = jeu.recupererMainJoueur(jeu.joueurCourant())[i].personnage();
                jeu.poserCarte(i);
            }
            i++;
        }
    }

    void gestionHistorique(Plateau plat) {
        Coup coup = jeu.creerCoup(plat);
        if (coup != null) {
            jeu.jouerCoup(coup);
        } else {
            System.out.println("Plateau d'historique null !!!");
        }
    }

    void annule() {
            jeu.annule();
            jeu.fixerPositions();
            jeu.annulerTour();
            plateauDebutTour = jeu.plateau().clone();
    }

    @Override
	public boolean tempsEcoule() {

        quiDoitGagner = jeu.joueurCourant();

        ListePlateaux lP = new ListePlateaux(jeu);

        Sequence<CoupleAtteindrePlateau> lesWINNER = Configuration.instance().nouvelleSequence();
        Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();

        int[] positions = new int[5];
        positions[0] = jeu.obtenirPositionElement(Element.ROI);
        positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
        positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
        positions[3] = jeu.obtenirPositionElement(Element.FOU);
        positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

        CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);
        CoupleAtteindrePlateau winner = new CoupleAtteindrePlateau(positions, positions);

        double note = -10000;
        double noteAutre = 0;

        plateauDebutTour = jeu.plateau().clone();

        int j = 0;

        while (!liste.estVide()) {
            System.out.println("Boucle de base : " + j);
            j++;
            test = liste.extraitTete();
            mettreLesPositions(test.positions());
            poserLesCartes(test.cartes());
            gestionHistorique(plateauDebutTour);
            jeu.finDeTour();
            noteAutre = calculJB(1);
            if(noteAutre == note){
                lesWINNER.insereQueue(test);
            }
            if(noteAutre > note){
                while(!lesWINNER.estVide()){
                    lesWINNER.extraitTete();
                }
                lesWINNER.insereQueue(test);
                note = noteAutre;
            }
            annule();
        }

        Random r = new Random();
        int i = r.nextInt(lesWINNER.taille() + 1);
        do {
            winner = lesWINNER.extraitTete();
            i--;
        } while (i > 0);

        mettreLesPositions(winner.positions());
        poserLesCartes(winner.cartes());

        jeu.majDernierTypeDePersonnageJouer(jouee);

        return true; 
    }

    public double calculJA(int horizon){
        if(jeu.estGagnant() || horizon == 0){
            Evaluation eval = new Evaluation(jeu.plateau().clone());
            double note = eval.note(jeu.joueurCourant());
            return note;
        }
        else{
            double noteMax = -10000;
            double noteAutre = 0;

            int[] positions = new int[5];
            positions[0] = jeu.obtenirPositionElement(Element.ROI);
            positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
            positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
            positions[3] = jeu.obtenirPositionElement(Element.FOU);
            positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

            CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);

            ListePlateaux lP = new ListePlateaux(jeu);
            Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();

            plateauDebutTour = jeu.plateau().clone();

            int i = 0;
            while (!liste.estVide()) {
                System.out.println("Joueur A : " + i);
                i++;
                test = liste.extraitTete();
                mettreLesPositions(test.positions());
                poserLesCartes(test.cartes());
                gestionHistorique(plateauDebutTour);
                jeu.finDeTour();
                noteAutre = calculJA(horizon - 1);
                if(noteAutre > noteMax){
                    noteMax = noteAutre;
                }
                annule();
            }
            return noteMax;
        }
    }

    public double calculJB(int horizon){
        if(jeu.estGagnant() || horizon == 0){
            Evaluation eval = new Evaluation(jeu.plateau().clone());
            jeu.changerJoueurCourant();
            double note = eval.note(jeu.joueurCourant());
            jeu.changerJoueurCourant();
            return note;
        }
        else{
            double noteMin = 10000;
            double noteAutre = 0;

            int[] positions = new int[5];
            positions[0] = jeu.obtenirPositionElement(Element.ROI);
            positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
            positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
            positions[3] = jeu.obtenirPositionElement(Element.FOU);
            positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

            CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);

            ListePlateaux lP = new ListePlateaux(jeu);
            Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();

            plateauDebutTour = jeu.plateau().clone();

            int i = 0;
            while (!liste.estVide()) {
                System.out.println("Joueur B : " + i);
                i++;
                test = liste.extraitTete();
                mettreLesPositions(test.positions());
                poserLesCartes(test.cartes());
                gestionHistorique(plateauDebutTour);
                jeu.finDeTour();
                noteAutre = calculJA(horizon - 1);
                if(noteAutre < noteMin){
                    noteMin = noteAutre;
                }
                annule();
            }
            return noteMin;
        }
    }
    
}
