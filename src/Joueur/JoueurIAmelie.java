package Joueur;

import java.util.Random;

import Global.Configuration;
import Global.Deplacement;
import Global.Element;
import Modele.Jeu;
import Modele.ListePlateaux;
import Modele.Plateau;
import Modele.Carte;
import Modele.Coup;
import Modele.CoupleAtteindrePlateau;
import Modele.Evaluation;
import Structures.Sequence;

public class JoueurIAmelie extends Joueur {

    Element jouee = Element.VIDE;
    int baseCouronne;
    int quiDoitGagner;
    int win = -1;
    Plateau plateauDebutTour;

    static final int HORIZON = 2;

    public JoueurIAmelie(int numeroJoueurCourant, Jeu jeu) {
		super(numeroJoueurCourant, jeu);
        quiDoitGagner = numeroJoueurCourant; 
	}

    int nbCartes(int[] cartes){
        int i = 0;
        int res = 0;
        while(i < 8){
            if(cartes[i] == 1){
                res++;
            }
            i++;
        }
        return res;
    }

    double counterStrikePositif(int[] cartes){
        double coeff = 1;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        int i = 0;
        if(jeu.typeDePersonnageJouerAuDernierTour == Element.VIDE){
            jeu.typeDePersonnageJouerAuDernierTour = Element.SORCIER;
        }
        while(i < 8){
            if(cartes[i] == 1 && (main[i].personnage() == jeu.typeDePersonnageJouerAuDernierTour)){
                coeff = 1.2;
            }
            i++;
        }
        return coeff;
    }

    double counterStrikeNegatif(int[] cartes){
        double coeff = 1;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        int i = 0;
        while(i < 8){
            if(cartes[i] == 1 && (main[i].personnage() == jeu.typeDePersonnageJouerAuDernierTour)){
                coeff = 0.8;
            }
            i++;
        }
        return coeff;
    }

