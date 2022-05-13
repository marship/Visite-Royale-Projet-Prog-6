import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
//import org.junit.rules.ExpectedException;

//import Global.Deplacement;
import Global.Element;
import Modele.Jeu;
//import Modele.Plateau;
//import Modele.Personnage;

public class TestPlateau {

    Jeu jeu;
    
    @Before
    public void init() {
		jeu = new Jeu(); 
	}

    @Test
    public void testInitialisation() {
        // Fonctionne

        assertEquals(-2, Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, Jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, Jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, Jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testEstPartieTerminee() { 
        // ??? 

        assertFalse(Jeu.estPartieTerminee());
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        Jeu.finDeTour();
        Jeu.deplacerElement(Element.ROI,-7);
        Jeu.finDeTour();
        assertTrue(Jeu.estPartieTerminee());

        jeu = new Jeu(); 
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        Jeu.finDeTour();
        assertFalse(Jeu.estPartieTerminee());
        Jeu.deplacerCouronne(7);
        Jeu.finDeTour();
        assertTrue(Jeu.estPartieTerminee());

        jeu = new Jeu(); 
        Jeu.deplacerCouronne(8);
        Jeu.finDeTour();
        assertTrue(Jeu.estPartieTerminee());

        jeu = new Jeu(); 
        Jeu.deplacerCouronne(-5);
        Jeu.finDeTour();
        assertFalse(Jeu.estPartieTerminee());        

    }

    @Test
    public void testEstPartieEnCours() { 
        // Fonctionne 

        assertTrue(Jeu.estPartieEnCours());
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        Jeu.finDeTour();
        Jeu.deplacerElement(Element.ROI,-7);
        Jeu.finDeTour();
        assertFalse(Jeu.estPartieEnCours());

        jeu = new Jeu();
        assertTrue(Jeu.estPartieEnCours());
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        Jeu.finDeTour();
        assertTrue(Jeu.estPartieEnCours());
        Jeu.deplacerCouronne(7);
        Jeu.finDeTour();
        assertFalse(Jeu.estPartieEnCours());

        jeu = new Jeu(); 
        assertTrue(Jeu.estPartieEnCours());
        Jeu.deplacerCouronne(8);
        Jeu.finDeTour();
        assertFalse(Jeu.estPartieEnCours());

        jeu = new Jeu(); 
        assertTrue(Jeu.estPartieEnCours());
        Jeu.deplacerCouronne(-5);
        Jeu.finDeTour();
        assertTrue(Jeu.estPartieEnCours());

    }

    @Test
    public void testChangerEtatPartie() {
        // Fonctionne

        jeu.changerEtatPartie();
        assertFalse(Jeu.estPartieEnCours());

        jeu.changerEtatPartie();
        assertTrue(Jeu.estPartieEnCours());

    }

    @Test
    public void testActionAutoriser() {
        // ???

        assertTrue(Jeu.actionAutoriser());
        jeu.changerEtatPartie();
        assertFalse(Jeu.actionAutoriser());

        jeu = new Jeu();
        assertTrue(Jeu.actionAutoriser());
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        Jeu.finDeTour();
        assertTrue(Jeu.actionAutoriser());
        Jeu.deplacerElement(Element.ROI,-7);
        Jeu.finDeTour();
        assertFalse(Jeu.actionAutoriser());

        jeu = new Jeu();
        assertTrue(Jeu.actionAutoriser());
        Jeu.deplacerCouronne(8);
        Jeu.finDeTour();
        assertFalse(Jeu.actionAutoriser());

    }

    @Test
    public void testDeplacerElement() {
        // Fonctionne

        Jeu.deplacerElement(Element.GARDE_DROIT,2);
        Jeu.finDeTour();
        assertEquals(4,Jeu.obtenirPositionElement(Element.GARDE_DROIT));

        Jeu.deplacerElement(Element.GARDE_GAUCHE,-2);
        Jeu.finDeTour();
        assertEquals(-4,Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        Jeu.deplacerElement(Element.SORCIER,1);
        Jeu.finDeTour();
        assertEquals(2,Jeu.obtenirPositionElement(Element.SORCIER));

        Jeu.deplacerElement(Element.ROI,-1);
        Jeu.finDeTour();
        assertEquals(-1,Jeu.obtenirPositionElement(Element.ROI));

        Jeu.deplacerElement(Element.SORCIER,-1);
        Jeu.finDeTour();
        assertEquals(1,Jeu.obtenirPositionElement(Element.SORCIER));

        Jeu.deplacerElement(Element.FOU,7);
        Jeu.finDeTour();
        assertEquals(6,Jeu.obtenirPositionElement(Element.FOU));      
    }

    @Test
    public void testValidationDeplacement() { 
        // Fonctionne

        assertTrue(Jeu.validationDeplacement(Element.GARDE_GAUCHE,-2));
        assertTrue(Jeu.validationDeplacement(Element.SORCIER,7));
        assertTrue(Jeu.validationDeplacement(Element.ROI,1));
        assertFalse(Jeu.validationDeplacement(Element.ROI,2));
        assertFalse(Jeu.validationDeplacement(Element.FOU,-8));
        assertFalse(Jeu.validationDeplacement(Element.GARDE_DROIT,7));

    }

    @Test
    public void testObtenirPositionElement() { 
        // Fonctionne  

        Jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        Jeu.finDeTour();
        assertEquals(-3, Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        Jeu.deplacerElement(Element.SORCIER,3);
        Jeu.finDeTour();
        assertEquals(4, Jeu.obtenirPositionElement(Element.SORCIER));

        Jeu.deplacerElement(Element.ROI,-1);
        Jeu.finDeTour();
        assertEquals(-1, Jeu.obtenirPositionElement(Element.ROI));

        Jeu.deplacerElement(Element.FOU,-5);
        Jeu.finDeTour();
        assertEquals(-6, Jeu.obtenirPositionElement(Element.FOU));

        Jeu.deplacerElement(Element.GARDE_DROIT,2);
        Jeu.finDeTour();
        assertEquals(4, Jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testObtenirPersonnageElement() {
        // Fonctionne 

        Jeu.obtenirPersonnageElement(Element.GARDE_GAUCHE).deplacerPersonnage(-1);
        Jeu.finDeTour();
        assertEquals(-3, Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        Jeu.obtenirPersonnageElement(Element.SORCIER).deplacerPersonnage(3);
        Jeu.finDeTour();
        assertEquals(4, Jeu.obtenirPositionElement(Element.SORCIER));

        Jeu.obtenirPersonnageElement(Element.ROI).deplacerPersonnage(-1);
        Jeu.finDeTour();
        assertEquals(-1, Jeu.obtenirPositionElement(Element.ROI));

        Jeu.obtenirPersonnageElement(Element.FOU).deplacerPersonnage(-5);
        Jeu.finDeTour();
        assertEquals(-6, Jeu.obtenirPositionElement(Element.FOU));

        Jeu.obtenirPersonnageElement(Element.GARDE_DROIT).deplacerPersonnage(2);
        Jeu.finDeTour();
        assertEquals(4, Jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testEchangerFouSorcier() {
        // Fonctionne

        jeu.echangerFouSorcier();
        assertEquals(-1,Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,Jeu.obtenirPositionElement(Element.FOU));

    }

    @Test
    public void testChoixPremierJoueur() {
        // Fonctionne

        jeu.choixPremierJoueur(Jeu.plateau().joueurCourant);
        assertEquals(1,Jeu.plateau().joueurCourant);

        jeu.choixPremierJoueur(0);
        assertEquals(0,Jeu.plateau().joueurCourant);
        assertEquals(-1,Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,Jeu.obtenirPositionElement(Element.FOU));

    }

    @Test
    public void testNumeroJoueurValide() {
        // Fonctionne
        
        assertEquals(1, Jeu.plateau().joueurCourant);
        assertTrue(Jeu.numeroJoueurValide(1));
        assertTrue(Jeu.numeroJoueurValide(0));
        assertFalse(Jeu.numeroJoueurValide(2));
        assertFalse(Jeu.numeroJoueurValide(-2));

    }

    @Test
    public void testChangerJoueurCourant() {
        // Fonctionne

        assertEquals(1,Jeu.plateau().joueurCourant);
        Jeu.changerJoueurCourant();
        assertEquals(0,Jeu.plateau().joueurCourant);     
        Jeu.changerJoueurCourant();
        assertEquals(1,Jeu.plateau().joueurCourant); 

    }  

    @Test
    public void testEstGagnant() { 
        // Fonctionne 

        assertFalse(Jeu.estGagnant());
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        Jeu.finDeTour();
        Jeu.deplacerElement(Element.ROI,-7);
        Jeu.finDeTour();
        assertTrue(Jeu.estGagnant());

        jeu = new Jeu(); 
        Jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        Jeu.finDeTour();
        assertFalse(Jeu.estGagnant());
        Jeu.deplacerCouronne(7);
        Jeu.finDeTour();
        assertTrue(Jeu.estGagnant());

        jeu = new Jeu(); 
        Jeu.deplacerCouronne(8);
        Jeu.finDeTour();
        assertTrue(Jeu.estGagnant());

        jeu = new Jeu(); 
        Jeu.deplacerCouronne(-5);
        Jeu.finDeTour();
        assertFalse(Jeu.estGagnant());

    }

    @Test
    public void testTraiterGagnant() {
        // Fonctionne

        Jeu.traiterGagnant();
        assertFalse(Jeu.estPartieTerminee());
        assertTrue(Jeu.estPartieEnCours());

        Jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        Jeu.finDeTour();
        Jeu.deplacerElement(Element.ROI,-7);
        Jeu.finDeTour();
        Jeu.traiterGagnant();
        assertTrue(Jeu.estPartieTerminee());
        assertFalse(Jeu.estPartieEnCours());
        assertEquals(0, Jeu.joueurGagnant);

        jeu = new Jeu(); 
        Jeu.deplacerCouronne(8);
        Jeu.finDeTour();
        Jeu.traiterGagnant();
        assertTrue(Jeu.estPartieTerminee());
        assertFalse(Jeu.estPartieEnCours());
        assertEquals(1, Jeu.joueurGagnant);

    }

    @Test
    public void testFinDeTour() {
        
        

    }    

    @Test           
    public void testDeplacerCouronne() { 
        // Fonctionne

        Jeu.deplacerCouronne(2);
        assertEquals(2,Jeu.getPositionCouronne());
        Jeu.deplacerCouronne(3);
        assertEquals(5,Jeu.getPositionCouronne());
        Jeu.deplacerCouronne(-7);
        assertEquals(-2,Jeu.getPositionCouronne());

    }

    @Test
    public void testChangerEtatCouronne() {
        // Fonctionne

        assertTrue(Jeu.getEtatCouronne());
        Jeu.changerEtatCouronne();
        assertFalse(Jeu.getEtatCouronne());
    } 

    @Test
    public void testJouerCarte() {
        // (à revoir)
        Jeu.recupererMainJoueur(Jeu.joueurCourant);

        Jeu.jouerCarte(Element.GARDE_GAUCHE, -3, 5);
        assertEquals(-3, Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));        

        Jeu.jouerCarte(Element.SORCIER, 4, 5);
        assertEquals(4, Jeu.obtenirPositionElement(Element.SORCIER)); 

        Jeu.jouerCarte(Element.ROI, -1, 5);
        assertEquals(-1, Jeu.obtenirPositionElement(Element.ROI)); 

        Jeu.jouerCarte(Element.FOU, -6, 5);
        assertEquals(-6, Jeu.obtenirPositionElement(Element.FOU)); 

        Jeu.jouerCarte(Element.GARDE_DROIT, -4, 5);
        assertEquals(-4, Jeu.obtenirPositionElement(Element.GARDE_DROIT)); 

    }
     
    @Test
    public void testJouerSequenceCarte() {
        // (à revoir)

        Jeu.recupererMainJoueur(Jeu.joueurCourant);

    }

    @Test
    public void testPoserCarte() {
        // (à revoir)

        Jeu.recupererMainJoueur(Jeu.joueurCourant);

    }

    @Test
    public void testRecupererMainJoueur() {



    }

    @Test
    public void testInitialiserDernierTypeDePersonnageJouer() {
        // Fonctionne

        Jeu.initialiserDernierTypeDePersonnageJouer();
        assertEquals(Element.VIDE, Jeu.dernierTypeDePersonnageJouer);

    }

    @Test
    public void testMajDernierTypeDePersonnageJouer() {
        // (à revoir)

        Jeu.recupererMainJoueur(Jeu.joueurCourant);
        Jeu.majDernierTypeDePersonnageJouer(5);
        assertEquals(Jeu.recupererMainJoueur(Jeu.joueurCourant)[5], Jeu.dernierTypeDePersonnageJouer);

    }

    @Test
    public void testListeCarteJouable() {



    }

    @Test
    public void testInitialiserTableau() {



    }

    @Test
    public void testPositionPlus8() {



    }

    @Test
    public void testPositionsPourCour() {



    }

    @Test
    public void testListeDeplacementPossiblesAvecCarte() {



    }

    @Test
    public void testPersonnageManipulerParLeFou() {



    }

    @Test
    public void testEstPouvoirFouActivable() {



    }

    @Test
    public void testActiverPouvoirSorcier() {



    }

    @Test
    public void testTeleportationPouvoirSorcier() {



    }

    @Test
    public void testEstPouvoirSorcierActivable() {



    }

    @Test
    public void testEstTeleportationValide() {



    }

    @Test
    public void testCalculerTeleportation() {



    }

    @Test
    public void testGetPositionCouronne() {



    }

    @Test
    public void testGetEtatCouronne() {



    }

}
