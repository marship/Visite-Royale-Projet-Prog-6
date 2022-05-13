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

public class TestPlateau {

    Jeu jeu;
    
    @Before
    public void init() {
		jeu = new Jeu(); 
	}

    @Test
    public void testInitialisation() {

        assertEquals(-2, Jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, Jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, Jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, Jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testEstGagnant() {  

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
    public void testEstPartieEnCours() {  

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

        jeu.changerEtatPartie();
        assertFalse(Jeu.estPartieEnCours());

        jeu.changerEtatPartie();
        assertTrue(Jeu.estPartieEnCours());

    }

    @Test
    public void testActionAutoriser() {

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

    }

    @Test
    public void testDeplacerElement() {

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

        assertTrue(Jeu.validationDeplacement(Element.GARDE_GAUCHE,-2));
        assertTrue(Jeu.validationDeplacement(Element.SORCIER,7));
        assertTrue(Jeu.validationDeplacement(Element.ROI,1));
        assertFalse(Jeu.validationDeplacement(Element.ROI,2));
        assertFalse(Jeu.validationDeplacement(Element.FOU,-8));
        assertFalse(Jeu.validationDeplacement(Element.GARDE_DROIT,7));

    }

    @Test
    public void testObtenirPositionElement() {   

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



    }

    @Test
    public void testEchangerFouSorcier() {

        jeu.echangerFouSorcier();
        assertEquals(-1,Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,Jeu.obtenirPositionElement(Element.FOU));
    }

    @Test
    public void testChoixPremierJoueur() {

        jeu.choixPremierJoueur(Jeu.plateau().joueurCourant);
        assertEquals(1,Jeu.plateau().joueurCourant);

        jeu.choixPremierJoueur(0);
        assertEquals(0,Jeu.plateau().joueurCourant);
        assertEquals(-1,Jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,Jeu.obtenirPositionElement(Element.FOU));
    }

    @Test
    public void testNumeroJoueurValide() {
        
        assertEquals(expected, actual);

    }
/*
    @Test
    public void testFinDeTour() {

    }    

    @Test           
    public void testDeplacerCouronne() { 

        jeu.deplacerCouronne(2);
        assertEquals(2,Jeu.getPositionCouronne());
        jeu.deplacerCouronne(3);
        assertEquals(5,Jeu.getPositionCouronne());
        jeu.deplacerCouronne(-7);
        assertEquals(-2,Jeu.getPositionCouronne());

    }
    @Test
    public void testChangerEtatCouronne() {

        assertTrue(Jeu.getEtatCouronne());
        jeu.changerEtatCouronne();
        assertFalse(Jeu.getEtatCouronne());
    } 

    @Test
    public void testChangerJoueurCourant() {
        plateau.changerJoueurCourant(plateau.joueurCourant);
        assertEquals(1,plateau.joueurCourant);        
    }   
*/
}