    double poidsDesCartesPositif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        boolean dejaMilieu = false;
        boolean dejaRapproche = false;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        while (i < 8) {
            if (cartes[i] == 1) {
                if(main[i].deplacement() == Deplacement.MILIEU){
                    if(!dejaMilieu){
                        dejaMilieu = true;
                        coeff = coeff * 0.7;
                    }
                    else{
                        return -100;
                    }
                }
                if(main[i].deplacement() == Deplacement.RAPPROCHE){
                    if(!dejaRapproche){
                        dejaRapproche = true;
                        coeff = coeff * 0.7;
                    }
                    else{
                        return -100;
                    }
                }
                if(main[i].deplacement() == Deplacement.CINQ){
                    coeff = coeff * 0.7;
                }
            }
            i++;
        }
        return coeff;
    }

    double poidsDesCartesNegatif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        boolean dejaMilieu = false;
        boolean dejaRapproche = false;
        Carte[] main = jeu.recupererMainJoueur(jeu.joueurCourant());
        while (i < 8) {
            if (cartes[i] == 1) {
                if(main[i].deplacement() == Deplacement.MILIEU){
                    if(!dejaMilieu){
                        dejaMilieu = true;
                        coeff = coeff * 1.3;
                    }
                    else{
                        return 100;
                    }
                }
                if(main[i].deplacement() == Deplacement.RAPPROCHE){
                    if(!dejaRapproche){
                        dejaRapproche = true;
                        coeff = coeff * 1.3;
                    }
                    else{
                        return 100;
                    }
                }
                if(main[i].deplacement() == Deplacement.CINQ){
                    coeff = coeff * 1.3;
                }
            }
            i++;
        }
        return coeff;
    }

    double coeffCartesPositif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        int nb = 0;
        while (i < 8) {
            if (cartes[i] == 1) {
                nb++;
            }
            i++;
        }
        switch (nb) {
            case 0:
                coeff = 1.6;
                break;

            case 1:
                coeff = 1.3;
                break;

            case 2:
                coeff = 1.6;
                break;

            case 3:
                coeff = 1.9;
                break;

            case 4:
                coeff = 2.2;
                break;

            default:
                coeff = 2.5;
                break;
        }
        return coeff;
    }

    double coeffCartesNegatif(int[] cartes) {
        double coeff = 1;
        int i = 0;
        int nb = 0;
        while (i < 8) {
            if (cartes[i] == 1) {
                nb++;
            }
            i++;
        }
        switch (nb) {
            case 0:
                coeff = 1;
                break;

            case 1:
                coeff = 1.3;
                break;

            case 2:
                coeff = 1;
                break;

            case 3:
                coeff = 0.7;
                break;

            case 4:
                coeff = 0.4;
                break;

            default:
                coeff = 0.1;
                break;
        }
        return coeff;
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
            System.out.println("Creation d'un coup null !!!");
        }
    }

    boolean victoire(){
        if(jeu.plateau().couronne.positionCouronne() >= 7){
            win = 1;
            return true;
        }
        if(jeu.plateau().couronne.positionCouronne() <= -7){
            win = 0;
            return true;
        }
        if(jeu.plateau().roi.positionPersonnage() >= 7){
            win = 1;
            return true;
        }
        if(jeu.plateau().roi.positionPersonnage() <= -7){
            win = 0;
            return true;
        }
        return false;
    }

    void annule() {
        if(jeu.actionAutoriser()){
            jeu.annulerTour();
            jeu.annule();
            jeu.fixerPositions();
            plateauDebutTour = jeu.plateau().clone();
        }
    }

    @Override
	public boolean tempsEcoule() {

        Evaluation eval = new Evaluation(jeu.plateau().clone());
        double noteDeBase = eval.note(jeu.joueurCourant());

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
        double nouvelleNote = 0;

        plateauDebutTour = jeu.plateau().clone();
        
        while (!liste.estVide()) {
            test = liste.extraitTete();
            mettreLesPositions(test.positions());
            int posCouronne = jeu.getPositionCouronne();
            jeu.deplacerCouronne(jeu.plateau().valeurDeplacementCouronne());
            eval = new Evaluation(jeu.plateau().clone());
            nouvelleNote = eval.note(jeu.joueurCourant());
            if(nouvelleNote < 0){
                nouvelleNote = nouvelleNote * coeffCartesNegatif(test.cartes()) * poidsDesCartesNegatif(test.cartes()) * counterStrikeNegatif(test.cartes());
            } 
            else{
                nouvelleNote = nouvelleNote * coeffCartesPositif(test.cartes()) * poidsDesCartesPositif(test.cartes()) * counterStrikePositif(test.cartes());
            }
            boolean vic = victoire();
            jeu.plateau().couronne.positionnerCouronne(posCouronne);
            if(vic || !jeu.plateau().paquet.resteAssezCarteDansPioche(nbCartes(test.cartes()))){
                if(vic){
                    if(jeu.joueurCourant() == win){
                        while(!lesWINNER.estVide()){
                            lesWINNER.extraitTete();
                        }
                        lesWINNER.insereQueue(test);
                        win = -1;
                        break;
                    }
                }
                else{
                    if(jeu.obtenirPositionElement(Element.ROI) == 0 || jeu.plateau().couronne.etatCouronne()){
                        if(nouvelleNote > noteDeBase){
                            gestionHistorique(plateauDebutTour);
                            poserLesCartes(test.cartes());
                            jeu.finDeTour();
                            noteAutre = calculJB(HORIZON);
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
                    }
                    else{
                        if(jeu.joueurCourant() == 1){
                            if(jeu.obtenirPositionElement(Element.ROI) > 0){
                                while(!lesWINNER.estVide()){
                                    lesWINNER.extraitTete();
                                }
                                lesWINNER.insereQueue(test);
                                break;
                            }
                        }
                        else{
                            if(jeu.obtenirPositionElement(Element.ROI) < 0){
                                while(!lesWINNER.estVide()){
                                    lesWINNER.extraitTete();
                                }
                                lesWINNER.insereQueue(test);
                                break;
                            }
                        }
                    }
                }
            }
            else{
                if(nouvelleNote > noteDeBase){
                    gestionHistorique(plateauDebutTour);
                    poserLesCartes(test.cartes());
                    jeu.finDeTour();
                    noteAutre = calculJB(HORIZON);
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
                else{
                    mettreLesPositions(positions);
                }
            }
        }

        if(lesWINNER.estVide()){
            Random r = new Random();
            liste = lP.constructionListePlateau();
            int i = r.nextInt(liste.taille() + 1);
            do {
                winner = liste.extraitTete();
                i--;
            } while (i > 0);
        }
        else{
            Random r = new Random();
            int i = r.nextInt(lesWINNER.taille() + 1);
            do {
                winner = lesWINNER.extraitTete();
                i--;
            } while (i > 0);
        }

        mettreLesPositions(winner.positions());
        poserLesCartes(winner.cartes());

        jeu.majDernierTypeDePersonnageJouer(jouee);

        return true; 
    }

    public double calculJA(int horizon){
        if(horizon == 0){
            Evaluation eval = new Evaluation(jeu.plateau().clone());
            double note = eval.note(jeu.joueurCourant());
            return note;
        }
        else{
            double noteMax = -10000;
            double noteAutre = 0;
            double nouvelleNote = 0;

            Evaluation eval = new Evaluation(jeu.plateau().clone());
            double noteDeBase = eval.note(jeu.joueurCourant());

            int[] positions = new int[5];
            positions[0] = jeu.obtenirPositionElement(Element.ROI);
            positions[1] = jeu.obtenirPositionElement(Element.GARDE_GAUCHE);
            positions[2] = jeu.obtenirPositionElement(Element.GARDE_DROIT);
            positions[3] = jeu.obtenirPositionElement(Element.FOU);
            positions[4] = jeu.obtenirPositionElement(Element.SORCIER);

            baseCouronne = jeu.getPositionCouronne();

            CoupleAtteindrePlateau test = new CoupleAtteindrePlateau(positions, positions);

            ListePlateaux lP = new ListePlateaux(jeu);
            Sequence<CoupleAtteindrePlateau> liste = lP.constructionListePlateau();

            plateauDebutTour = jeu.plateau().clone();

            while (!liste.estVide()) {
                test = liste.extraitTete();
                mettreLesPositions(test.positions());
                int posCouronne = jeu.getPositionCouronne();
                jeu.deplacerCouronne(jeu.plateau().valeurDeplacementCouronne());
                eval = new Evaluation(jeu.plateau().clone());
                nouvelleNote = eval.note(jeu.joueurCourant());
                if(nouvelleNote < 0){
                    nouvelleNote = nouvelleNote * coeffCartesNegatif(test.cartes()) * poidsDesCartesNegatif(test.cartes()) * counterStrikeNegatif(test.cartes());
                } 
                else{
                    nouvelleNote = nouvelleNote * coeffCartesPositif(test.cartes()) * poidsDesCartesPositif(test.cartes()) * counterStrikePositif(test.cartes());
                }
                boolean vic = victoire();
                jeu.plateau().couronne.positionnerCouronne(posCouronne);
                if(vic|| !jeu.plateau().paquet.resteAssezCarteDansPioche(nbCartes(test.cartes()))){
                    if(vic){
                        if(jeu.joueurCourant() == win){
                            win = -1;
                            return 10000;
                        }
                        else{
                            win = -1;
                            return -10000;
                        }
                    }
                    else{
                        if(jeu.obtenirPositionElement(Element.ROI) == 0 || jeu.plateau().couronne.etatCouronne()){
                            if(nouvelleNote > noteDeBase){
                                poserLesCartes(test.cartes());
                                gestionHistorique(plateauDebutTour);
                                jeu.finDeTour();
                                noteAutre = calculJB(horizon - 1);
                                if(noteAutre > noteMax){
                                    noteMax = noteAutre;
                                }
                                annule();
                            }
                        }
                        else{
                            if(jeu.joueurCourant() == 1){
                                if(jeu.obtenirPositionElement(Element.ROI) > 0){
                                    return 10000;
                                }
                                else{
                                    return -10000;
                                }
                            }
                            else{
                                if(jeu.obtenirPositionElement(Element.ROI) < 0){
                                    return 10000;
                                }
                                else{
                                    return -10000;
                                }
                            }
                        }
                    }
                }
                else{
                    if(nouvelleNote > noteDeBase){
                        poserLesCartes(test.cartes());
                        gestionHistorique(plateauDebutTour);
                        jeu.finDeTour();
                        noteAutre = calculJB(horizon - 1);
                        if(noteAutre > noteMax){
                            noteMax = noteAutre;
                        }
                        annule();
                    }
                }
            }
            return noteMax;
        }
    }

    public double calculJB(int horizon){
        if(horizon == 0){
            Evaluation eval = new Evaluation(jeu.plateau().clone());
            double note = eval.note(jeu.joueurCourant());
            return note;
        }
        else{
            double noteMin = 10000;
            double noteAutre = 0;
            double nouvelleNote = 0;

            Evaluation eval = new Evaluation(jeu.plateau().clone());
            double noteDeBase = eval.note(jeu.joueurCourant());

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

            while (!liste.estVide()) {
                test = liste.extraitTete();
                mettreLesPositions(test.positions());
                int posCouronne = jeu.getPositionCouronne();
                jeu.deplacerCouronne(jeu.plateau().valeurDeplacementCouronne());
                eval = new Evaluation(jeu.plateau().clone());
                nouvelleNote = eval.note(jeu.joueurCourant());
                if(nouvelleNote < 0){
                    nouvelleNote = nouvelleNote * coeffCartesNegatif(test.cartes()) * poidsDesCartesNegatif(test.cartes()) * counterStrikeNegatif(test.cartes());
                } 
                else{
                    nouvelleNote = nouvelleNote * coeffCartesPositif(test.cartes()) * poidsDesCartesPositif(test.cartes()) * counterStrikePositif(test.cartes());
                }
                Boolean vic = victoire();
                jeu.plateau().couronne.positionnerCouronne(posCouronne);
                if(vic || !jeu.plateau().paquet.resteAssezCarteDansPioche(nbCartes(test.cartes()))){
                    if(vic){
                        if(jeu.joueurCourant() == win){
                            win = -1;
                            return -10000;
                        }
                        else{
                            win = -1;
                            return 10000;
                        }
                    }
                    else{
                        if(jeu.obtenirPositionElement(Element.ROI) == 0 || jeu.plateau().couronne.etatCouronne()){
                            if(nouvelleNote > noteDeBase){
                                poserLesCartes(test.cartes());
                                gestionHistorique(plateauDebutTour);
                                jeu.finDeTour();
                                noteAutre = calculJA(horizon - 1);
                                if(noteAutre < noteMin){
                                    noteMin = noteAutre;
                                }
                                annule();
                            }
                        }
                        else{
                            if(jeu.joueurCourant() == 1){
                                if(jeu.obtenirPositionElement(Element.ROI) > 0){
                                    return -10000;
                                }
                                else{
                                    return 10000;
                                }
                            }
                            else{
                                if(jeu.obtenirPositionElement(Element.ROI) < 0){
                                    return -10000;
                                }
                                else{
                                    return 10000;
                                }
                            }
                        }
                    }
                }
                else{
                    if(nouvelleNote > noteDeBase){
                        poserLesCartes(test.cartes());
                        gestionHistorique(plateauDebutTour);
                        jeu.finDeTour();
                        noteAutre = calculJA(horizon - 1);
                        if(noteAutre < noteMin){
                            noteMin = noteAutre;
                        }
                        annule();
                    }
                }
            }
            return noteMin;
        }
    }
}
