import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
//import org.junit.Rule;
import org.junit.Test;
//import org.junit.rules.ExpectedException;

import Global.Deplacement;
import Global.Element;
import Modele.Jeu;
import Modele.Plateau;

public class TestPlateau {

    Jeu jeu;
    
    @Before
    public void init() {
		jeu = new Jeu(); 
	}

    /*
    @Test
    public void testInitialisation() {

        assertEquals(-2, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));
        assertEquals(1, jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(0, jeu.obtenirPositionElement(Element.ROI));
        assertEquals(-1, jeu.obtenirPositionElement(Element.FOU));
        assertEquals(2, jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testEstGagnant() {  

        assertFalse(jeu.estGagnant());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.deplacerElement(Element.ROI,-7);
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        assertFalse(jeu.estGagnant());
        jeu.deplacerCouronne(7);
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(8);
        assertTrue(jeu.estGagnant());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(-5);
        assertFalse(jeu.estGagnant());

    }

    @Test
    public void testEstPartieEnCours() {  

        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.deplacerElement(Element.ROI,-7);
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu();
        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        assertTrue(jeu.estPartieEnCours());
        jeu.deplacerCouronne(7);
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(8);
        assertFalse(jeu.estPartieEnCours());

        jeu = new Jeu(); 
        jeu.deplacerCouronne(-5);
        assertTrue(jeu.estPartieEnCours());

    }

    @Test
    public void testChangerEtatPartie() {

        jeu.changerEtatPartie();
        assertTrue(jeu.estPartieTerminee());
        assertFalse(jeu.estPartieEnCours());

        jeu.changerEtatPartie();
        assertFalse(jeu.estPartieTerminee());
        assertTrue(jeu.estPartieEnCours());

    }

    @Test
    public void testActionAutoriser() {

        assertTrue(jeu.actionAutoriser());
        jeu.changerEtatPartie();
        assertFalse(jeu.actionAutoriser());

        jeu = new Jeu();
        jeu.deplacerElement(Element.GARDE_GAUCHE,-8);
        jeu.deplacerElement(Element.ROI,-7);
        assertFalse(jeu.actionAutoriser());

    }

    @Test
    public void testDeplacerElement() {

        jeu.deplacerElement(Element.GARDE_DROIT,2);
        assertEquals(4,jeu.obtenirPositionElement(Element.GARDE_DROIT));

        jeu.deplacerElement(Element.GARDE_GAUCHE,-2);
        assertEquals(-4,jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,1);
        assertEquals(2,jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.ROI,-1);
        assertEquals(-1,jeu.obtenirPositionElement(Element.ROI));

        jeu.deplacerElement(Element.SORCIER,-1);
        assertEquals(1,jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.FOU,7);
        assertEquals(6,jeu.obtenirPositionElement(Element.FOU));      
    }

    @Test
    public void testValidationDeplacement() { 

        assertTrue(jeu.validationDeplacement(Element.GARDE_GAUCHE,-2));
        assertTrue(jeu.validationDeplacement(Element.SORCIER,7));
        assertFalse(jeu.validationDeplacement(Element.ROI,2));
        assertFalse(jeu.validationDeplacement(Element.FOU,-8));

    }

    @Test
    public void testObtenirPositionElement() {   

        jeu.deplacerElement(Element.GARDE_GAUCHE,-1);
        assertEquals(-3, jeu.obtenirPositionElement(Element.GARDE_GAUCHE));

        jeu.deplacerElement(Element.SORCIER,3);
        assertEquals(4, jeu.obtenirPositionElement(Element.SORCIER));

        jeu.deplacerElement(Element.ROI,-1);
        assertEquals(-1, jeu.obtenirPositionElement(Element.ROI));

        jeu.deplacerElement(Element.FOU,-5);
        assertEquals(-6, jeu.obtenirPositionElement(Element.FOU));

        jeu.deplacerElement(Element.GARDE_DROIT,2);
        assertEquals(4, jeu.obtenirPositionElement(Element.GARDE_DROIT));

    }

    @Test
    public void testObtenirPersonnageElement() {



    }

    @Test
    public void testEchangerFouSorcier() {
        jeu.echangerFouSorcier();
        assertEquals(-1,jeu.obtenirPositionElement(Element.SORCIER));
        assertEquals(1,jeu.obtenirPositionElement(Element.FOU));
    }

    /*@Test
    public void testChoixPremierJoueur() {
        jeu.choixPremierJoueur(jeu.plateau().joueurCourant);
        assertEquals(1,jeu.plateau.joueurCourant);

        jeu.choixPremierJoueur(0);
        assertEquals(0,plateau.joueurCourant);
        assertEquals(-1,plateau.sorcier.positionPersonnage());
        assertEquals(1,plateau.fou.positionPersonnage());
    }

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
    */

/*
    
    

    

    

    

    

    

    @Test
    public void testChangerJoueurCourant() {
        plateau.changerJoueurCourant(plateau.joueurCourant);
        assertEquals(1,plateau.joueurCourant);        
    }

    
*/
}
